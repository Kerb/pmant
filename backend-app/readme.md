### Порядок сборки

- Запустить контейнер с БД:
```shell
docker compose --profile dev up db --build
```
- Накатить изменения ченджсетов БД:
```shell
mvn liquibase:update -DPOSTGRES_HOST=localhost -DPOSTGRES_PORT=5432 -DPOSTGRES_DB=pmant_db -DPOSTGRES_USER=pmant_user -DPOSTGRES_PASSWORD=pmant_password
```
- Сгенерировать jooq
```shell
mvn jooq-codegen:generate -DPOSTGRES_HOST=localhost -DPOSTGRES_PORT=5432 -DPOSTGRES_DB=pmant_db -DPOSTGRES_USER=pmant_user -DPOSTGRES_PASSWORD=pmant_password
```
- Остановить контейнер с БД:
```shell
docker compose down
```