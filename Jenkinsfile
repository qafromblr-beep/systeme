pipeline {
    agent any
    triggers {
        cron('35 10 * * 1-5')
    }
    tools {
        allure 'allure'
    }

    stages {
        stage('Checkout & Permissions') {
            steps {
                // Гарантируем, что скрипт будет исполняемым перед сборкой образа
                sh 'chmod +x mvnw'
            }
        }

        stage('Build Docker Image') {
            steps {
                // Используем стандартный тег, чтобы не плодить мусор
                sh 'docker build -t systeme-qa-test .'
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Очистка контейнера перед запуском
                    sh 'docker rm -f temp-results || true'

                    // Запуск тестов. Ошибка теста не должна прерывать пайплайн,
                    // чтобы отчет Allure все равно сгенерировался.
                    sh 'docker run --name temp-results systeme-qa-test || true'

                    // Очищаем локальную папку перед копированием свежих результатов
                    sh 'rm -rf allure-results && mkdir allure-results'

                    // Копируем результаты из контейнера в рабочую директорию Jenkins
                    sh 'docker cp temp-results:/app/target/allure-results/. ./allure-results/'
                }
            }
        }
    }

    post {
        always {
            // Указываем путь к папке, в которую скопировали данные из контейнера
            allure includeProperties: false, results: [[path: 'allure-results']]

            // Удаляем контейнер после копирования данных
            sh 'docker rm -f temp-results || true'
        }
    }
}
