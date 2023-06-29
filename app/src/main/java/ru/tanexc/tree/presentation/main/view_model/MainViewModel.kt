package ru.tanexc.tree.presentation.main.view_model

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.tanexc.tree.core.utils.Screen
import ru.tanexc.tree.core.utils.State
import ru.tanexc.tree.domain.model.Node
import ru.tanexc.tree.domain.model.Settings
import ru.tanexc.tree.domain.use_case.node_use_cases.GetNodeByIdUseCase
import ru.tanexc.tree.domain.use_case.settings_use_cases.GetSettingsUseCase
import ru.tanexc.tree.domain.use_case.settings_use_cases.SetSettingsUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNodeByIdUseCase: GetNodeByIdUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val setSettingsUseCase: SetSettingsUseCase
) : ViewModel() {

    private val _currentNode: MutableState<Node?> = mutableStateOf(null)
    val currentNode by _currentNode

    var currentNodeChild: MutableList<Node> = mutableListOf()

    private val _currentNodeParent: MutableState<Node?> = mutableStateOf(null)
    val currentNodeParent by _currentNodeParent

    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.Node)
    val currentScreen by _currentScreen

    private val _settings: MutableState<Settings?> = mutableStateOf(null)
    val settings by _settings

    private val _colorScheme: MutableState<ColorScheme?> = mutableStateOf(null)
    val colorScheme by _colorScheme

    init {
        getSettings()
        initializeNode(1)
    }

    fun initializeNode(id: Long) {
        getNodeByIdUseCase(id).onEach { state ->
            when (state) {
                is State.Success -> {
                    _currentNode.value = state.data!!
                    updateParent(state.data.parent)
                    updateChildId(state.data.child)
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun updateParent(id: Long) {
        getNodeByIdUseCase(id).onEach { state ->
            when (state) {
                is State.Success -> {
                    _currentNodeParent.value = state.data
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun updateChildId(idList: List<Long>) {
        currentNodeChild = currentNodeChild.filter { it.id in idList } as MutableList<Node>
        getChild(idList)
    }

    fun updateChild(nodeList: List<Node>) {
        currentNodeChild = nodeList as MutableList<Node>
    }

    private fun getChild(idList: List<Long>) {
        repeat(idList.size) { index ->
            getNodeByIdUseCase(idList[index]).onEach { state ->
                when (state) {
                    is State.Success -> {
                        if (!currentNodeChild.contains(state.data!!)) {
                            currentNodeChild = (listOf(state.data) + currentNodeChild) as MutableList<Node>
                            currentNodeChild = currentNodeChild.sortedBy { it.id } as MutableList<Node>
                        }
                    }

                    else -> {}
                }
            }.launchIn(viewModelScope)
        }
    }

    fun updateCurrentScreen(screen: Screen) {
        _currentScreen.value = screen
    }

    fun updateCurrentNode(node: Node) {
        changeSettings(settings!!.copy(lastNodeId = node.id))
        _currentNode.value = node
        updateChildId(node.child)
        updateParent(node.parent)
    }

    private fun getSettings() {
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

                            else -> {}
                        }
                    }.launchIn(viewModelScope)
                }

                else -> {}
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