package ru.tanexc.tree.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.tanexc.tree.domain.interfaces.DatabaseEntity
import ru.tanexc.tree.domain.model.Node


@Entity(tableName = "nodes")
data class NodeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val label: String,
    val child: List<Long>,
    val parent: Long,
    val description: String
) : DatabaseEntity {

    override fun asDomain(): Node = Node(
        id = id,
        label = label,
        child = child,
        parent = parent,
        description = description
    )
}