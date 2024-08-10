package com.chub.officemanager.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chub.officemanager.data.repo.OfficeItemRepository
import com.chub.officemanager.util.OfficeItem
import com.chub.officemanager.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchItemsViewModel @Inject constructor(private val officeRepo: OfficeItemRepository) : ViewModel() {

    val filter = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<Result<SearchItemsUiState>> = filter.flatMapLatest { officeRepo.search(it) }
        .map { Result.Success(SearchItemsUiState(it)) }.flowOn(Dispatchers.IO)
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
    val items: List<OfficeItem> = emptyList()
)