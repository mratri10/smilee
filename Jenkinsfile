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
                    // Copy files into workspace directory
                    sh 'sudo -S cp -r /var/lib/jenkins/workspace/puscerdas_atri/target/puscerdas-0.0.1-SNAPSHOT.jar /var/java'
                    sh 'atri2808'
                    // Build Docker image
                    docker.build('puscerdas-image:latest', '.')
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
