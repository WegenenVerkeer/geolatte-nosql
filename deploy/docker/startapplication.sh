#!/bin/sh

exec /ONT/geolatte-nosql/app/geolatte-nosql/bin/geolatte-nosql -J-Dconfig.file=/ONT/geolatte-nosql/app/geolatte-nosql/config/geolatte-nosql.conf -J-Dlogger.file=/ONT/geolatte-nosql/app/geolatte-nosql/config/geolatte-nosql-logger.xml 2>&1
