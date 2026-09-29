package com.witokclone.core

data class Gift(val id: String, val name: String, val emoji: String, val price: Long)

object GiftCatalog {
    val gifts = listOf(
        Gift("rose", "Gül", "🌹", 10),
        Gift("heart", "Kalp", "❤️", 52),
        Gift("cake", "Pasta", "🎂", 99),
        Gift("rocket", "Roket", "🚀", 500),
        Gift("crown", "Taç", "👑", 1000),
        Gift("castle", "Şato", "🏰", 5000),
    )

    fun byId(id: String) = gifts.firstOrNull { it.id == id }
}

/** Hediye gönderimi: bakiye düşer, alıcının popülerliği artar. */
class GiftService(private val wallet: Wallet) {
    val sentLog = mutableListOf<Pair<Gift, User>>()

    fun send(giftId: String, to: User): Boolean {
        val gift = GiftCatalog.byId(giftId) ?: return false
        if (!wallet.spend(gift.price)) return false
        to.likes += gift.name.length // basitleştirilmiş popülerlik etkisi
        sentLog += gift to to
        return true
    }
}
