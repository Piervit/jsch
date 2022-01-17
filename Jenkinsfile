pipeline {
    agent { dockerfile true }
    stages {
        stage('Test') {
            steps {
              sh 'java -jar target/testjsch-*-with-dependencies.jar test.txt sshuser@127.0.0.1:/home/sshuser/test.txt pass; cat /home/sshuser/test.txt'
            }
        }
    }
}
