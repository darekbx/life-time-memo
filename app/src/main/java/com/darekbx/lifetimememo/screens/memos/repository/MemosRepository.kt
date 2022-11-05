package com.darekbx.lifetimememo.screens.memos.repository

import com.darekbx.lifetimememo.data.MemoDao
import com.darekbx.lifetimememo.data.dto.ContainerDto
import com.darekbx.lifetimememo.data.dto.MemoDto
import com.darekbx.lifetimememo.screens.memos.model.Container
import com.darekbx.lifetimememo.screens.memos.model.Container.Companion.toDomain
import com.darekbx.lifetimememo.screens.memos.model.Memo
import com.darekbx.lifetimememo.screens.memos.model.Memo.Companion.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MemosRepository @Inject constructor(
    private val memoDao: MemoDao
) {

    suspend fun getPath(
        parentId: String?,
        path: MutableList<String> = mutableListOf()
    ): MutableList<String> {
        if (parentId != null && parentId != "null") {
            val container = memoDao.containerSync(parentId)
            if (container != null) {
                path.add(container.title)
                return getPath(container.parentUid, path)
            }
        }
        return (path + "Root").toMutableList()
    }

    fun getContainer(parentId: String): Flow<Container> {
        return memoDao.container(parentId).map { it.toDomain() }
    }

    fun elements(containerId: String?): Flow<List<Any>> {
        val memosSource = memoDao
            .memos(containerId)
            .map { memoDtos ->
                memoDtos.map {
                    val memo = it.toDomain()
                    memo.hasLocation = memoDao.countLocations(memo.uid) > 0
                    memo.categoryName = memoDao.getCategory(memo.categoryUid).name
                    memo
                }
            }
        return when (containerId) {
            null -> memoDao.containers()
            else -> memoDao.containers(containerId)
        }.map { containerDtos ->
            containerDtos.map {
                val container = it.toDomain()
                container.childrenCount = countChildren(it.uid)
                container
            }
        }.combine(memosSource) { containers, memos ->
            containers + memos
        }
    }

    private suspend fun countChildren(containerUid: String): Int {
        val childContainersCount = memoDao.countContainers(containerUid)
        val memosCount = memoDao.countMemos(containerUid)
        return childContainersCount + memosCount
    }

    suspend fun add(memo: Memo) {
        withContext(Dispatchers.IO) {
            memoDao.add(memo.mapToDto())
        }
    }

    suspend fun add(container: Container) {
        withContext(Dispatchers.IO) {
            memoDao.add(container.mapToDto())
        }
    }

    suspend fun delete(container: Container) {
        memoDao.deleteContainer(container.uid)
    }

    private fun Memo.mapToDto(): MemoDto {
        return MemoDto(
            this.uid,
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

    private fun Container.mapToDto(): ContainerDto {
        return ContainerDto(
            this.uid,
            parentUid,
            title,
            subtitle,
            timestamp
        )
    }
}
