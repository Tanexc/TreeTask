package ru.tanexc.tree.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanexc.tree.core.utils.State
import ru.tanexc.tree.data.local.dao.NodeDao
import ru.tanexc.tree.domain.model.Node
import ru.tanexc.tree.domain.repository.NodeRepository
import javax.inject.Inject

class NodeRepositoryImpl @Inject constructor(
    private val nodeDao: NodeDao
) : NodeRepository {
    override fun getNodeByLable(label: String): Flow<State<Node?>> = flow {
        emit(State.Loading())
        try {
            val node = nodeDao.getNodeByLabel(label).asDomain()
            emit(State.Success(data = node))
        } catch (e: Exception) {
            emit(State.Error())
        }
    }

    override fun getAllNodes(): Flow<State<List<Node>?>> = flow {
        emit(State.Loading())
        try {
            val nodeList = nodeDao.getNodesList().map { it.asDomain() }
            emit(State.Success(data = nodeList))
        } catch (e: Exception) {
            emit(State.Error())
        }
    }


    override suspend fun <T> insertNode(data: Node): State<T> {
        return try {
            nodeDao.setNode(data.asDatabaseEntity())
            State.Success()
        } catch (e: Exception) {
            State.Error()
        }

    }

    override suspend fun <T> insertNodeList(data: List<Node>): State<T> {
        return try {
            nodeDao.setNodeList(data.map { it.asDatabaseEntity() })
            State.Success()
        } catch (e: Exception) {
            State.Error()
        }
    }
}