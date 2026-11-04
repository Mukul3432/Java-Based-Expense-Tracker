#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
./build.sh
rm -rf out-test && mkdir -p out-test
if command -v javac >/dev/null 2>&1; then JAVAC=(javac); else JAVAC=(java --module jdk.compiler/com.sun.tools.javac.Main); fi
"${JAVAC[@]}" -cp out -d out-test src/test/java/com/expensetracker/ExpenseServiceTest.java
java -cp out:out-test com.expensetracker.ExpenseServiceTest
