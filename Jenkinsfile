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
                    sudo docker.build('puscerdas-image:latest', '.')
                }
                script{
                    sudo docker.withRegistry('http://192.168.1.103:2050', 'atri0205'){
                        sudo docker.image('puscerdas-image:latest').push()
                    }
                }
                script{
                    sudo docker.image('puscerdas-image:latest').run()
                }
            }
        }
    }
}
