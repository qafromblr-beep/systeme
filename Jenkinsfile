pipeline {
    agent any
    stages {
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t systeme-qa-test .'
            }
        }
        stage('Run Tests') {
            steps {
                sh 'docker run --rm systeme-qa-test'
            }
        }
    }
    post {
        always {
            echo 'Тестирование завершено.'
        }
    }
}