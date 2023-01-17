@echo off

gradlew jibDockerBuild && docker compose --env-file .env_win up -d