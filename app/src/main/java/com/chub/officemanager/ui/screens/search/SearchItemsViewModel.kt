package com.chub.officemanager.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chub.officemanager.data.repo.OfficeItemRepository
import com.chub.officemanager.util.OfficeItem
import com.chub.officemanager.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchItemsViewModel @Inject constructor(private val officeRepo: OfficeItemRepository) : ViewModel() {

    private val filter = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val uiState: StateFlow<Result<SearchItemsUiState>> = combine(filter, filter.debounce(350L)
        .flatMapLatest { officeRepo.search(it) }
    ) { filter, items ->
        Result.Success(SearchItemsUiState(filter, items))
    }.flowOn(Dispatchers.IO).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500L),
        initialValue = Result.Loading
    )

    fun onAction(action: SearchItemsScreenAction.StateChange) {
        when (action) {
            is SearchItemsScreenAction.StateChange.FilterChanged -> onFilterChanged(action.filter)
            is SearchItemsScreenAction.StateChange.ItemRemove -> onItemRemove(action.item)
        }
    }

    private fun onFilterChanged(filter: String) {
        this.filter.value = filter
    }

    private fun onItemRemove(officeItem: OfficeItem) {
        viewModelScope.launch(Dispatchers.IO) {
            officeRepo.removeItem(officeItem)
        }
    }
}

data class SearchItemsUiState(
    val filter: String,
    val items: List<OfficeItem> = emptyList()
)