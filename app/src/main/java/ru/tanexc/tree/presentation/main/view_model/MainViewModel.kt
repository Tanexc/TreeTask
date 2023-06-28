package ru.tanexc.tree.presentation.main.view_model

import androidx.compose.material3.ColorScheme
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
import ru.tanexc.tree.domain.model.Settings
import ru.tanexc.tree.domain.use_case.node_use_cases.DeleteNodeListUseCase
import ru.tanexc.tree.domain.use_case.node_use_cases.DeleteNodeUseCase
import ru.tanexc.tree.domain.use_case.node_use_cases.GetNodeByIdUseCase
import ru.tanexc.tree.domain.use_case.node_use_cases.SetNodeUseCase
import ru.tanexc.tree.domain.use_case.settings_use_cases.GetSettingsUseCase
import ru.tanexc.tree.domain.use_case.settings_use_cases.SetSettingsUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNodeByIdUseCase: GetNodeByIdUseCase,
    private val setNodeUseCase: SetNodeUseCase,
    private val deleteNodeListUseCase: DeleteNodeListUseCase,
    private val deleteNodeUseCase: DeleteNodeUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val setSettingsUseCase: SetSettingsUseCase
) : ViewModel() {

    private val _currentNode: MutableState<Node?> = mutableStateOf(null)
    val currentNode by _currentNode

    private val _currentNodeChild: MutableState<List<Node>> = mutableStateOf(emptyList())
    val currentNodeChild by _currentNodeChild

    private val _currentNodeParent: MutableState<Node?> = mutableStateOf(null)
    val currentNodeParent by _currentNodeParent

    private val _toastMessage: MutableState<String?> = mutableStateOf(null)
    val toastMessage by _toastMessage

    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.Node)
    val currentScreen by _currentScreen

    private val _settings: MutableState<Settings?> = mutableStateOf(null)
    val settings by _settings

    private val _colorScheme: MutableState<ColorScheme?> = mutableStateOf(null)
    val colorScheme by _colorScheme

    init {
        getSettings()
        initializeRootNode()
    }

    private fun initializeRootNode() {
        getNodeByIdUseCase(1).onEach {
            when (it) {
                is State.Success -> {
                    _currentNode.value = it.data!!
                    updateParent(it.data.parent)
                    updateChild(it.data.child)
                }

                else -> {
                    _toastMessage.value = it.message
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun updateParent(id: Long) {
        getNodeByIdUseCase(id).onEach {
            when (it) {
                is State.Success -> {
                    _currentNodeParent.value = it.data
                }

                else -> {
                    _toastMessage.value = it.message
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun updateChild(idList: List<Long>) {
        _currentNodeChild.value = emptyList()
        repeat(idList.size) { index ->
            getNodeByIdUseCase(idList[index]).onEach { state ->
                when (state) {
                    is State.Success -> {
                        _currentNodeChild.value = listOf(state.data!!) + _currentNodeChild.value
                        _currentNodeChild.value.sortedBy { it.id }
                    }

                    else -> {
                        _toastMessage.value = state.message
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun editShownNode() {
        setNodeUseCase(currentNode!!.copy(child = _currentNodeChild.value.map { it.id })).onEach {
            when (it) {
                is State.Success -> {
                    updateShownNode(it.data ?: currentNode!!)
                }

                else -> {
                    _toastMessage.value = it.message
                }
            }
        }.launchIn(viewModelScope)
    }

    fun createNode(description: String) {
        currentNode?.let { parent ->
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
                    parent.parent.toString(),
                    description
                ),
                40
            )

            setNodeUseCase(node.copy(label = hashLabel)).onEach {
                when (it) {
                    is State.Success -> {
                        _currentNodeChild.value = listOf(it.data!!) + _currentNodeChild.value
                        editShownNode()
                    }

                    else -> {
                        _toastMessage.value = it.message
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun updateCurrentScreen(screen: Screen) {
        _currentScreen.value = screen
    }

    fun updateShownNode(node: Node) {
        _currentNode.value = node
        updateChild(node.child)
        updateParent(node.parent)
    }


    fun deleteChildNodeBranch(node: Node) {

        deleteNodeUseCase(node.id).launchIn(viewModelScope)
        if (node.parent == currentNode!!.id) {
            editShownNode()
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

    fun removeChild(childId: Long) {
        _currentNodeChild.value = _currentNodeChild.value.filter { it.id != childId }
    }

    fun getSettings() {
        getSettingsUseCase().onEach { state ->
            when (state) {
                is State.Success -> {
                    _settings.value = state.data!!
                    _colorScheme.value = settings!!.getColorScheme()
                    getNodeByIdUseCase(settings!!.lastNodeId).onEach {
                        when (it) {
                            is State.Success -> {
                                _currentNode.value = it.data!!
                            }

                            else -> {
                                _toastMessage.value = state.message
                            }
                        }
                    }.launchIn(viewModelScope)
                }

                else -> {
                    _toastMessage.value = state.message
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setColorScheme(colorScheme: ColorScheme) {
        _colorScheme.value = colorScheme
    }

    fun changeSettings(settings: Settings) {
        _settings.value = settings
        setSettingsUseCase(settings).launchIn(viewModelScope)
    }
}