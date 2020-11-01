package com.ses.app.zxbrowser.model.local

import com.ses.app.zxbrowser.model.*

class LocalModelEntry : ModelEntry {
    constructor() : super()
    constructor(model: Model) : super(model)

    lateinit var key: String
    private lateinit var _title: String
    private val _releaseDate = ReleaseDate()

    private val _downloads = ArrayList<ModelDownload>()

    fun addFile(download: LocalModelDownload) {
        if (_downloads.isEmpty()) {
            val nameExtractor = download.nameExtractor
            key = nameExtractor.baseName
            _title = nameExtractor.title
        }
        _downloads.add(download)
    }

    override fun getTitle(): String = _title
    override fun getGenre(): String = ""
    override fun getReleaseYear(): Int? = null
    override fun getReleaseDate(): ReleaseDate = _releaseDate
    override fun getMachine(): String = ""
    override fun getAvailability(): String = ""
    override fun getDownloads(): List<ModelDownload> = _downloads
}
