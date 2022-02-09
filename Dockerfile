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
RUN mvn install
#Create a dummy file && start ssh server && start a bash so we can run commands
CMD service ssh start && /bin/bash
