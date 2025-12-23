pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Получение кода из репозитория
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Собираем образ, используя ваш Dockerfile
                    sh 'docker build -t systeme-qa-test .'
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Запускаем тесты и пробрасываем результаты наружу (если нужно)
                    sh 'docker run --rm systeme-qa-test'
                }
            }
        }
    }

    post {
        always {
            echo 'Тестирование завершено.'
            // Здесь можно добавить шаги для публикации Allure отчетов
        }
        success {
            echo 'Пайплайн успешно пройден!'
        }
        failure {
            echo 'Тесты упали или произошла ошибка сборки.'
        }
    }
}