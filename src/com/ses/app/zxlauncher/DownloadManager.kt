package com.ses.app.zxlauncher

import com.ses.app.zxlauncher.model.EntryDownload
import com.ses.app.zxlauncher.ui.ProgressDialog
import com.ses.net.Http
import javafx.application.Platform
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class DownloadManager {
    var rootDir = App.workingDir // directorio raíz
    var downloadDir: File = File(rootDir, "zxdb") // directorio de descargas

    fun getFile(download: EntryDownload): File = File(downloadDir, download.getLink())

    fun download(download: EntryDownload, completion: (file: File) -> Unit) {
        val file = getFile(download)

        if (file.exists()) {
            completion(file)
            return
        }

        val dialog = ProgressDialog.create().apply {
            title = T("download")
            message = download.getFileName()
            show()
        }

        GlobalScope.launch {
            Http().apply {
                file.parentFile.mkdirs()
                request = download.getFullUrl()

                getFile(file) { status, progress ->
                    when (status) {
                        Http.Status.Connecting -> Platform.runLater { dialog.message = T("connecting_") }
                        Http.Status.Connected -> Platform.runLater { dialog.message = T("downloading_fmt").format(download.getFileName()) }
                        Http.Status.Completed -> completion(file)
                    }
                }
                Platform.runLater { dialog.hide() }
            }
        }
    }

    fun exists(download: EntryDownload): Boolean = getFile(download).exists()
}