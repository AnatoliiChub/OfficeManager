package com.chub.officemanager.ui.screens.addedit

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chub.officemanager.NavArgs
import com.chub.officemanager.data.repo.OfficeItemRepository
import com.chub.officemanager.exceptioin.ItemDuplicatedException
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
            temporaryState.relations,
            temporaryState.error
        ) { name, description, type, relations, error ->
            if (error.isNotBlank()) {
                Result.Error(error)
            } else {
                Result.Success(
                    ItemUiState(
                        name = name,
                        description = description,
                        type = type,
                        relations = relations
                    )
                )
            }
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

    fun saveItem(onSaved: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val item = temporaryState.toOfficeItem()
                officeRepo.storeItem(item)
                onSaved()
            } catch (exception: Exception) {
                Log.e("AddEditViewModel", "saveItem: ", exception)
                when (exception) {
                    is ItemDuplicatedException -> temporaryState.error.value =
                        "You tried to add duplicated item or item which already related to another object"

                    else -> temporaryState.error.value = "Something went wrong"
                }
            }
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



