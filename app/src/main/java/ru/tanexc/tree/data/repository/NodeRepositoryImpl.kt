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
    override fun getNodeById(id: Long): Flow<State<Node?>> = flow {
        try {
            val node = nodeDao.getNodeById(id).asDomain()
            emit(State.Success(data = node))
        } catch (e: Exception) {
            emit(State.Error(message = e.message))
        }
    }

    override fun getAllNodes(): Flow<State<List<Node>?>> = flow {
        try {
            val nodeList = nodeDao.getNodesList().map { it.asDomain() }
            emit(State.Success(data = nodeList))
        } catch (e: Exception) {
            emit(State.Error(message = e.message))
        }
    }


    override fun insertNode(data: Node): Flow<State<Node>> = flow {
        try {
            val id = nodeDao.setNode(data.asDatabaseEntity())
            emit(State.Success(data.copy(id=id)))
        } catch (e: Exception) {
            emit(State.Error(message = e.message))
        }

    }

    override fun insertNodeList(data: List<Node>): Flow<State<List<Node>>> = flow {
        try {
            val idList = nodeDao.setNodeList(data.map { it.asDatabaseEntity() })
            emit(State.Success(data.map { it.copy(id=idList[data.indexOf(it)]) }))
        } catch (e: Exception) {
            emit(State.Error(message = e.message))
        }
    }

    override fun deleteNode(data: Node): Flow<State<Node?>> = flow {
        try {
            nodeDao.deleteNode(data.asDatabaseEntity())
            emit(State.Success(data))
        } catch (e: Exception) {
            emit(State.Error(message = e.message))
        }
    }

    override fun deleteNodeList(data: List<Node>): Flow<State<List<Node>?>> = flow {
        try {
            nodeDao.deleteNodeList(data.map { it.asDatabaseEntity() })
            emit(State.Success(data))
        } catch (e: Exception) {
            emit(State.Error(message = e.message))
        }
    }
}