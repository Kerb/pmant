#!/bin/sh
# wait-for-cert.sh

DOMAIN="pmant.kerba.net"
CERT_DIR="/etc/letsencrypt/live/$DOMAIN"

# Максимальное время ожидания в секундах
TIMEOUT=300
INTERVAL=5
ELAPSED=0

echo "Ждём сертификат для $DOMAIN..."

while [ ! -f "$CERT_DIR/fullchain.pem" ] || [ ! -f "$CERT_DIR/privkey.pem" ]; do
    if [ $ELAPSED -ge $TIMEOUT ]; then
        echo "Timeout: сертификат не появился за $TIMEOUT секунд"
        exit 1
    fi
    sleep $INTERVAL
    ELAPSED=$((ELAPSED + INTERVAL))
done

echo "Сертификат найден, запускаем Nginx..."
exec nginx -g "daemon off;"
