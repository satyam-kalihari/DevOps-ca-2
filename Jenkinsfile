pipeline {
    agent any

    options {
        timestamps()
    }

    environment {
        GIT_URL = 'https://github.com/satyam-kalihari/DevOps-ca-2/'
        GIT_BRANCH = 'main'
        MAVEN_CMD = 'C:\\Users\\satya\\tools\\apache-maven-3.9.11\\bin\\mvn.cmd'
    }

    stages {
        stage('Checkout from GitHub') {
            steps {
                git branch: "${GIT_BRANCH}", url: "${GIT_URL}"
            }
        }

        stage('Set up Java and Maven') {
            steps {
                bat 'java -version'
                bat 'if not exist "%MAVEN_CMD%" exit /b 1'
                bat '"%MAVEN_CMD%" -version'
            }
        }

        stage('Run Selenium Tests') {
            steps {
                bat '''
                    "%MAVEN_CMD%" -B clean test
                '''
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'target/surefire-reports/*.xml', fingerprint: true, allowEmptyArchive: true
        }
    }
}
