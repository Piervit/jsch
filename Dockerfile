#Start from the lastest debian stable.
FROM debian:latest

ENV USERNAME=sshuser
ENV PASSWORD=pass

#Add the current application we want to manage to the docker container.
COPY . /var/jenkins_home/workspace/TestJsch_master
WORKDIR /var/jenkins_home/workspace/TestJsch_master
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
WORKDIR /var/jenkins_home/workspace/TestJsch_master/CITest
RUN mvn install
RUN chown -R 1000:1000 /var/jenkins_home/workspace/TestJsch_master
#Create a dummy file && start ssh server && start a bash so we can run commands
CMD service ssh start && /bin/bash
