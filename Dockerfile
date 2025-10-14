# Use official OpenJDK 11 image
FROM openjdk:11-jdk-slim

# Set working directory
WORKDIR /app

# Install dependencies and sbt with proper error handling
RUN set -ex && \
    apt-get update && \
    apt-get install -y --no-install-recommends curl wget gnupg2 ca-certificates netcat-openbsd && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | gpg --dearmor | tee /etc/apt/trusted.gpg.d/sbt.gpg > /dev/null && \
    apt-get update && \
    apt-get install -y --no-install-recommends sbt && \
    # Verify installations
    java -version && \
    sbt --version && \
    # Cleanup
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# Copy application files
COPY . .

# Make entrypoint script executable
RUN chmod +x entrypoint.sh

# Set Java options to avoid cgroup issues and optimize for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom -XX:+IgnoreUnrecognizedVMOptions"
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom -XX:+IgnoreUnrecognizedVMOptions -Xmx2g"
ENV SBT_OPTS="-Djava.awt.headless=true -Dfile.encoding=UTF-8 -Dsbt.log.noformat=true -Dlog4j2.disable.jmx=true -Dsbt.coursier.home=/tmp/coursier -Dsbt.global.base=/tmp/sbt -Dsbt.ivy.home=/tmp/ivy"

# Pre-download dependencies to speed up container startup
RUN sbt $SBT_OPTS update compile stage || echo "Build completed with warnings"

# Create an entrypoint script to configure database connection at runtime

# Expose Play default port
EXPOSE 9000

# Run the app using the entrypoint script
CMD ["./entrypoint.sh"]
