package com.example.audionotes

import android.net.Uri
import com.vk.api.sdk.VKApiJSONResponseParser
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKHttpPostCall
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.exceptions.VKApiIllegalResponseException
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class VKAudioNoteWallPostCommand(private val message: String? = null,
                                 private val audioNotesUriList: List<Uri> = listOf(),
                           ): ApiCommand<Int>() {
    override fun onExecute(manager: VKApiManager): Int {
        val callBuilder = VKMethodCall.Builder()
            .method("wall.post")
            .version(manager.config.version)
        message?.let {
            callBuilder.args("message", it)
        }


        if (audioNotesUriList.isNotEmpty()) {
            val uploadInfo = getServerUploadInfo(manager)
            val attachments = audioNotesUriList.map { uploadMessage(it, uploadInfo, manager) }

            callBuilder.args("attachments", attachments.joinToString(","))
        }



        return manager.execute(callBuilder.build(), ResponseApiParser())
    }

    private fun getServerUploadInfo(manager: VKApiManager): VKServerUploadInfoMessage {
        val uploadInfoCall = VKMethodCall.Builder()
            .method("docs.getMessagesUploadServer")
            .args("type", "audio_message")
            .version(manager.config.version)
            .build()
        return manager.execute(uploadInfoCall, ServerUploadInfoParser())
    }

    private fun uploadMessage(uri: Uri, serverUploadInfo: VKServerUploadInfoMessage, manager: VKApiManager): String {
        val fileUploadCall = VKHttpPostCall.Builder()
            .url(serverUploadInfo.uploadUrl)
            .args("file", uri)
            .timeout(TimeUnit.MINUTES.toMillis(5))
            .retryCount(RETRY_COUNT)
            .build()
        val fileUploadInfo = manager.execute(fileUploadCall, null, FileUploadInfoParser())

        val saveCall = VKMethodCall.Builder()
            .method("docs.save")
            .args("file", fileUploadInfo.file)
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

    private class ServerUploadInfoParser : VKApiJSONResponseParser<VKServerUploadInfoMessage> {
        override fun parse(responseJson: JSONObject): VKServerUploadInfoMessage{
            try {
                val joResponse = responseJson.getJSONObject("response")
                return VKServerUploadInfoMessage(
                    uploadUrl = joResponse.getString("upload_url"),
                )
            } catch (ex: JSONException) {
                throw VKApiIllegalResponseException(ex)
            }
        }
    }

    private class FileUploadInfoParser: VKApiJSONResponseParser<VKFileUploadInfoMessage> {
        override fun parse(responseJson: JSONObject): VKFileUploadInfoMessage{
            try {
                val joResponse = responseJson
                return VKFileUploadInfoMessage(
                    file = joResponse.getString("file"),
                )
            } catch (ex: JSONException) {
                throw VKApiIllegalResponseException(ex)
            }
        }
    }

    private class SaveInfoParser: VKApiJSONResponseParser<VKSaveInfoMessage> {
        override fun parse(responseJson: JSONObject): VKSaveInfoMessage {
            try {
                val joResponse = responseJson.getJSONObject("response").getJSONObject("audio_message")
                return VKSaveInfoMessage(
                    id = joResponse.getInt("id"),
                    link_ogg = joResponse.getString("link_ogg"),
                    ownerId = joResponse.getInt("owner_id"),
                    link_mp3 = joResponse.getString("link_mp3")
                )
            } catch (ex: JSONException) {
                throw VKApiIllegalResponseException(ex)
            }
        }
    }
}