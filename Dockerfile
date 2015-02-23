FROM ubuntu:14.04

# Declare working directory
WORKDIR /var/app/current

# Update Package
sudo apt-get update -y

# Add oracle java 7 repository
RUN apt-get -y install software-properties-common \
                       sudo
RUN add-apt-repository ppa:webupd8team/java
RUN apt-get -y update

# Accept the Oracle Java license
RUN echo "oracle-java7-installer shared/accepted-oracle-license-v1-1 boolean true" | debconf-set-selections

# Install Oracle Java
RUN apt-get -y install oracle-java7-installer
RUN update-alternatives --display java
ENV JAVA_HOME /usr/lib/jvm/java-7-oracle

# Install Maven
RUN apt-get -y install maven

# Install VB Service APIs
#ADD /var/app/current/VB_API_Services /app/VB_API_Services
RUN sudo cd VB_API_Services && sudo mvn clean install
EXPOSE 8080
CMD ["java", "-jar", "VB_API_Services/target/vb-api-services-0.0.1-SNAPSHOT.jar", "server"]
#CMD pwd && echo "$$$$" && id -u -n && echo "####" && ls -alh
