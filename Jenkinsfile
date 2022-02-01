pipeline {
    agent { 
        dockerfile {
            args '-u 1000:1000'
        }
    }
    stages {
        stage('Test') {
            steps {
              sh "echo 'Simple file content to transfert' > test.txt; java -jar target/testjsch-*-with-dependencies.jar test.txt sshuser@127.0.0.1:/home/sshuser/test.txt pass; cat /home/sshuser/test.txt"
              sh "rm -rf test.txt"
            }
        }
    }
}
