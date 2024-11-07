package com.pxy.visaz.ui

sealed class ListItem {
    data class HEADER(val text: String) : ListItem()
    data class CONTENT(val text: String) : ListItem()
}
