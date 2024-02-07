pipeline {
    agent any
    stages {
        stage('Init') {
                steps {
                    chmod -R 777 mvnw
                }
            }
        stage('clean') {
            steps {
                ./mvnw clean
            }
        }
        stage('test') {
            steps {
                ./mvnw compile test-compile,
                ./mvnw test
            }
        }
        stage('Build') {
            steps {
                ./mvnw clean package
            }
        }
        stage('Run') {
            steps {
                sh 'java -jar /target/puscerdas-0.0.1-SNAPSHOT.jar'
            }
        }
    }
}
