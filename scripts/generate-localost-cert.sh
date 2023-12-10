#!/bin/bash

ROOT_CA=$1
DOMAIN=cryptofunds.local
SUBDOMAIN1=www.cryptofunds.local

# Generate Root CA private key
#openssl genrsa -des3 -out ${ROOT_CA}.key 2048

# Generate Root Certificate
#openssl req -x509 -new -nodes -key ${ROOT_CA}.key -sha256 -days 1825 -out ${ROOT_CA}.pem

# Generate a private key for the site
openssl genrsa -out ${DOMAIN}.key 2048

openssl req -new -key ${DOMAIN}.key -out ${DOMAIN}.csr -config <( cat << EOF
[ req ]
default_bits = 2048
default_md = sha256
prompt = no
encrypt_key = no
distinguished_name = dn

[ dn ]
C = US
ST = California
O = CryptoFunds
CN = ${DOMAIN}
EOF
)

cat > ${DOMAIN}.ext <<EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
extendedKeyUsage = serverAuth
subjectAltName = @alt_names
[alt_names]
DNS.1 = ${DOMAIN}
DNS.2 = ${SUBDOMAIN1}
EOF

openssl x509 -req -in ${DOMAIN}.csr -CA ${ROOT_CA}.pem -CAkey ${ROOT_CA}.key -CAcreateserial \
    -out ${DOMAIN}.crt -days 1825 -sha256 -extfile ${DOMAIN}.ext
