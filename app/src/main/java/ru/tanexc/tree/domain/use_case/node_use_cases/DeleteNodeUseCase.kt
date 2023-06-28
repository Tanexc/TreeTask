package ru.tanexc.tree.domain.use_case.node_use_cases

import ru.tanexc.tree.domain.model.Node
import ru.tanexc.tree.domain.repository.NodeRepository
import javax.inject.Inject

class DeleteNodeUseCase @Inject constructor(
    private val nodeRepository: NodeRepository
) {
    operator fun invoke(nodeId: Long) = nodeRepository.deleteNode(nodeId)
}