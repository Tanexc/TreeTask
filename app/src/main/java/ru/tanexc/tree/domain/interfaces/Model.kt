package ru.tanexc.tree.domain.interfaces

interface Model {
    fun asDatabaseEntity(): DatabaseEntity
}