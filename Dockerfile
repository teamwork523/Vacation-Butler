FROM ubuntu:14.04

# Declare working directory
WORKDIR /var/app/current

# Update Package
sudo apt-get update -y

# Add oracle java 7 repository
RUN apt-get -y install software-properties-common
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
#RUN mvn -f VB_API_Services/pom.xml clean install
EXPOSE 8080
#CMD ["java", "-jar", "VB_API_Services/target/vb-api-services-0.0.1-SNAPSHOT.jar", "server"]
CMD ls -lha VB_API_Services
