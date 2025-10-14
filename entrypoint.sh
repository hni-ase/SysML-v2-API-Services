#!/bin/bash

set -e

# Set default values if environment variables are not set
DB_HOST=${DB_HOST:-postgres}
DB_PORT=${DB_PORT:-5432}
DB_NAME=${DB_NAME:-sysml2}
DB_USER=${DB_USER:-postgres}
DB_PASSWORD=${DB_PASSWORD:-mysecretpassword}

# Update persistence.xml with environment variables
echo "Configuring database connection to $DB_HOST:$DB_PORT/$DB_NAME"
sed -i "s|jdbc:postgresql://localhost:5432/sysml2|jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME|g" /app/conf/META-INF/persistence.xml
sed -i "s|<property name=\"javax.persistence.jdbc.user\" value=\"postgres\"/>|<property name=\"javax.persistence.jdbc.user\" value=\"$DB_USER\"/>|g" /app/conf/META-INF/persistence.xml
sed -i "s|<property name=\"javax.persistence.jdbc.password\" value=\"mysecretpassword\"/>|<property name=\"javax.persistence.jdbc.password\" value=\"$DB_PASSWORD\"/>|g" /app/conf/META-INF/persistence.xml

# Download sbt-launch jar if not already present
mkdir -p ~/.cache/sbt/boot/sbt-launch/1.11.6/
if [ ! -f ~/.cache/sbt/boot/sbt-launch/1.11.6/sbt-launch-1.11.6.jar ]; then
	wget https://repo1.maven.org/maven2/org/scala-sbt/sbt-launch/1.11.6/sbt-launch-1.11.6.jar -O ~/.cache/sbt/boot/sbt-launch/1.11.6/sbt-launch-1.11.6.jar
fi

# Wait for database to be ready
echo "Waiting for database to be ready..."
while ! nc -z $DB_HOST $DB_PORT; do
  sleep 1
done
echo "Database is ready!"

# Run sbt to create distribution and then run it
echo "Starting SBT compilation and application..."
echo "Building application distribution..."
sbt stage

echo "Starting application in production mode..."
export APPLICATION_SECRET=${APPLICATION_SECRET:-"tXyZrg3Iun1gPTxofBfuSZcabPlN+q+Su41sOUDQjrQNe2F3yihLbf8aw"}
exec ./target/universal/stage/bin/sysml-v2-api-services \
  -Dplay.server.http.address=0.0.0.0 \
  -Dplay.http.secret.key="$APPLICATION_SECRET" 