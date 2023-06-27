package ru.tanexc.tree.domain.interfaces

interface DatabaseEntity {
    fun asDomain(): Model
}