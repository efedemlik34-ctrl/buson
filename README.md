# Witok Clone (Açık Kaynak Demo)

"Witok - Sohbet, Parti & Oyun" tarzı bir sosyal ses/parti uygulamasının
**eğitim amaçlı, bağımsız (orijinal kodlu) açık kaynak klonudur.**

> ⚠️ Önemli: Bu proje, Witok'un ticari markasını, kodunu veya tasarımını
> içermez. Yalnızca genel konsepti (sesli sohbet odaları, hediye gönderme,
> mini oyunlar, jeton/coin sistemi) örnekleyen kendi yazılmış bir demodur.
> Gerçek zamanlı altyapı için bir WebSocket sunucusuna bağlamanız gerekir.

## Özellikler
- 🎙️ Sesli sohbet odaları (Parti Odaları) — rol sistemi: Owner / Admin / Mic / Boş koltuk
- 💰 Jeton (Coin) cüzdanı ve günlük bonus
- 🎁 Sanal hediye gönderme (efekt animasyonlu)
- 🎮 Mini oyun: 51 Jeton Yarışı (basitleştirilmiş)
- ❤️ Beğeni / popülerlik puanı
- 👤 Profil düzenleme

## Teknoloji
- Kotlin + Jetpack Compose (Material 3)
- MVVM mimarisi, saf fonksiyonlu iş mantığı (`core` paketi) → JVM'de test edilebilir

## Derleme
```bash
# İş mantığı testleri (SDK gerekmeden, JDK 17 ile):
./scripts/build.sh

# APK derlemek için Android SDK gereklidir:
gradle assembleDebug   # veya Android Studio ile açın
```

## GitHub Actions
`.github/workflows/android.yml` otomatik derleme + test çalıştırır.

## Lisans
MIT — Efedemlik © 2026
