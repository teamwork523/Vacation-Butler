FROM ubuntu:14.04

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
#RUN echo "JAVA_HOME=/usr/lib/jvm/java-7-oracle" >> /etc/environment

# Install App
#Add /var/app/current/VB_API_Services /src
EXPOSE 8080
CMD ["java", "-jar", "/var/app/current/VB_API_Services/target/vb-api-services-0.0.1-SNAPSHOT.jar", "server"]
