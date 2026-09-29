package com.witokclone.core

import kotlin.random.Random

/**
 * Basitleştirilmiş "51" mini oyunu: her turda zarlar ilerler,
 * 51 kareye ilk ulaşan kazanır. Bahis, kazananın ödül havuzundan ödenir.
 */
class Game51(private val wallet: Wallet, private val random: Random = Random.Default) {
    var position = 0; private set
    var bet = 0L; private set
    var finished = false; private set
    var won = false; private set

    fun start(bet: Long): Boolean {
        if (bet < MIN_BET || !wallet.spend(bet)) return false
        this.bet = bet
        position = 0
        finished = false
        won = false
        return true
    }

    /** @return atılan toplam zar değeri. Oyun bittiyse -1. */
    fun roll(): Int {
        if (finished || bet == 0L) return -1
        val dice = random.nextInt(1, 7) + random.nextInt(1, 7)
        position += dice
        when {
            position > GOAL -> { // geçtikse bahis yanar
                finished = true
                won = false
                bet = 0
            }
            position == GOAL -> {
                finished = true
                won = true
                wallet.earn(bet * PAYOUT_MULTIPLIER)
            }
        }
        return dice
    }

    companion object {
        const val GOAL = 51
        const val MIN_BET = 100L
        const val PAYOUT_MULTIPLIER = 2
    }
}
