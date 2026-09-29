package com.witokclone.core

/** Kullanıcının sanal cüzdanı. */
data class Wallet(
    var coins: Long = 0,
    var lastDailyBonusDay: Long = -1,
) {
    fun canAfford(price: Long) = coins >= price

    fun spend(amount: Long): Boolean {
        if (!canAfford(amount)) return false
        coins -= amount
        return true
    }

    fun earn(amount: Long) {
        require(amount >= 0)
        coins += amount
    }

    /** Gün başına bir kez 500 jeton bonus. */
    fun claimDailyBonus(dayEpoch: Long): Boolean {
        if (dayEpoch <= lastDailyBonusDay) return false
        lastDailyBonusDay = dayEpoch
        earn(DAILY_BONUS)
        return true
    }

    companion object {
        const val DAILY_BONUS = 500L
    }
}
