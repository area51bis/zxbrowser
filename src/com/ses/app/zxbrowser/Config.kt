package com.ses.app.zxbrowser

import com.ses.app.zxbrowser.model.ModelDownload
import com.ses.util.all
import com.ses.util.clear
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

object Config {
    private val configFile = File(App.workingDir, "config.json")
    private lateinit var config: JSONObject

    // programas soportados
    private val programs = LinkedHashMap<String, Program>()

    // extensiones soportadas
    private val extensions = HashMap<String, ArrayList<Program>>()

    // bibliotecas
    private val libraries = ArrayList<Library>()

    init {
        if (configFile.exists()) {
            config = JSONObject(configFile.readText())

            loadPrograms(config.optJSONArray("programs"))
            loadDefaults(config.optJSONArray("default_programs"))
            loadLibraries(config.optJSONArray("libraries"))
        }
    }

    // cambia la lista de programas
    fun setPrograms(list: List<Program>) {
        // reescribe la lista de programas
        val arr = config.optJSONArray("programs") ?: JSONArray()
        arr.clear()
        list.forEach { program ->
            val o = JSONObject()
            o.put("id", program.id)
            o.put("name", program.name)
            o.put("path", program.path)
            o.put("args", program.args)
            o.put("ext", JSONArray(program.ext))
            if (program.unzip) o.put("unzip", true)
            arr.put(o)
        }

        // guarda en disco
        configFile.writer().use {
            config.write(it, 4, 0)
        }

        // recarga
        programs.clear()
        extensions.clear()
        loadPrograms(config.optJSONArray("programs"))
        loadDefaults(config.optJSONArray("default_programs"))
    }

    private fun loadPrograms(json: JSONArray?) {
        json?.all<JSONObject>()?.forEach { def ->
            val prog = loadProgram(def)
            programs[prog.id] = prog

            prog.ext.forEach { ext ->
                val extensionPrograms = extensions.getOrPut(ext) { ArrayList() }
                extensionPrograms.add(prog)
            }
        }
    }

    private fun loadProgram(json: JSONObject): Program {
        // extensiones soportadas
        val ext = ArrayList<String>()
        json.getJSONArray("ext")?.all<String>()?.forEach {
            ext.add(it)
        }

        return Program(json.getString("id"),
                json.getString("name"),
                json.getString("path"),
                json.getString("args"),
                ext.toTypedArray(),
                json.optBoolean("unzip"))
    }

    private fun loadDefaults(json: JSONArray?) {
        json?.all<JSONObject>()?.forEach { def ->
            val programId = def.getString("program")
            programs[programId]?.also { program ->
                def.optJSONArray("ext")?.all<String>()?.forEach { ext ->
                    val list = extensions.getOrPut(ext) { ArrayList() }
                    // ponerle el primero de la lista
                    list.remove(program)
                    list.add(0, program)
                }
            }
        }
    }

    private fun loadLibraries(json: JSONArray?) {
        json?.all<JSONObject>()?.forEach { o ->
            val lib = Library(o.getString("type"), o.getString("name"), o.getString("path"))
            libraries.add(lib)
        }
    }

    val allLibraries: Collection<Library> get() = libraries

    val allPrograms: Collection<Program> get() = programs.values

    fun getDefaultProgram(download: ModelDownload): Program? {
        val extension = download.getExtension()
        return if (extension != null) {
            getDefaultProgram(download.getRawExtension())
        } else {
            null
        }
    }

    fun getPrograms(download: ModelDownload): List<Program> {
        val ext = download.getRawExtension().toLowerCase()
        return extensions[ext] ?: emptyList()
    }

    fun getDefaultProgram(ext: String): Program? {
        val list = extensions[ext.toLowerCase()]
        return if ((list != null) && list.isNotEmpty()) list[0]
        else null
    }

    object general {
        val showRootNode: Boolean = false
    }
}