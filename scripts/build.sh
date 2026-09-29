#!/usr/bin/env bash
# Çekirdek iş mantığını Android SDK olmadan JVM'de derleyip test eder.
# Gerekler: JDK 17+, internet (kotlin-compiler ve junit jarları ilk çalıştırmada indirilir).
set -euo pipefail
cd "$(dirname "$0")/.."

TOOLS=build/tools
LIBS=build/libs
mkdir -p "$TOOLS" "$LIBS" build/classes build/testclasses

KOTLIN_VERSION=1.9.24
JUNIT_VERSION=4.13.2
HAMCREST_VERSION=1.3

fetch() {
  local url="$1" dest="$2"
  [ -f "$dest" ] || curl -fsSL -o "$dest" "$url"
}

echo "==> Kotlin compiler alınıyor..."
fetch "https://github.com/JetBrains/kotlin/releases/download/v${KOTLIN_VERSION}/kotlin-compiler-${KOTLIN_VERSION}.zip" "$TOOLS/kotlinc.zip"
[ -d "$TOOLS/kotlinc" ] || python3 -c "import zipfile;zipfile.ZipFile('$TOOLS/kotlinc.zip').extractall('$TOOLS')"
chmod +x "$TOOLS/kotlinc/bin/"* 2>/dev/null || true

echo "==> Test bağımlılıkları alınıyor..."
fetch "https://repo1.maven.org/maven2/junit/junit/${JUNIT_VERSION}/junit-${JUNIT_VERSION}.jar" "$LIBS/junit.jar"
fetch "https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/${HAMCREST_VERSION}/hamcrest-core-${HAMCREST_VERSION}.jar" "$LIBS/hamcrest.jar"

KOTLINC="$TOOLS/kotlinc/bin/kotlinc"

echo "==> Çekirdek kod derleniyor..."
"$KOTLINC" app/src/main/kotlin/com/witokclone/core/*.kt -d build/classes

echo "==> Testler derleniyor..."
CP="build/classes:$LIBS/junit.jar:$LIBS/hamcrest.jar"
"$KOTLINC" -cp "$CP" app/src/test/kotlin/com/witokclone/core/*.kt -d build/testclasses

echo "==> Testler çalıştırılıyor..."
java -cp "$CP:build/testclasses:$TOOLS/kotlinc/lib/kotlin-stdlib.jar" \
  org.junit.runner.JUnitCore com.witokclone.core.CoreLogicTest | tee build/core-tests.txt

echo "✅ Bitti."
