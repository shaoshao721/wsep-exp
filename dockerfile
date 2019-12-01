FROM ubuntu:16.04
MAINTAINER ss23485 <ss23485@126.com>

RUN mkdir /script
ADD sources.list /etc/apt/sources.list

RUN DEBIAN_FRONTEND=noninteractive && \
	apt-get -qq update && \
	apt-get -y -qq dist-upgrade
RUN DEBIAN_FRONTEND=noninteractive && apt-get -qq install -y vim wget git lsof iputils-ping curl openjdk-8-jdk
RUN DEBIAN_FRONTEND=noninteractive && apt-get -qq install -y python3 python3-pip

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

# RUN DEBIAN_FRONTEND=noninteractive && \
#     wget -q https://packages.microsoft.com/config/ubuntu/16.04/packages-microsoft-prod.deb -O packages-microsoft-prod.deb && \
#     dpkg -i packages-microsoft-prod.deb && \
#     apt-get update && \
#     apt-get install apt-transport-https -y && \
#     apt-get install dotnet-sdk-2.1  -y && \

ADD run.sh /script/run.sh
ADD tools.sh /script/tools.sh
RUN	chmod +x /script/*.sh && \
	sh /script/tools.sh

ENTRYPOINT ["sh", "/script/run.sh"]