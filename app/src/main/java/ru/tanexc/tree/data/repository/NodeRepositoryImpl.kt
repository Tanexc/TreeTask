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
): NodeRepository {
    override fun getNodeByLable(label: String): Flow<State<Node?>> = flow {
        emit(State.Loading())
        try {
            val node = nodeDao.getNodeByLabel(label).asDomain()
            emit(State.Success(data=node))
        } catch (e: Exception) {

        }
    }

    override fun getAllNodes(): Flow<State<Node?>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> insertNode(data: Node): State<T> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> insertNodeList(data: List<Node>): State<T> {
        TODO("Not yet implemented")
    }
}