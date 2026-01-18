FROM sbtscala/scala-sbt:eclipse-temurin-17.0.4_1.7.1_3.2.0

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    libxrender1 libxtst6 libxi6 libgtk-3-0 libgl1-mesa-glx libgl1-mesa-dri \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY build.sbt /app/
COPY project /app/project/

RUN sbt update

COPY . /app

RUN sbt compile

CMD ["sbt", "run"]