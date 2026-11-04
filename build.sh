#!/usr/bin/env bash
# Compiles with plain javac (JDK 8+). No Maven needed.
set -e
cd "$(dirname "$0")"
rm -rf out && mkdir -p out
if command -v javac >/dev/null 2>&1; then JAVAC=(javac); else JAVAC=(java --module jdk.compiler/com.sun.tools.javac.Main); fi
"${JAVAC[@]}" -d out $(find src/main -name '*.java')
echo "Build OK -> out/"
