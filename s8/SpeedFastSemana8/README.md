# SpeedFast Semana 8

Aplicación Java Swing para gestionar repartidores, pedidos y entregas con MySQL y JDBC.

## Preparación de MySQL

Ejecuta `database/schema.sql` en MySQL Workbench o desde la consola. El script crea la base `speedfast_db` y las tablas requeridas.

## Ejecución en IntelliJ IDEA

1. Abre la carpeta `SpeedFastSemana8` como proyecto.
2. Agrega `lib/mysql-connector-j-8.4.0.jar` como biblioteca del proyecto si IntelliJ no la reconoce.
3. Revisa `config.properties` junto al README y ajusta el usuario y la clave de MySQL de tu equipo.
4. Ejecuta `src/main/Main.java`.

```text
db.url=jdbc:mysql://localhost:3306/speedfast_db
db.user=speedfast_user
db.password=speedfast_local
```

La interfaz contiene pestañas para administrar repartidores, pedidos y entregas. En cada tabla puedes seleccionar un registro para editarlo o eliminarlo.
