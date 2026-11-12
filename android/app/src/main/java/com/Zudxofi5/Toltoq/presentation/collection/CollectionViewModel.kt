package com.Zudxofi5.Toltoq.presentation.collection

import androidx.lifecycle.ViewModel
import com.Zudxofi5.Toltoq.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CollectionViewModel(private val progress: ProgressRepository) : ViewModel() {

    private val _state = MutableStateFlow(CollectionUiState())
    val state: StateFlow<CollectionUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val career = progress.career()
        _state.value = CollectionUiState(
            costumes = progress.costumes(),
            pieces = career.pieces,
            maxPieces = career.maxPieces
        )
    }

    fun equip(costumeId: Int) {
        progress.equipCostume(costumeId)
        refresh()
    }
}
