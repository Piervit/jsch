pipeline {
    agent { dockerfile true }
    stages {
        stage('Test') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    retry(2) {
                        sh 'ls'
                    }
                }
            }
        }
    }
}
