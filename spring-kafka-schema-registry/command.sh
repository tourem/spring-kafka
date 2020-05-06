#!/usr/bin/env bash

docker-compose exec schema-registry bash

# changeit
keytool -import -keystore /usr/lib/jvm/zulu-8-amd64/jre/lib/security/cacerts -trustcacerts -alias "broker" -file /etc/kafka/secrets/broker-ca1-signed.crt

export SCHEMA_REGISTRY_OPTS="-Djavax.net.ssl.keyStore=/etc/kafka/secrets/kafka.schema-registry.keystore.jks -Djavax.net.ssl.trustStore=/etc/kafka/secrets/kafka.schema-registry.truststore.jks -Djavax.net.ssl.keyStorePassword=awesomekafka -Djavax.net.ssl.trustStorePassword=awesomekafka"


kafka-avro-console-consumer --topic zali --bootstrap-server broker.local:19092 --property schema.registry.url=https://schema-registry.local:8081 --from-beginning --consumer.config /etc/kafka/secrets/consumer.conf


http://localhost:8888/orders?name=zali

http://localhost:9021/


docker container prune
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)
