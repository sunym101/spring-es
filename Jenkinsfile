pipeline {
    agent any
    stages{
        stage('checkout source'){
            steps{
                echo 'checkout source'
                git 'https://github.com/sunym101/spring-es.git'
            }
        }

        stage('maven build'){
            steps{
                echo 'maven build'
            }
        }
    }

}