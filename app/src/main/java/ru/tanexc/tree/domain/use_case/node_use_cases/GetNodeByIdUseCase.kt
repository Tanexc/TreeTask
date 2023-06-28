package ru.tanexc.tree.domain.use_case.node_use_cases

import ru.tanexc.tree.domain.repository.NodeRepository
import javax.inject.Inject

class GetNodeByIdUseCase @Inject constructor(
    private val nodeRepository: NodeRepository
) {
    operator fun invoke(id: Long) = nodeRepository.getNodeById(id)
}