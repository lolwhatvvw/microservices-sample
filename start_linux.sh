#!/bin/bash

./gradlew jibDockerBuild && docker compose --env-file .env_linux up -d
