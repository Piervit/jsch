pipeline {
    agent { 
        dockerfile {
                args '-u root'
        }
    }
    stages {
        stage('Test') {
            steps {
                sh "service ssh start ; service ssh status" 
                sh "cd /usr/src/jsch/CITest; echo 'Simple file content to transfert' > test.txt; java -jar target/testjsch-*-with-dependencies.jar test.txt sshuser@127.0.0.1:/home/sshuser/test.txt pass; cat /home/sshuser/test.txt"
                sh "cd /usr/src/jsch/CITest; rm -rf test.txt"
            }
        }
    }
}
