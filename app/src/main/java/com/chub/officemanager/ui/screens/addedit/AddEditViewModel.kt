package com.chub.officemanager.ui.screens.addedit

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chub.officemanager.NavArgs
import com.chub.officemanager.domain.OfficeItem
import com.chub.officemanager.domain.OfficeItem.Companion.NONE
import com.chub.officemanager.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val currentItemId = savedStateHandle.getStateFlow(NavArgs.ITEM_ID, NONE)
    private val name = MutableStateFlow("")
    private val description = MutableStateFlow("")
    private val type = MutableStateFlow("")
    private val relations = MutableStateFlow<MutableList<OfficeItem>>(mutableListOf())

    init {
        currentItemId.value.let { itemId ->
            if (itemId != NONE) {
                name.value = "45"
                description.value = "The best desk I ever had"
                type.value = "Desk"
                relations.value = mutableListOf(
                    OfficeItem(55, "zx spectrum", "The best computer I ever had", "Computer"),
                    OfficeItem(44, "Chair", "The best chair I ever had", "Chair")
                )
            }
        }
    }

    val uiState: StateFlow<Result<ItemUiState>> =
        combine(currentItemId, name, description, type, relations) { itemId, name, description, type, relations ->
            Result.Success(
                if (itemId == NONE) {
                    ItemUiState()
                } else {
                    ItemUiState(
                        name = name,
                        description = description,
                        type = type,
                        relations = relations
                    )
                }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(500L),
            initialValue = Result.Loading
        )

    fun onNameChanged(name: String) {
        this.name.value = name
    }

    fun onDescriptionChanged(description: String) {
        this.description.value = description
    }

    fun onTypeChanged(type: String) {
        this.type.value = type
    }

    fun onActionClick(officeItem: OfficeItem) {
        relations.value = relations.value.apply {
            remove(officeItem)
        }
    }

}

data class ItemUiState(
    val name: String = "",
    val description: String = "",
    val type: String = "",
    val relations: List<OfficeItem> = emptyList()
)