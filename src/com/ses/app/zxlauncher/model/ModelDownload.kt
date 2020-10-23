package com.ses.app.zxlauncher.model

import com.ses.zxdb.dao.Extension
import com.ses.zxdb.dao.FileType
import java.io.File

abstract class ModelDownload() {
    lateinit var model: Model

    constructor(model: Model) : this() {
        this.model = model
    }

    fun getFile(): File = File(model.dir, getFilePath())

    /** ruta relativa a la biblioteca/modelo */
    abstract fun getFilePath(): String
    abstract fun getFileName(): String
    abstract fun getLink(): String
    abstract fun getFullUrl(): String
    abstract fun getExtension(): Extension? //TODO: cambiar por ModelFileExtension
    abstract fun getFileType(): FileType    //TODO: ¿cambiar por ModelFileType?
    abstract fun getFormat(): String?
    abstract fun getReleaseYear(): Int?
    abstract fun getMachine(): String?
    abstract fun isImage(): Boolean
}