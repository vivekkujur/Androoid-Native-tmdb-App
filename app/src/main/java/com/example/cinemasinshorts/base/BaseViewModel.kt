package com.example.cinemasinshorts.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : Any, Event : Any> : ViewModel() {
    private val _state = MutableStateFlow<State?>(null)
    val state: StateFlow<State?> = _state.asStateFlow()

    private val _event = MutableStateFlow<Event?>(null)
    val event: StateFlow<Event?> = _event.asStateFlow()

    protected fun setState(state: State) {
        viewModelScope.launch {
            _state.emit(state)
        }
    }

    protected fun setEvent(event: Event) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }
} 