pipeline {
    agent any
triggers {
        cron('H 13 * * 1-5')
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
                    // 1. Запускаем тесты (используем || true, чтобы пайплайн не падал до генерации отчета, если тест упадет)
                    sh 'docker run --name temp-results systeme-qa-test || true'

                    // 2. Копируем результаты из контейнера в рабочую папку Jenkins
                    sh 'docker cp temp-results:/app/allure-results ./'

                    // 3. Удаляем временный контейнер
                    sh 'docker rm temp-results'
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