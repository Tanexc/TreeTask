package ru.tanexc.tree.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.tanexc.tree.core.utils.State
import ru.tanexc.tree.domain.model.Node

interface NodeRepository {

    fun getNodeByLable(label: String): Flow<State<Node?>>

    fun getAllNodes(): Flow<State<List<Node>?>>

    suspend fun <T> insertNode(data: Node): State<T>

    suspend fun <T> insertNodeList(data: List<Node>): State<T>

}