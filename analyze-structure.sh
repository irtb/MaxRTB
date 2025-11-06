#!/bin/bash

echo "=== CSJ Structure ===" && find csj/src/main/java -type f | sort
echo "=== GDT Structure ===" && find gdt/src/main/java -type f | sort
echo "=== KS Structure ===" && find ks/src/main/java -type f | sort
echo "=== MG Structure ===" && find mg/src/main/java -type f | sort
echo "=== BAIDU Structure ===" && find baidu/src/main/java -type f | sort
echo "=== CORE Structure ===" && find core/src/main/java -type f | sort
echo "=== ZX Structure ===" && find zx/src/main/java -type f 2>/dev/null || echo "zx not implemented yet"

echo -e "\n=== Core Build Config ===" && cat core/build.gradle
echo -e "\n=== CSJ Build Config ===" && cat csj/build.gradle
