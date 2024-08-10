package com.chub.officemanager.ui.screens.addedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chub.officemanager.NavArgs
import com.chub.officemanager.data.OfficeItemRepository
import com.chub.officemanager.util.OfficeItem
import com.chub.officemanager.util.OfficeItem.Companion.NONE
import com.chub.officemanager.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val officeRepo: OfficeItemRepository
) :
    ViewModel() {

    private val temporaryState = TemporaryItemState()

    init {
        with(temporaryState) {
            currentItemId.value = savedStateHandle.get<Long>(NavArgs.ITEM_ID) ?: NONE
            if (currentItemId.value != NONE) {
                fetchItem()
            }
        }
    }

    val uiState: StateFlow<Result<ItemUiState>> =
        combine(
            temporaryState.name,
            temporaryState.description,
            temporaryState.type,
            temporaryState.relations
        ) { name, description, type, relations ->
            Result.Success(
                ItemUiState(
                    name = name,
                    description = description,
                    type = type,
                    relations = relations
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(500L),
            initialValue = Result.Loading
        )

    fun onNameChanged(name: String) {
        temporaryState.name.value = name
    }

    fun onDescriptionChanged(description: String) {
        temporaryState.description.value = description
    }

    fun onTypeChanged(type: String) {
        temporaryState.type.value = type
    }

    fun onRemoveClick(officeItem: OfficeItem) {
        temporaryState.removeRelation(officeItem)
    }

    fun saveItem() {
        viewModelScope.launch(Dispatchers.IO) {
            val item = temporaryState.toOfficeItem()
            officeRepo.storeItem(item)
        }
    }

    fun onRelationSelected(selectedRelation: OfficeItem) {
        temporaryState.addRelations(selectedRelation)
    }

    private fun fetchItem() {
        viewModelScope.launch(Dispatchers.IO) {
            officeRepo.getItemById(temporaryState.currentItemId.value).let { item ->
                temporaryState.update(item)
            }
        }
    }
}



