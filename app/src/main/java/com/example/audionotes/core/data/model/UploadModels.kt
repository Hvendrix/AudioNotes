package com.example.audionotes

class VKSaveInfo(
    val id: Int,
//    val albumId: Int,
    val ownerId: Int
) {
    fun getAttachment() = "photo${ownerId}_$id"
}

class VKFileUploadInfo(val server: Int, val audio: String, val hash: String)

class VKServerUploadInfo(val uploadUrl: String)


class VKFileUploadInfoMessage(val file: String)

class VKServerUploadInfoMessage(val uploadUrl: String)

class VKSaveInfoMessage(
    val id: Int,
    val link_ogg: String,
    val link_mp3: String,
    val ownerId: Int
) {
    fun getAttachment() = "${link_mp3}"
}