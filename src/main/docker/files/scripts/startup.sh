#!/bin/bash

echo '----------< Preparing to start Google Pub/Sub Emulator >----------'

gcloud beta emulators pubsub start --data-dir=$GOOGLE_PUBSUB_EMULATOR_DATA_DIR --host-port=$GOOGLE_PUBSUB_EMULATOR_HOST --log-http --verbosity=$GOOGLE_PUBSUB_EMULATOR_VERBOSITY_LEVEL --user-output-enabled

${gcloud beta emulators pubsub env-init --data-dir=$GOOGLE_PUBSUB_EMULATOR_DATA_DIR}

echo '----------< Finished starting Google Pub/Sub Emulator >----------'