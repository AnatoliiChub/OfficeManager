package com.chub.officemanager.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chub.officemanager.data.OfficeItemRepository
import com.chub.officemanager.domain.OfficeItem
import com.chub.officemanager.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchItemsViewModel @Inject constructor(private val officeRepo: OfficeItemRepository) : ViewModel() {

    private val filter = MutableStateFlow("")

    val uiState: StateFlow<Result<SearchItemsUiState>> = officeRepo.getAllItems()
        .map {
            Result.Success(SearchItemsUiState(filter.value, it))
        }.flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(500L),
            initialValue = Result.Loading
        )

    fun onFilterChanged(filter: String) {
        this.filter.value = filter
    }

    fun onItemRemove(officeItem: OfficeItem) {
        viewModelScope.launch(Dispatchers.IO) {
            officeRepo.removeItem(officeItem)
        }
    }
}


data class SearchItemsUiState(
    val filter: String = "",
    val items: List<OfficeItem> = emptyList()
)