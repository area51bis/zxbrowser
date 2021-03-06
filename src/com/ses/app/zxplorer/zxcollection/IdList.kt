package com.ses.app.zxplorer.zxcollection

class IdList<T : IdText<*>> {
    private val _rows: LinkedHashMap<Any, T> = LinkedHashMap()
    val rows: Collection<T> get() = _rows.values

    fun addRow(row: T) {
        _rows[row.id] = row
    }

    operator fun get(key: Any?): T? = if (key != null) _rows[key] else null
}