FROM ubuntu:20.04

ENV MAVEN_VERSION=3.8.8
ENV JAVA_VERSION=17

RUN apt-get update && \
    apt-get install -y wget openjdk-${JAVA_VERSION}-jdk && \
    apt-get install -y vim && \
    rm -rf /var/lib/apt/lists/*

RUN wget https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    tar xzf apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    rm apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    mv apache-maven-${MAVEN_VERSION} /opt/maven && \
    ln -s /opt/maven/bin/mvn /usr/bin/mvn

ENV JAVA_HOME=/usr/lib/jvm/java-${JAVA_VERSION}-openjdk-amd64
ENV MAVEN_HOME=/opt/maven

ENV JAVA_VERSION=${JAVA_VERSION}
ENV MAVEN_VERSION=${MAVEN_VERSION}

EXPOSE 8080

COPY target/poc-redis-cluster.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
