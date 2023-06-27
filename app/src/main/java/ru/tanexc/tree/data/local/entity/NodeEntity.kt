package ru.tanexc.tree.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.tanexc.tree.domain.interfaces.DatabaseEntity
import ru.tanexc.tree.domain.interfaces.Model
import ru.tanexc.tree.domain.model.Node


@Entity(tableName = "nodes")
data class NodeEntity(
    @PrimaryKey
    val label: String,
    val child: List<Long>,
    val parent: Long,
) : DatabaseEntity {


    override fun asDomain(): Node = Node(
        label = label,
        child = child,
        parent = parent
    )
}