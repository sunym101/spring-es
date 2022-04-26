pipeline {
    agent any
	environment {
        CODE_REPOSITORY = 'https://github.com/sunym101/spring-es.git'
		CODE_BRANCH = 'master'
    }
    stages {
        stage('1.拉取代码') {
            steps {
                git credentialsId:'github-sunym', url: "${CODE_REPOSITORY}", branch:"${CODE_BRANCH}"
            }
        }
        stage('2.编译构建') {
            steps {
				echo '执行Windows环境下Maven构建'
				bat 'mvn -Dmaven.test.skip=true clean package'
			}
        }
        stage('3.构建发布') {
             steps{
                echo '执行Windows环境下Maven构建后发布'
             }
        }
    }
}
