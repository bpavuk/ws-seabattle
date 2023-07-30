package com.bpavuk.wsSeabattle.core.types

class Room(
    val id: Int = autoId,
    val isOpen: Boolean = true
) {
    companion object {
        var autoId = 0
            get() = field++
    }

    val table = Table()
    val opponents = mutableListOf<Int /* UserID */>()

    fun addOpponent(userId: Int) {
        opponents.add(userId)
    }
}
