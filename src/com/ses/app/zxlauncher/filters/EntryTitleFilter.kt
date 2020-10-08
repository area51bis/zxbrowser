package com.ses.app.zxlauncher.filters

import com.ses.app.zxlauncher.model.EntryRow

class EntryTitleFilter : Filter<EntryRow> {
    var text: String?
        set(value) {
            regex = if ((value != null) && (value.isNotEmpty())) {
                (if (isRegEx) value else ".*${Regex.escape(value)}.*").toRegex(RegexOption.IGNORE_CASE)
            } else {
                null
            }
        }
    private val isRegEx: Boolean
    private var regex: Regex? = null

    constructor(text: String?, isRegEx: Boolean = false) : super() {
        this.text = text
        this.isRegEx = isRegEx
    }

    override fun check(o: EntryRow): Boolean {
        return regex?.matches(o.title) ?: true
    }
}