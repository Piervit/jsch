pipeline {
    agent { 
        dockerfile {
                args '-u root'
        }
    }
    stages {
        stage('Test') {
            steps {
                dir(path: '/usr/src/jsch/CITest'){
                    sh "echo 'Simple file content to transfert' > test.txt; java -jar target/testjsch-*-with-dependencies.jar test.txt sshuser@127.0.0.1:/home/sshuser/test.txt pass; cat /home/sshuser/test.txt"
                    sh "rm -rf test.txt"
                }
            }
        }
    }
}
