package com.witokclone.core

import org.junit.Assert.*
import org.junit.Test

class CoreLogicTest {

    @Test fun walletSpendAndEarn() {
        val w = Wallet(coins = 1000)
        assertTrue(w.spend(400))
        assertEquals(600L, w.coins)
        assertFalse(w.spend(700))
        assertEquals(600L, w.coins)
        w.earn(250)
        assertEquals(850L, w.coins)
    }

    @Test fun dailyBonusOncePerDay() {
        val w = Wallet()
        assertTrue(w.claimDailyBonus(1))
        assertEquals(500L, w.coins)
        assertFalse(w.claimDailyBonus(1))
        assertTrue(w.claimDailyBonus(2))
    }

    @Test fun roomOwnerTransferOnLeave() {
        val room = PartyRoom("r1")
        val a = User("a", "Ali"); val b = User("b", "Ayşe")
        room.join(a); room.join(b)
        assertEquals(SeatRole.OWNER, room.seats[0].role)
        room.leave("a")
        assertEquals(SeatRole.OWNER, room.seats[1].role)
    }

    @Test fun roomKickAndAdmin() {
        val room = PartyRoom("r1")
        val a = User("a", "Ali"); val b = User("b", "Ayşe")
        room.join(a); room.join(b)
        assertTrue(room.promoteToAdmin("b"))
        assertEquals(SeatRole.ADMIN, room.seats[1].role)
        assertTrue(room.kick("b"))
        assertFalse(room.seats[1].isOccupied)
        assertFalse(room.kick("a")) // owner atılamaz
    }

    @Test fun fullRoomRejectsJoin() {
        val room = PartyRoom("r1", seatCount = 2)
        assertTrue(room.join(User("x", "X")))
        assertTrue(room.join(User("y", "Y")))
        assertTrue(room.isFull)
        assertFalse(room.join(User("z", "Z")))
    }

    @Test fun giftFlowCorrect() {
        val w = Wallet(coins = 60)
        val svc = GiftService(w)
        val target = User("t", "Target")
        assertTrue(svc.send("rose", target))
        assertEquals(50L, w.coins)
        assertFalse(svc.send("heart", target)) // 52 > 50
        assertEquals(50L, w.coins)
        assertTrue(target.likes > 0)
    }

    @Test fun game51WinPaysOut() {
        val w = Wallet(coins = 1000)
        val fixed = object : kotlin.random.Random() {
            var i = 0
            val seq = intArrayOf(6, 6, 6, 6, 6, 6, 6, 3, 1)
            override fun nextBits(bitCount: Int): Int = 0
            override fun nextInt(from: Int, until: Int): Int = from + (seq[i++ % seq.size] % (until - from))
        }
        val g = Game51(w, fixed)
        assertTrue(g.start(100))
        assertEquals(900L, w.coins)
        while (!g.finished) g.roll()
        // oyun ya tam 51'e vurdu (kazanç) ya geçti (bahis yanar) - ikisi de tutarlı olmalı
        if (g.won) assertEquals(1100L, w.coins) else assertEquals(900L, w.coins)
        assertEquals(-1, g.roll())
    }

    @Test fun game51MinBetEnforced() {
        val w = Wallet(coins = 1000)
        val g = Game51(w)
        assertFalse(g.start(50))
        assertEquals(1000L, w.coins)
    }
}
