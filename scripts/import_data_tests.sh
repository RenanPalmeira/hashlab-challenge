#!/bin/bash

CONTAINER=hashlab-challenge_hashlab-postgres_1
USER=${POSTGRES_USER:-hashlab}

cat dump.sql | docker exec -i $CONTAINER psql -U$USER