package ru.tanexc.tree.domain.model

import ru.tanexc.tree.data.local.entity.NodeEntity
import ru.tanexc.tree.domain.interfaces.Model

data class Node(
    val id: Long = 0,
    val label: String = "null",
    val child: List<Long> = emptyList(),
    val parent: Long = 0,
    val description: String = ""

): Model {
    override fun asDatabaseEntity(): NodeEntity = NodeEntity(
        id = id,
        label = label,
        child = child,
        parent = parent,
        description = description
    )
}