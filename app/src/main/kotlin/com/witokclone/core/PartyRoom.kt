package com.witokclone.core

/** Parti odasındaki koltuk rolleri. */
enum class SeatRole { EMPTY, OWNER, ADMIN, MEMBER }

data class User(
    val id: String,
    val name: String,
    val avatarEmoji: String = "🙂",
    var likes: Int = 0,
)

data class Seat(val index: Int, var user: User? = null, var role: SeatRole = SeatRole.EMPTY) {
    val isOccupied get() = user != null
}

/** 8 kişilik sesli parti odası. */
class PartyRoom(val id: String, seatCount: Int = DEFAULT_SEATS) {
    val seats: List<Seat> = List(seatCount) { Seat(it) }
    val messages = mutableListOf<String>()

    val occupants: List<User> get() = seats.mapNotNull { it.user }
    val isFull: Boolean get() = seats.all { it.isOccupied }

    fun join(user: User): Boolean {
        val seat = seats.firstOrNull { !it.isOccupied } ?: return false
        seat.user = user
        if (seats.none { it.role == SeatRole.OWNER }) seat.role = SeatRole.OWNER
        return true
    }

    fun leave(userId: String) {
        val seat = seats.firstOrNull { it.user?.id == userId } ?: return
        val wasOwner = seat.role == SeatRole.OWNER
        seat.user = null
        seat.role = SeatRole.EMPTY
        if (wasOwner) seats.firstOrNull { it.isOccupied }?.role = SeatRole.OWNER
    }

    fun promoteToAdmin(userId: String): Boolean {
        val seat = seats.firstOrNull { it.user?.id == userId } ?: return false
        if (seat.role == SeatRole.OWNER) return false
        seat.role = SeatRole.ADMIN
        return true
    }

    fun kick(userId: String): Boolean {
        val seat = seats.firstOrNull { it.user?.id == userId } ?: return false
        if (seat.role == SeatRole.OWNER) return false
        leave(userId)
        return true
    }

    fun sendMessage(text: String) {
        if (text.isNotBlank()) messages += text.trim()
    }

    companion object {
        const val DEFAULT_SEATS = 8
    }
}
