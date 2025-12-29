pipeline {
    agent any
triggers {
        cron('35 10 * * 1-5')
    }
    tools {
        // Это имя 'allure' должно совпадать с именем в Global Tool Configuration
        allure 'allure'
    }

    stages {
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t systeme-qa-test .'
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // 1. Удаляем старый контейнер, если он остался после сбоя
                    sh 'docker rm -f temp-results || true'

                    // 2. Запускаем тесты
                    sh 'docker run --name temp-results systeme-qa-test || true'

                    // 3. Копируем результаты из ПРАВИЛЬНОГО пути (в Maven это target/allure-results)
                    // Добавлена точка в конце, чтобы скопировать содержимое
                    sh 'docker cp temp-results:/app/target/allure-results ./'

                    // 4. Чистим за собой
                    sh 'docker rm -f temp-results'
                }
            }
        }
    }

    post {
        always {
            // Генерируем красивый отчет
            allure includeProperties: false, results: [[path: 'allure-results']]
            echo 'Allure report generated.'
        }
    }
}