FROM wegenenverkeer/java8

ADD powerUpWhenReady /root/
ADD *.deb /tmp/app.deb

RUN apt-get update && \
		curl -s https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add - && \
    sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt/ trusty-pgdg main" >> /etc/apt/sources.list.d/pgdg.list' && \
    apt-get update && \
    apt-get install -yq postgresql-client-9.4 && \
    dpkg -i /tmp/app.deb && \
    update-rc.d -f geolatte-nosqlfs remove && \
    /usr/sbin/enable_insecure_key && \
    apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

CMD /root/powerUpWhenReady

EXPOSE 8080
