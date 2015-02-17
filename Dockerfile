FROM ubuntu:14.04

# Update Package
sudo apt-get update -y

# Install JDK
sudo apt-get install -y default-jdk

# Install App
COPY . /app

EXPOSE 8080
CMD ["/usr/bin/java", "-jar", "VB_API_Services/target/vb-api-services-0.0.1-SNAPSHOT.jar", "server"]
