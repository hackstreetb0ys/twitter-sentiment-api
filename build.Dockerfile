FROM ragnarula/typesafe-activator:latest

ADD $WORKDIR

RUN activator update