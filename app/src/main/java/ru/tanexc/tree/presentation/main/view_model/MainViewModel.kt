package ru.tanexc.tree.presentation.main.view_model

import android.util.Log
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
import ru.tanexc.tree.core.utils.HashTool.getHashPart
import ru.tanexc.tree.core.utils.Screen
import ru.tanexc.tree.core.utils.State
import ru.tanexc.tree.domain.model.Node
import ru.tanexc.tree.domain.use_case.node_use_cases.GetAllNodesUseCase
import ru.tanexc.tree.domain.use_case.node_use_cases.GetNodeByIdUseCase
import ru.tanexc.tree.domain.use_case.node_use_cases.SetNodeUseCase
import ru.tanexc.tree.domain.use_case.node_use_cases.SetNodesListUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNodeByIdUseCase: GetNodeByIdUseCase,
    private val getAllNodesUseCase: GetAllNodesUseCase,
    private val setNodeUseCase: SetNodeUseCase,
    private val setNodesListUseCase: SetNodesListUseCase
) : ViewModel() {

    private val _shownNode: MutableState<Node?> = mutableStateOf(null)
    val shownNode by _shownNode

    private val _shownNodeChild: MutableState<List<Node>> = mutableStateOf(emptyList())
    val shownNodeChild by _shownNodeChild

    private val _shownNodeParent: MutableState<Node?> = mutableStateOf(null)
    val shownNodeParent by _shownNodeParent

    private val _toastMessage: MutableState<String?> = mutableStateOf(null)
    val toastMessage by _toastMessage

    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.Node)
    val currentScreen by _currentScreen

    init {
        updateShownNode(0)
    }

    fun updateShownNode(id: Long) {
        getNodeByIdUseCase(id).onEach {
            when (it) {
                is State.Success -> {
                    _shownNode.value = it.data!!
                    updateParent(it.data.parent)
                    updateChild(it.data.child)

                }

                else -> {
                    _toastMessage.value = it.message
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updateParent(id: Long) {
        getNodeByIdUseCase(id).onEach {
            when (it) {
                is State.Success -> {
                    _shownNodeParent.value = it.data
                }

                else -> {
                    _toastMessage.value = it.message
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updateChild(idList: List<Long>) {
        repeat(idList.size) { index ->
            getNodeByIdUseCase(idList[index]).onEach { state ->
                when (state) {
                    is State.Success -> {
                        _shownNodeChild.value = listOf(state.data!!) + _shownNodeChild.value
                        _shownNodeChild.value.sortedBy { it.id }
                    }

                    else -> {
                        _toastMessage.value = state.message
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun createNode(description: String) {
        shownNode?.let { parent ->
            val node = Node(
                parent = parent.id,
                child = emptyList(),
                label = "",
                description = description
            )

            val hashLabel = getHashPart(
                values = listOf(
                    parent.id.toString(),
                    parent.child.toString(),
                    parent.parent.toString()
                ),
                20
            )

            setNodeUseCase(node.copy(label = hashLabel)).onEach {
                when(it) {
                    is State.Success -> _shownNodeChild.value = listOf(it.data!!) + _shownNodeChild.value
                    else -> _toastMessage.value = it.message
                }
            }
        }
    }

    fun updateCurrentScreen(screen: Screen) {
        _currentScreen.value = screen
    }

    fun updateShownNode(node: Node) {
        _shownNode.value = node
        updateChild(node.child)
        updateParent(node.parent)
    }
}