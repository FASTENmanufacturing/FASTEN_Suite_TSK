FROM ubuntu:18.04

ENV LANG C.UTF-8

RUN apt-get update -y && \
    apt-get install -y python3.6-minimal python3-pip

COPY . /ppa

WORKDIR /ppa/docs

RUN pip3 install -r requirements.txt

WORKDIR /ppa

EXPOSE 8083

ENTRYPOINT [ "python3" ]

CMD [ "start.py" ]
