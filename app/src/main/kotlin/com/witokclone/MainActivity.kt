package com.witokclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.witokclone.core.*
import com.witokclone.ui.Game51Screen
import com.witokclone.ui.PartyRoomScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val wallet = remember { Wallet(coins = 5000) }
                val me = remember { User("me", "Ben", "😎") }
                val room = remember {
                    PartyRoom("42").apply {
                        join(me)
                        join(User("bot1", "Ayşe", "👩"))
                        sendMessage("Hoş geldin partiye!")
                    }
                }
                var tab by remember { mutableStateOf(0) }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = tab == 0,
                                onClick = { tab = 0 },
                                icon = { Text("🎙️") },
                                label = { Text("Oda") },
                            )
                            NavigationBarItem(
                                selected = tab == 1,
                                onClick = { tab = 1 },
                                icon = { Text("🎲") },
                                label = { Text("51 Oyunu") },
                            )
                            NavigationBarItem(
                                selected = tab == 2,
                                onClick = { tab = 2 },
                                icon = { Text("👤") },
                                label = { Text("Profil") },
                            )
                        }
                    },
                ) { pad ->
                    when (tab) {
                        0 -> Box(Modifier.padding(pad)) { PartyRoomScreen(room, wallet, me) }
                        1 -> Box(Modifier.padding(pad)) { Game51Screen(wallet) }
                        else -> ProfileScreen(wallet, me)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileScreen(wallet: Wallet, me: User) {
    Column(Modifier.padding(24.dp)) {
        Text("${me.avatarEmoji} ${me.name}", style = MaterialTheme.typography.headlineSmall)
        Text("❤️ Beğeni: ${me.likes}")
        Text("🪙 Jeton: ${wallet.coins}")
        Spacer(Modifier.height(16.dp))
        Button(onClick = { wallet.claimDailyBonus(System.currentTimeMillis() / 86_400_000L) }) {
            Text("Günlük Bonus (+${Wallet.DAILY_BONUS})")
        }
    }
}
