package com.task.newsapp.model

sealed class NetworkStatusState {
    object NetworkStatusConnected : NetworkStatusState()
    object NetworkStatusDisconnected : NetworkStatusState()
}