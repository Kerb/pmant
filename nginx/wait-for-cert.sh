#!/bin/sh
DOMAIN="pmant.eu"
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

echo "Сертификат найден успешно!"

TIMEOUT=10
INTERVAL=2
ELAPSED=0

# Ждём, пока certbot не завершится
while docker ps --format '{{.Names}}' | grep -q "$CERTBOT_CONTAINER_NAME"; do
  if [ "$ELAPSED" -ge "$TIMEOUT" ]; then
      echo "Timeout: сертификат не появился за $TIMEOUT секунд"
      exit 1
  fi
  echo "Waiting for certbot container to finish..."
  sleep 2
  ELAPSED=$((ELAPSED + INTERVAL))
done

echo "Запускаем nginx..."
exec nginx -g "daemon off;"
