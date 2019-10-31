#!/usr/bin/env bash

cd "$(dirname "$0")/.."

./mvnw package
docker build . -t maarten551/tic-tac-toe-multiplayer-back-end
docker push maarten551/tic-tac-toe-multiplayer-back-end
