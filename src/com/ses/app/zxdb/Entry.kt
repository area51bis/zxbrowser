package com.ses.app.zxdb

import com.ses.app.sql.Column
import com.ses.app.sql.Key
import com.ses.app.sql.Table

@Table("entries")
class Entry {
    @Key @Column var id: Int? = null
    @Column var title: String? = null
    @Column var library_title: String? = null
    @Column var is_xrated: Boolean? = null
    @Column var machinetype_id: Byte? = null
    @Column var max_players: Byte? = null
    @Column var genretype_id: Byte? = null
    @Column var spot_genretype_id: Byte? = null
    @Column var publicationtype_id: Char? = null
    @Column var availabletype_id: Char? = null
    @Column var without_load_screen: Boolean? = null
    @Column var without_inlay: Boolean? = null
    @Column var hide_from_stp: Boolean? = null
    @Column var language_id: String? = null
    @Column var mag_ratings: String? = null
    @Column var issue_id: Int? = null
    @Column var book_isbn: String? = null
    @Column var book_pages: String? = null
}
