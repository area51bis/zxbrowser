package com.ses.app.zxbrowser.model.zxcollection

import com.ses.app.zxbrowser.model.ModelDownload
import com.ses.app.zxbrowser.model.ModelFileExtension
import com.ses.app.zxbrowser.zxcollection.Download
import com.ses.zxdb.dao.Extension
import com.ses.zxdb.dao.FileType
import java.io.File
import java.net.URLDecoder

class ZXCModelDownload(val entry: ZXCModelEntry, private val download: Download) : ModelDownload(entry.model) {
    private val _fileType: FileType by lazy {
        FileType().apply {
            id = download.fileType?.id!!
            text = download.fileType?.text!!
        }
    }

    private val _rawExtension: String by lazy {
        download.extension
                ?: getFileName()
                        .toLowerCase()
                        .removeSuffix(".zip")
                        .substringAfterLast('.')
    }

    override fun getType(): Type = Type.File

    override fun getFilePath(): String {
        val path = URLDecoder.decode(download.fileLink, "utf-8")
                .removePrefix("http://")
                .removePrefix("https://")
                .replace('/', File.separatorChar)
        val name = download.fileName
        return if (name == null) {
            path
        } else {
            "${path.substringBeforeLast(File.separatorChar)}${File.separatorChar}${download.fileName}"
        }
        //return URLDecoder.decode(download.fileLink, "utf-8").removePrefix("http://").removePrefix("https://")
    }

    override fun getFileName(): String = download.fileName
            ?: URLDecoder.decode(download.fileLink, "utf-8").substringAfterLast('/').substringBeforeLast('?')

    override fun getLink(): String = download.fileLink

    override fun getFullUrl(): String = download.fileLink

    override fun getExtension(): Extension? = null

    override fun getRawExtension(): String = _rawExtension

    override fun getFileType(): FileType = _fileType

    override fun getFormat(): String? = null

    override fun getReleaseYear(): Int? = download.releaseDate?.year ?: entry.getReleaseYear()

    override fun getMachine(): String? = download.machine?.text

    override fun isImage(): Boolean = ModelFileExtension.isImage(getRawExtension())

    override fun getSource(): String = ""
}