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
                sh './mvnw clean install'
            }
        }
        stage('test') {
            steps {
                sh './mvnw test'
            }
        }
        stage('Build Docker Image'){
            steps{
                def dockerfile = "./Dockerfile"
                def imageTag = "puscerdas:1.0.0"

                 sh "docker build -t ${imageTag} -f ${dockerfile} ."
            }
        }
        stage('Run Docker Container') {
            steps {
                script {
                    // Define the name for the Docker container
                    def containerName = "puscerdasKu"

                    // Run the Docker container with port 2808 exposed
                    sh "docker run -d --name ${containerName} -p 2808:2808 ${imageTag}"
                }
            }
        }
    }
}
