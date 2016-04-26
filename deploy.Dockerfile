FROM ragnarula/jdk:8u73-b02_0.1

ADD target/universal/twitter-sentiment-play-1.0.zip /tmp

RUN unzip /tmp/twitter-sentiment-play-1.0.zip && \
    mv twitter-sentiment-play-1.0 /opt/tsp && \
    rm /tmp/twitter-sentiment-play-1.0.zip

ENTRYPOINT /opt/tsp/bin/twitter-sentiment-play