pipeline {
    agent { dockerfile true }
    stages {
        stage('Test') {
            steps {
                dir('/usr/src/jsch/CITest'){
                  timeout(time: 2, unit: 'MINUTES') {
                    retry(2) {
                        sh 'ls'
                    }
                  }
                }
            }
        }
    }
}
