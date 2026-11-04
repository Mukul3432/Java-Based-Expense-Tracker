#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
[ -d out ] || ./build.sh
java -cp out com.expensetracker.Main "$@"
