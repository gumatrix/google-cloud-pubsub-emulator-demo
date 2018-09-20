FROM google/cloud-sdk:216.0.0-alpine

LABEL maintainer="Gary Underwood" \
      organisation="Player Services" \
      area="Incentive" \
      team="Unify" \
      title="Googe Cloud Pub/Sub Emulator" \
      description="Used to simulate the Google Cloud Pub/Sub behaviour on a local development machine, or build agent."

WORKDIR root

################################################################
# Install Google Pub/Sub Emulator and dependencies
################################################################

RUN apk --update add openjdk7-jre
RUN gcloud components update
RUN gcloud components install app-engine-java kubectl
RUN gcloud components install beta pubsub-emulator

################################################################
# Configure Google Pub/Sub Emulator startup configuration
################################################################

# Defaults
ARG GOOGLE_PUBSUB_EMULATOR_HOST_IP=0.0.0.0
ARG GOOGLE_PUBSUB_EMULATOR_HOST_PORT=8095
ARG GOOGLE_PUBSUB_EMULATOR_VERBOSITY_LEVEL=info
ARG GOOGLE_PUBSUB_EMULATOR_DATA_DIR=var/pubsub

ENV GOOGLE_PUBSUB_EMULATOR_HOST $GOOGLE_PUBSUB_EMULATOR_HOST_IP:$GOOGLE_PUBSUB_EMULATOR_HOST_PORT
ENV GOOGLE_PUBSUB_EMULATOR_VERBOSITY_LEVEL $GOOGLE_PUBSUB_EMULATOR_VERBOSITY_LEVEL
ENV GOOGLE_PUBSUB_EMULATOR_DATA_DIR $GOOGLE_PUBSUB_EMULATOR_DATA_DIR

VOLUME $GOOGLE_PUBSUB_EMULATOR_DATA_DIR

RUN mkdir -p $GOOGLE_PUBSUB_EMULATOR_DATA_DIR

COPY files/scripts/startup.sh startup.sh
RUN chmod 755 startup.sh

################################################################
# Expose Google Pub/Sub Emulator port
################################################################

EXPOSE $GOOGLE_PUBSUB_EMULATOR_HOST_PORT/tcp

################################################################
# Start Google Pub/Sub Emulator fake
################################################################

ENTRYPOINT [ "./startup.sh" ]