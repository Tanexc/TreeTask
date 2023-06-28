package ru.tanexc.tree.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.tanexc.tree.core.utils.State
import ru.tanexc.tree.domain.model.Node

interface NodeRepository {

    fun getNodeById(id: Long): Flow<State<Node?>>

    fun getAllNodes(): Flow<State<List<Node>?>>

    fun insertNode(data: Node): Flow<State<Node>>

    fun insertNodeList(data: List<Node>): Flow<State<List<Node>>>

    fun deleteNode(id: Long): Flow<State<Long?>>

    fun deleteNodeList(data: List<Long>): Flow<State<List<Long>?>>

}