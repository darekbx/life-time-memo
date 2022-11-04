package com.darekbx.lifetimememo.screens.memos.model

import com.darekbx.lifetimememo.data.dto.MemoDto
import com.darekbx.lifetimememo.screens.category.model.Category

class Memo(
    val uid: String,
    val parentUid: String? = null,
    val title: String,
    val timestamp: Long,
    val categoryUid: String,
    val subtitle: String? = null,
    val description: String? = null,
    val link: String? = null,
    val dateTime: Long? = null,
    val flag: Int? = null,
    val reminder: Long? = null
) {

    enum class Flag(val value: Int) {
        IMPORTANT(1),
        STICKED(2)
    }

    lateinit var categoryName: String

    var hasLocation = false
    var hasDescription = description != null
    var hasSubtitle = subtitle != null
    var hasLink = link != null

    companion object {
        fun MemoDto.toDomain() = Memo(
            uid,
            parentUid,
            title,
            timestamp,
            categoryUid,
            subtitle,
            description,
            link,
            dateTime,
            flag,
            reminder
        )
    }
}