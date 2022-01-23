pipeline {
    agent { 
      dockerfile {
        args '-u root --privileged'
      }
    }
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
