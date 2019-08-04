#!/bin/bash

mongoimport \
  --authenticationDatabase admin \
  --username hashlab \
  --password hashlab \
  --db hashlab \
  --mode upsert \
  --collection user \
  --type json \
  --file /docker-entrypoint-initdb.d/users.json \
  --jsonArray
