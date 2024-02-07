pipeline {
    agent any
    stages {
        stage('Init') {
                steps {
                    sh 'chmod -R 777 mvnw'
                }
            }
        stage('clean') {
            steps {
                sh './mvnw clean'
            }
        }
        stage('test') {
            steps {
                sh './mvnw compile test-compile',
                sh './mvnw test'
            }
        }
        stage('Build') {
            steps {
                sh './mvnw clean package'
            }
        }
        stage('Run') {
            steps {
                sh 'java -jar /target/puscerdas-0.0.1-SNAPSHOT.jar'
            }
        }
    }
}