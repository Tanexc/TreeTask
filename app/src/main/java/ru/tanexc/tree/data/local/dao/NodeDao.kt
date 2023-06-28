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

    @Query("SELECT * FROM nodes WHERE id = :id")
    suspend fun getNodeById(id: Long): NodeEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setNodeList(nodeList: List<NodeEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setNode(node: NodeEntity): Long
}