#Start from the lastest debian stable.
FROM debian:latest

ENV USERNAME=sshuser
ENV PASSWORD=pass

#Add the current application we want to manage to the docker container.
COPY . /usr/src/jsch
WORKDIR /usr/src/jsch
#update application cache
RUN apt-get update
#install openjdk and maven
RUN apt-get install -y openjdk-11-jdk-headless
RUN apt-get install -y maven
#install openssh-server
RUN apt-get install -y openssh-server
#Add user to be used in the ssh connection
RUN useradd -ms /bin/bash sshuser
RUN echo "sshuser:pass" | chpasswd  sshuser
#Compile the library jsch
RUN mvn install
#Compile our test application
WORKDIR /usr/src/jsch/CITest
RUN mvn install
#Create a dummy file
#start ssh server
#Run the application
CMD echo "this is a stupid file." > test.txt; service ssh start ; java -jar target/testjsch-*-with-dependencies.jar test.txt sshuser@127.0.0.1:/home/sshuser/test.txt pass; cat /home/sshuser/test.txt
~
