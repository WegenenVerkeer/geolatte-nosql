FROM wegenenverkeer/java8

RUN mkdir -p /etc/service/geolatte-nosql
ADD startapplication.sh /etc/service/geolatte-nosql/run
RUN chmod a+x /etc/service/geolatte-nosql/run

EXPOSE 9000
