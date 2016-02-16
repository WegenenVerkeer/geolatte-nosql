FROM wegenenverkeer/java8

RUN curl -s https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add - && \
    sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt/ trusty-pgdg main" >> /etc/apt/sources.list.d/pgdg.list' && \
    apt-get update && apt-get install -yq postgresql-client-9.4 && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

COPY powerUpWhenReady /root/

COPY *.deb /tmp/app.deb

RUN dpkg -i /tmp/app.deb && \
    rm -rf /tmp/* /var/tmp/*  && \
    update-rc.d -f geolatte-nosqlfs remove

CMD /root/powerUpWhenReady

EXPOSE 8080
