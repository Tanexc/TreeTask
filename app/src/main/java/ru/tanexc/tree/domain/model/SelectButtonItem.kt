package ru.tanexc.tree.domain.model


data class SelectButtonItem(
    val title: String,
    val onSelected: () -> Unit
)