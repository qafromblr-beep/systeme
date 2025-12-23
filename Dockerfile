# Используем образ Maven с JDK 21 (актуально для 2025 года)
FROM maven:3.9.6-eclipse-temurin-21-alpine

# Устанавливаем Chrome и ChromeDriver для запуска тестов внутри контейнера
RUN apk add --no-cache chromium chromium-chromedriver

# Указываем путь к Chrome для Selenium
ENV CHROME_BIN=/usr/bin/chromium-browser
ENV CHROME_PATH=/usr/lib/chromium/

# Копируем проект в контейнер
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Запускаем тесты при старте контейнера
CMD ["mvn", "clean", "test"]