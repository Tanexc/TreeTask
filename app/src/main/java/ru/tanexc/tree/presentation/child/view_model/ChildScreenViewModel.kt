package ru.tanexc.tree.presentation.child.view_model

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
import ru.tanexc.tree.core.utils.HashTool
import ru.tanexc.tree.core.utils.State
import ru.tanexc.tree.domain.model.Node
import ru.tanexc.tree.domain.use_case.node_use_cases.DeleteNodeListUseCase
import ru.tanexc.tree.domain.use_case.node_use_cases.DeleteNodeUseCase
import ru.tanexc.tree.domain.use_case.node_use_cases.GetNodeByIdUseCase
import ru.tanexc.tree.domain.use_case.node_use_cases.SetNodeUseCase
import javax.inject.Inject

@HiltViewModel
class ChildScreenViewModel @Inject constructor(
    private val deleteNodeUseCase: DeleteNodeUseCase,
    private val deleteNodeListUseCase: DeleteNodeListUseCase,
    private val setNodeUseCase: SetNodeUseCase,
    private val getNodeByIdUseCase: GetNodeByIdUseCase
) : ViewModel() {

    private val _parentNode: MutableState<Node?> = mutableStateOf(null)
    val parent by _parentNode

    private val _child: MutableState<List<Node>?> = mutableStateOf(null)
    val child by _child

    private val _toastMessage: MutableState<String?> = mutableStateOf(null)
    val toastMessage by _toastMessage


    fun updateParent(parent: Node) {
        _parentNode.value = parent

    }

    fun updateNodeChild(nodeList: List<Node>) {
        _child.value = nodeList
    }

    fun createNode(
        description: String,
        onCreated: (Node, List<Node>) -> Unit
    ) {
        parent?.let { parent ->
            val node = Node(
                parent = parent.id,
                child = emptyList(),
                label = "",
                description = description
            )

            val hashLabel = HashTool.getHashPart(
                values = listOf(
                    parent.id.toString(),
                    parent.child.toString(),
                    parent.parent.toString(),
                    description
                ),
                40
            )

            setNodeUseCase(node.copy(label = hashLabel)).onEach { state ->
                when (state) {
                    is State.Success -> {
                        _child.value = listOf(state.data!!) + (_child.value ?: listOf())
                        _child.value!!.sortedBy { it.id }
                        onCreated(parent.copy(child=child!!.map { it.id }), child!!)
                        setParentNode(parent.copy(child=child!!.map { it.id }))
                    }

                    else -> {
                        _toastMessage.value = state.message
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun setParentNode(parent: Node) {
        _parentNode.value = parent
        setNodeUseCase(parent).onEach { state ->
            when (state) {
                is State.Success -> {
                    _parentNode.value = state.data!!
                }

                else -> {
                    _toastMessage.value = state.message
                }
            }
        }.launchIn(viewModelScope)
    }


    fun deleteChildNodeBranch(node: Node) {
        deleteNodeUseCase(node.id).launchIn(viewModelScope)

        if (node.parent == parent!!.id) {
            _child.value = _child.value!!.filter { it.id != node.id }
            setParentNode(parent!!.copy(child=child!!.map { it.id }))
        }

        val child: List<Long> = node.child
        viewModelScope.launch(Dispatchers.IO) {
            for (childId in node.child) {
                getNodeByIdUseCase(childId).onEach { state ->
                    when (state) {
                        is State.Success -> {
                            deleteChildNodeBranch(state.data!!)
                        }

                        else -> {
                            _toastMessage.value = state.message
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }.invokeOnCompletion {
            deleteNodeListUseCase(child).launchIn(viewModelScope)
        }
    }

}