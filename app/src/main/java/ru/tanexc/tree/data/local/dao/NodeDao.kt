package ru.tanexc.tree.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.tanexc.tree.data.local.entity.NodeEntity

@Dao
interface NodeDao {
    @Query("SELECT * FROM nodes")
    suspend fun getNodesList(): List<NodeEntity>

    @Query("SELECT * FROM nodes WHERE label = :label")
    suspend fun getNodeByLabel(label: String): NodeEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setNodeList(nodeList: List<NodeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setNode(node: NodeEntity)
}