# SysML v2 API Services (Play / sbt) — Docker image
FROM eclipse-temurin:11-jdk-jammy

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl ca-certificates unzip \
    && rm -rf /var/lib/apt/lists/* \
    && curl -fsSL -o /tmp/sbt.tgz \
      https://github.com/sbt/sbt/releases/download/v1.2.8/sbt-1.2.8.tgz \
    && tar -xzf /tmp/sbt.tgz -C /usr/local \
    && ln -sf /usr/local/sbt/bin/sbt /usr/local/bin/sbt \
    && rm /tmp/sbt.tgz

WORKDIR /app

# Copy build definition first for better layer caching of dependency download
COPY project project
COPY build.sbt ./

RUN sbt -batch update

COPY . .

RUN sbt -batch stage \
    && APP_BIN="$(find target/universal/stage/bin -type f ! -name '*.bat' | head -1)" \
    && test -n "$APP_BIN" \
    && ln -sf "$APP_BIN" /app/start-sysml

# JDBC: set SYSML_JDBC_URL / SYSML_JDBC_USER / SYSML_JDBC_PASSWORD at runtime
# (Compose, docker run -e, or --env-file). If unset, persistence.xml defaults apply.
ENV JAVA_OPTS="-Xmx2G"
EXPOSE 9000

CMD ["/app/start-sysml", "-Dhttp.address=0.0.0.0", "-Dhttp.port=9000"]
