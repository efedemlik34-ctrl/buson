package com.witokclone.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.witokclone.core.*

/** "51" mini oyun ekranı. */
@Composable
fun Game51Screen(wallet: Wallet) {
    val game = remember { mutableStateOf<Game51?>(null) }
    val log = remember { mutableStateListOf<String>() }
    var betInput by remember { mutableStateOf("100") }

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("🎲 51 Jeton Yarışı", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        Text("Pozisyon: ${game.value?.position ?: 0} / ${Game51.GOAL}")

        LinearProgressIndicator(
            progress = { (game.value?.position ?: 0) / Game51.GOAL.toFloat() },
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        )

        val g = game.value
        if (g == null || g.finished) {
            OutlinedTextField(value = betInput, onValueChange = { betInput = it }, label = { Text("Bahis") })
            Button(
                onClick = {
                    val bet = betInput.toLongOrNull() ?: return@Button
                    val ng = Game51(wallet)
                    if (ng.start(bet)) {
                        game.value = ng
                        log.add("Bahis: $bet")
                    } else {
                        log.add("Yetersiz bakiye veya min bahis ${Game51.MIN_BET}")
                    }
                },
                modifier = Modifier.padding(top = 8.dp),
            ) { Text("Oyunu Başlat") }
        } else {
            Button(onClick = {
                val d = g.roll()
                if (d >= 0) log.add("Zar: $d → ${g.position}")
                if (g.finished) {
                    log.add(
                        if (g.won) "🏆 Kazandın! +${g.bet * Game51.PAYOUT_MULTIPLIER}"
                        else "💥 Bahsi geçtin, bahis yandı."
                    )
                }
            }) { Text("Zar At") }
        }

        Spacer(Modifier.height(12.dp))
        log.reversed().take(8).forEach { Text(it) }
        Text("Bakiye: 🪙 ${wallet.coins}", style = MaterialTheme.typography.titleMedium)
    }
}
