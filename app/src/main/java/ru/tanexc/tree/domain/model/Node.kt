package ru.tanexc.tree.domain.model

import ru.tanexc.tree.data.local.entity.NodeEntity
import ru.tanexc.tree.domain.interfaces.DatabaseEntity
import ru.tanexc.tree.domain.interfaces.Model

data class Node(
    val label: String,
    val child: List<Long>,
    val parent: Long

): Model {
    override fun asDatabaseEntity(): NodeEntity = NodeEntity(
        label = label,
        child = child,
        parent = parent
    )
}