###
# vert.x docker example using a Java verticle packaged as a fatjar
# To build:
#  docker build -t sample/vertx-java-fat .
# To run:
#   docker run -t -i -p 8080:8080 sample/vertx-java-fat
###

# FROM adoptopenjdk/openjdk8-openj9
FROM adoptopenjdk/openjdk8-openj9:alpine-jre

ENV VERTICLE_FILE eurovision-song-contest-1.0.0-SNAPSHOT-fat.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles

EXPOSE 8888

# Copy your fat jar to the container
COPY build/libs/$VERTICLE_FILE $VERTICLE_HOME/

# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $VERTICLE_FILE"]
