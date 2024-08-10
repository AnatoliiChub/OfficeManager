package com.chub.officemanager.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chub.officemanager.domain.OfficeItem
import com.chub.officemanager.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchItemsViewModel @Inject constructor() : ViewModel() {

    val items = listOf(
        OfficeItem(1, "35", "A desk for working", "Desk"),
        OfficeItem(2, "32", "A chair for sitting", "Chair"),
        OfficeItem(3, "10w LED", "A lamp for lighting", "Lamp"),
        OfficeItem(4, "Dell 24HL", "A monitor for viewing", "Monitor"),
        OfficeItem(5, "Logitech 121", "A keyboard for typing", "Keyboard"),
        OfficeItem(6, "Logitech 121", "A mouse for clicking", "Mouse"),
        OfficeItem(7, "Max", "A person for working", "Employee"),
        OfficeItem(8, "Dexter", "A person for working", "Employee"),
        OfficeItem(9, "John", "A person for working", "Employee"),
    )

    private val filter = MutableStateFlow("")


    val uiState: StateFlow<Result<SearchItemsUiState>> = filter.map { filter ->
        items.filter {
            it.name.contains(filter, ignoreCase = true)
                    || it.description.contains(filter, ignoreCase = true)
                    || it.type.contains(filter, ignoreCase = true)
        }
    }.map { Result.Success(SearchItemsUiState(filter.value, it)) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500L),
        initialValue = Result.Loading
    )

    fun onFilterChanged(filter: String) {
        this.filter.value = filter
    }
}


data class SearchItemsUiState(
    val filter: String = "",
    val items: List<OfficeItem> = emptyList()
)