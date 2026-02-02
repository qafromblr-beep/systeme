# 1. Используем легкий образ только с JDK (Alpine версия экономит место)
FROM eclipse-temurin:21-jdk-alpine

# 2. Устанавливаем Chrome и ChromeDriver
RUN apk add --no-cache chromium chromium-chromedriver

# 3. Настраиваем переменные окружения
ENV CHROME_BIN=/usr/bin/chromium-browser \
    CHROME_PATH=/usr/lib/chromium/

WORKDIR /app

# 4. Сначала копируем только файлы обертки и pom.xml
# Это позволяет Docker кэшировать зависимости и не качать их заново при изменении кода
COPY .mvn .mvn
COPY mvnw pom.xml ./

# Даем права на выполнение скрипту (важно для Linux-сред)
RUN chmod +x mvnw

# Скачиваем зависимости (опционально, ускоряет последующие сборки)
RUN ./mvnw dependency:go-offline

# 5. Копируем исходный код
COPY src ./src

# 6. Запускаем через ./mvnw вместо системного mvn
CMD ["./mvnw", "clean", "test"]
