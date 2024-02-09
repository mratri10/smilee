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
        stage('Deploy to Docker'){
            steps{
                script{
                    docker.build('puscerdas-image:latest', '.')
                }
                script{
                    docker.withRegistry('http://192.168.1.103:0205', 'atri0205'){
                        docker.image('puscerdas-image:latest').push()
                    }
                }
                script{
                    docker.image('puscerdas-image:latest').run()
                }
            }
        }
    }
}
