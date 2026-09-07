# SpeedFast - Semana 4

Continuación del sistema de entregas SpeedFast para Desarrollo Orientado a
Objetos II. Esta versión simula entregas concurrentes mediante repartidores que
ejecutan sus pedidos en paralelo.

## Funcionalidades

- Pedidos de comida, encomienda y express con tiempos de entrega distintos.
- Uso de las interfaces `Despachable`, `Cancelable` y `Rastreable`.
- Clase `Repartidor` que implementa `Runnable` y procesa su lista de pedidos.
- Simulación de cada entrega con una pausa aleatoria usando `Thread.sleep()`.
- Ejecución simultánea de tres repartidores con `ExecutorService`.
- Espera hasta el término de todas las entregas antes de finalizar el programa.

## Ejecución

Abre `SpeedFastSemana4` en IntelliJ IDEA y ejecuta `app.Main`.
