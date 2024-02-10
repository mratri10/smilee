pipeline {
    agent any
    environment {
        DOCKER_USER = 'your_docker_user' // Specify your Docker user
    }
    stages {
        stage('Init') {
            steps {
                sh 'chmod -R 777 mvnw'
            }
        }
        stage('Clean') {
            steps {
                sh './mvnw clean install'
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }
        stage('Deploy to Docker') {
            steps {
                script {
                    docker.build('puscerdas-image:latest', '.').withRun('-u root') // Specify Docker user as root
                }
                script {
                    docker.withRegistry('http://192.168.1.103:2050', 'atri0205') {
                        docker.image('puscerdas-image:latest').push()
                    }
                }
                script {
                    docker.image('puscerdas-image:latest').run('-u root') // Specify Docker user as root
                }
            }
        }
    }
}
