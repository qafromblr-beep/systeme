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
                    // 1. Запускаем контейнер.
                    // Используем '|| true', чтобы Jenkins продолжил работу при падении тестов.
                    sh 'docker run --name temp-results systeme-qa-test || true'

                    // 2. Копируем результаты.
                    // ВАЖНО: Если в Dockerfile путь другой, исправьте /app/target/allure-results
                    sh 'docker cp temp-results:/app/target/allure-results ./'

                    // Если папка называется просто allure-results в корне контейнера:
                    // sh 'docker cp temp-results:/app/allure-results ./'

                    // 3. Удаляем контейнер
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