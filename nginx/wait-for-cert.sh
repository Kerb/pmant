#!/bin/sh
DOMAIN="pmant.kerba.net"
CERT_DIR="/etc/letsencrypt/live/$DOMAIN"
TIMEOUT=300
INTERVAL=5
ELAPSED=0


while [ ! -f "$CERT_DIR/fullchain.pem" ] || [ ! -f "$CERT_DIR/privkey.pem" ]; do
    echo "Ждём сертификат для $DOMAIN в течение $ELAPSED sec ..."
    if [ "$ELAPSED" -ge "$TIMEOUT" ]; then
        echo "Timeout: сертификат не появился за $TIMEOUT секунд"
        exit 1
    fi
    sleep $INTERVAL
    ELAPSED=$((ELAPSED + INTERVAL))
done

echo "Сертификат найден, запускаем Nginx..."
exec nginx -g "daemon off;"
