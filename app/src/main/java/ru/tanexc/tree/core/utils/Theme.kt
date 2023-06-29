package ru.tanexc.tree.core.utils

sealed class Theme(val id: Int) {
    class Default: Theme(id=0)
    class Orange: Theme(id=1)
    class Purple: Theme(id=2)

    companion object {
        fun getScheme(id: Int)
                = when (id) {
            0 -> Default()
            1 -> Orange()
            2 -> Purple()
            else -> Default()
        }
    }
}