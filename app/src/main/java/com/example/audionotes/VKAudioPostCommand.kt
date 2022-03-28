package com.example.audionotes


import android.net.Uri
import com.example.audionotes.core.data.model.AudioNote
import com.example.audionotes.core.utils.IOUtils
import com.vk.api.sdk.VKApiJSONResponseParser
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKHttpPostCall
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.exceptions.VKApiIllegalResponseException
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class VKAudioPostCommand(private val message: String? = null,
                         private val photos: List<Uri> = listOf(),
                         ): ApiCommand<Int>() {
    override fun onExecute(manager: VKApiManager): Int {
        val callBuilder = VKMethodCall.Builder()
            .method("wall.post")
            .version(manager.config.version)
        message?.let {
            callBuilder.args("message", it)
        }


        if (photos.isNotEmpty()) {
            val uploadInfo = getServerUploadInfo(manager)
            val attachments = photos.map { uploadMessage(it, uploadInfo, manager) }

            callBuilder.args("attachments", attachments.joinToString(","))
        }

        return manager.execute(callBuilder.build(), ResponseApiParser())
    }

    private fun getServerUploadInfo(manager: VKApiManager): VKServerUploadInfo {
        val uploadInfoCall = VKMethodCall.Builder()
            .method("audio.getUploadServer")
            .version(manager.config.version)
            .build()
        return manager.execute(uploadInfoCall, ServerUploadInfoParser())
    }

    private fun uploadMessage(uri: Uri, serverUploadInfo: VKServerUploadInfo, manager: VKApiManager): String {
        val fileUploadCall = VKHttpPostCall.Builder()
            .url(serverUploadInfo.uploadUrl)
            .args("file", uri)
            .timeout(TimeUnit.MINUTES.toMillis(5))
            .retryCount(RETRY_COUNT)
            .build()
        val fileUploadInfo = manager.execute(fileUploadCall, null, FileUploadInfoParser())

        val saveCall = VKMethodCall.Builder()
            .method("audio.save")
            .args("server", fileUploadInfo.server)
            .args("audio", fileUploadInfo.audio)
            .args("hash", fileUploadInfo.hash)
            .version(manager.config.version)
            .build()

        val saveInfo = manager.execute(saveCall, SaveInfoParser())

        return saveInfo.getAttachment()
    }

    companion object {
        const val RETRY_COUNT = 3
    }

    private class ResponseApiParser : VKApiJSONResponseParser<Int> {
        override fun parse(responseJson: JSONObject): Int {
            try {
                return responseJson.getJSONObject("response").getInt("post_id")
            } catch (ex: JSONException) {
                throw VKApiIllegalResponseException(ex)
            }
        }
    }

    private class ServerUploadInfoParser : VKApiJSONResponseParser<VKServerUploadInfo> {
        override fun parse(responseJson: JSONObject): VKServerUploadInfo{
            try {
                val joResponse = responseJson.getJSONObject("response")
                return VKServerUploadInfo(
                    uploadUrl = joResponse.getString("upload_url"),
                )
            } catch (ex: JSONException) {
                throw VKApiIllegalResponseException(ex)
            }
        }
    }

    private class FileUploadInfoParser: VKApiJSONResponseParser<VKFileUploadInfo> {
        override fun parse(responseJson: JSONObject): VKFileUploadInfo{
            try {
                val joResponse = responseJson
                return VKFileUploadInfo(
                    server = joResponse.getInt("server"),
                    audio = joResponse.getString("audio"),
                    hash = joResponse.getString("hash")
                )
            } catch (ex: JSONException) {
                Timber.v("t5 " + responseJson.toString())
                throw VKApiIllegalResponseException(ex)
            }
        }
    }

    private class SaveInfoParser: VKApiJSONResponseParser<VKSaveInfo> {
        override fun parse(responseJson: JSONObject): VKSaveInfo {
            try {
                val joResponse = responseJson.getJSONArray("response").getJSONObject(0)
                return VKSaveInfo(
                    id = joResponse.getInt("id"),
                    ownerId = joResponse.getInt("owner_id")
                )
            } catch (ex: JSONException) {
                throw VKApiIllegalResponseException(ex)
            }
        }
    }
}