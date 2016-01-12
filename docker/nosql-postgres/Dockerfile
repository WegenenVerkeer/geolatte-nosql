FROM wegenenverkeer/postgres94

ENV PGPASSWORD docker

RUN /etc/init.d/postgresql start &&\
    psql -U docker -d postgres -h 0.0.0.0 --command "CREATE USER nosql WITH SUPERUSER PASSWORD 'nosql';" &&\
    createdb -E UTF8 -U docker -h 0.0.0.0 -O nosql nosql --template template_postgis2.1 &&\
    psql -U docker -d nosql -h 0.0.0.0 --command "create extension hstore;" &&\
    createdb -E UTF8 -U docker -h 0.0.0.0 -O nosql nosql-test --template template_postgis2.1 &&\
    psql -U docker -d nosql-test -h 0.0.0.0 --command "create extension hstore;" &&\
    /etc/init.d/postgresql stop
