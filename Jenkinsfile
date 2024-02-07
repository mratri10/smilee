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
        stage('compile') {
            steps {
                sh './mvnw compile test-compile'
            }
        }
        stage('test') {
            steps {
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
                sh 'nohup java -jar /var/lib/jenkins/workspace/puscerdas_atri/target/puscerdas-0.0.1-SNAPSHOT.jar'
            }
        }
    }
}
