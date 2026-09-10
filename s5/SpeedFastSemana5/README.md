# SpeedFast - Semana 5

Versión de SpeedFast que coordina el retiro de pedidos desde una zona de carga
compartida. La sincronización evita que un pedido sea retirado por más de un
repartidor.

## Funcionalidades

- Estados de pedido: `PENDIENTE`, `EN_REPARTO` y `ENTREGADO`.
- Clase `ZonaDeCarga` con acceso sincronizado a los pedidos pendientes.
- Tres repartidores que comparten la misma zona de carga.
- Simulación de la entrega con `Thread.sleep()`.
- Ejecución concurrente mediante `ExecutorService`.

## Ejecución

Abre `SpeedFastSemana5` en IntelliJ IDEA y ejecuta `app.Main`.
