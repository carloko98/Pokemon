FROM sbtscala/scala-sbt:eclipse-temurin-17.0.4_1.7.1_3.2.0

# Install libraries for GUI (JavaFX/GTK support)
RUN apt-get update && apt-get install -y \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libgl1-mesa-glx \
    libgtk-3-0 \
    libcanberra-gtk-module

WORKDIR /app

COPY build.sbt .
COPY project project/

RUN sbt update

COPY . .

RUN sbt compile

CMD ["sbt", "run"]