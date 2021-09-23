#!/bin/bash

pushd "$(dirname "${BASH_SOURCE:0}")" || exit

docker-compose up --build --remove-orphans

popd || exit
