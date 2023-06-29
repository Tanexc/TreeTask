package ru.tanexc.tree.presentation.node.view_model


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.tanexc.tree.core.utils.State
import ru.tanexc.tree.domain.model.Node
import ru.tanexc.tree.domain.use_case.node_use_cases.DeleteNodeListUseCase
import ru.tanexc.tree.domain.use_case.node_use_cases.DeleteNodeUseCase
import ru.tanexc.tree.domain.use_case.node_use_cases.GetNodeByIdUseCase
import ru.tanexc.tree.domain.use_case.node_use_cases.SetNodeUseCase
import javax.inject.Inject

@HiltViewModel
class NodeScreenViewModel @Inject constructor(
    private val getNodeByIdUseCase: GetNodeByIdUseCase,
    private val setNodeUseCase: SetNodeUseCase,
    private val deleteNodeListUseCase: DeleteNodeListUseCase,
    private val deleteNodeUseCase: DeleteNodeUseCase
) : ViewModel() {

    private val _currentNode: MutableState<Node?> = mutableStateOf(null)
    val currentNode by _currentNode

    private val _parentNode: MutableState<Node?> = mutableStateOf(null)
    val parentNode by _parentNode


    fun deleteChildNodeBranch(node: Node) {
        val child: List<Long> = node.child
        deleteNodeUseCase(node.id)
            .launchIn(viewModelScope)

        viewModelScope.launch(Dispatchers.IO) {
            for (childId in node.child) {
                getNodeByIdUseCase(childId).onEach { state ->
                    when (state) {
                        is State.Success -> {
                            deleteChildNodeBranch(state.data!!)
                        }

                        else -> {}
                    }
                }.launchIn(viewModelScope)
            }
        }.invokeOnCompletion {
            deleteNodeListUseCase(child).launchIn(viewModelScope)

        }
    }

    fun updateCurrentNode(nodeId: Long) {
        getNodeByIdUseCase(nodeId).onEach { state ->
            when (state) {
                is State.Success -> {
                    _currentNode.value = state.data!!
                    updateParent()
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun updateParent() {
        getNodeByIdUseCase(currentNode!!.parent).onEach { state ->
            when (state) {
                is State.Success -> {
                    _parentNode.value = state.data!!
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun changeNode(node: Node) {
        setNodeUseCase(node).launchIn(viewModelScope)
    }
}