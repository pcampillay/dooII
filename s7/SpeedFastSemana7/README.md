# SpeedFast Semana 7

Aplicación Java Swing conectada a MySQL mediante JDBC.

## Configuración local

1. Crea la base de datos ejecutando `database/schema.sql` en MySQL.
2. Agrega `lib/mysql-connector-j-8.4.0.jar` como biblioteca del proyecto en IntelliJ.
3. Verifica que exista el archivo local `config.properties` junto al README. La aplicación lo carga automáticamente al ejecutarse.

```text
db.url=jdbc:mysql://localhost:3306/speedfast_db
db.user=speedfast_user
db.password=speedfast_local
```

El archivo `config.properties` no se publica en Git. Usa `config.properties.example` como referencia si necesitas crearlo nuevamente.
