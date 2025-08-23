#!/bin/bash

# wait-for-db.sh - Wait for database to be ready
# Usage: ./wait-for-db.sh host:port [--] command

set -e

host="$1"
shift
cmd="$@"

until PGPASSWORD=${POSTGRES_PASSWORD:-password} psql -h "$host" -p 5432 -U "${POSTGRES_USER:-nimas2pdf}" -d "${POSTGRES_DB:-nimas2pdf}" -c '\q'; do
  >&2 echo "PostgreSQL is unavailable - sleeping"
  sleep 1
done

>&2 echo "PostgreSQL is up - executing command"
exec $cmd
