package com.witokclone.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.witokclone.core.*

/** Parti odası ekranı: koltuklar, sohbet, hediye ve jeton göstergesi. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyRoomScreen(room: PartyRoom, wallet: Wallet, me: User) {
    var msg by remember { mutableStateOf("") }
    var giftPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Parti Odası #${room.id}") },
                actions = {
                    Text(
                        "🪙 ${wallet.coins}",
                        modifier = Modifier.padding(end = 16.dp),
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
            )
        },
    ) { pad ->
        Column(Modifier.padding(pad).padding(12.dp)) {
            room.seats.chunked(4).forEach { rowSeats ->
                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    rowSeats.forEach { seat ->
                        Card(Modifier.width(80.dp).height(80.dp).padding(4.dp)) {
                            Column(
                                Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    seat.user?.avatarEmoji ?: "➕",
                                    style = MaterialTheme.typography.headlineMedium,
                                )
                                Text(seat.role.name, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider()

            LazyColumn(Modifier.weight(1f)) {
                items(room.messages) { Text(it) }
            }

            HorizontalDivider()
            Row(Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = msg,
                    onValueChange = { msg = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Mesaj yaz…") },
                )
                FilledIconButton(onClick = { room.sendMessage(msg); msg = "" }) { Text("📤") }
                FilledIconButton(onClick = { giftPicker = true }) { Text("🎁") }
            }
        }
    }

    if (giftPicker) {
        AlertDialog(
            onDismissRequest = { giftPicker = false },
            title = { Text("Hediye Gönder") },
            text = {
                Column {
                    GiftCatalog.gifts.forEach { g ->
                        TextButton(
                            onClick = {
                                room.occupants.firstOrNull { it.id != me.id }?.let {
                                    GiftService(wallet).send(g.id, it)
                                }
                                giftPicker = false
                            },
                        ) { Text("${g.emoji} ${g.name} — 🪙${g.price}") }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { giftPicker = false }) { Text("Kapat") } },
        )
    }
}
