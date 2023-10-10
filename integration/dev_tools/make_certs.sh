#!/bin/sh
set -e

# install requirements if they are missing
#for r in openssl openjdk8-jre-base; do
#    if [ ! "$(command -v "$r")" ]; then
#        if [ "$(id -u)" != 0 ]; then
#            echo "$r is missing, unable to install it"
#            exit 1
#        fi
#
#        apk add --no-cache "$r"
#    fi
#done

out_dir="/home/kkochel/cert_gen"
cert_dir="/opt/certs"
script_dir="$(dirname "$0")"
mkdir -p "$out_dir"

# list all certificates we want, so that we can check if they already exist
#if [ -f "$out_dir/ca.crt" ]; then
#    echo "certificates already exists"
#    cp -r "$out_dir/." /temp/certs
#    exit 0
#fi

# create CA certificate
openssl req -config "$script_dir/ssl.cnf" -new -sha256 -nodes -extensions v3_ca -out "$out_dir/ca.csr" -keyout "$out_dir/ca-key.pem"
openssl req -config "$script_dir/ssl.cnf" -key "$out_dir/ca-key.pem" -x509 -new -days 7300 -sha256 -nodes -extensions v3_ca -out "$out_dir/ca.crt"

# Create certificate for the servers
openssl req -config "$script_dir/ssl.cnf" -new -nodes -newkey rsa:4096 -keyout "$out_dir/server.key" -out "$out_dir/mq.csr" -extensions server_cert
openssl x509 -req -in "$out_dir/mq.csr" -days 1200 -CA "$out_dir/ca.crt" -CAkey "$out_dir/ca-key.pem" -set_serial 01 -out "$out_dir/server.crt" -extensions server_cert -extfile "$script_dir/ssl.cnf"

# Create client certificate
openssl req -config "$script_dir/ssl.cnf" -new -nodes -newkey rsa:4096 -keyout "$out_dir/client.key" -out "$out_dir/client.csr" -extensions client_cert -subj "/CN=admin"
openssl x509 -req -in "$out_dir/client.csr" -days 1200 -CA "$out_dir/ca.crt" -CAkey "$out_dir/ca-key.pem" -set_serial 01 -out "$out_dir/client.crt" -extensions client_cert -extfile "$script_dir/ssl.cnf"

if [ -n "$KEYSTORE_PASSWORD" ]; then
    # Create Java keystore
    mkdir -p /certs/java
    if [ -f /certs/java/cacerts ]; then
        rm /certs/java/cacerts
    fi
    keytool -import -trustcacerts -file "$out_dir/ca.crt" -alias CegaCA -storetype JKS -keystore /certs/java/cacerts -storepass "$KEYSTORE_PASSWORD" -noprompt
    openssl pkcs12 -export -out /certs/java/keystore.p12 -inkey "$out_dir/server.key" -in "$out_dir/server.crt" -passout pass:"$KEYSTORE_PASSWORD"
fi
# fix permissions
chmod 644 "$out_dir"/*

# move certificates to volumes
cp -p "$out_dir/ca.crt" /opt/certs/ca.crt


cp -p "$out_dir/server.crt" /opt/certs/server.crte
cp -p "$out_dir/server.key" /opt/certs/server.key
chown 1000:1000 /opt/certs/server.*

cp -p "$out_dir/server.crt" /opt/certs/mq.crt
cp -p "$out_dir/server.key" /opt/certs/mq.key
chown 100:101 /opt/certs/mq.*

cp -p "$out_dir/server.crt" /opt/certs/db.crt
cp -p "$out_dir/server.key" /opt/certs/db.key
chown 70:70 /opt/certs/db.*

chmod 600 /opt/certs/*.key

cp -p "$out_dir/client.crt" /opt/certs/client/client.crt
cp -p "$out_dir/client.key" /opt/certs/client/client.key
openssl pkcs8 -topk8 -inform PEM -outform DER -in "/opt/certs/client/client.key" -out "/opt/certs/client/client.pk8" -nocrypt
keytool -import -trustcacerts -file /opt/certs/ca.crt -alias BiobankCA -storetype JKS -keystore "/opt/certs/client/cacerts" -storepass changeit -noprompt
openssl pkcs12 -export -out "/opt/certs/client/keystore.p12" -inkey "/opt/certs/server.key" -in "/opt/certs/server.crt" -passout pass:changeit
chown kkochel:kkochel /opt/certs/client

chmod 600 /opt/client_certs/*.key

# needed if testing locally
#mkdir -p /temp/certs
#cp -r "$out_dir/." /temp/certs

ls -la /certs/