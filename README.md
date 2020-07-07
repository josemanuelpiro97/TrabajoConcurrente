# Trabajo Concurrente
>Proyecto dirigido a la confeccion de un Monitor de concurrencia basado en redes de petri. 

#### Caracteristicas del Monitor
>Monitor utilizado para el manejo y sincronizacion de hilos con el fin de simular un sistema a traves de una red de petri, aplicando alguna
>de las propiedades de esta.
#### Descripcion y funcionamiento 
- El monitor inicializara todas las variables necesarias para su ejecucion. Las caracteristicas de la red
de petri y del sistema son leidas desde un archivo JSON, lo cual nos da la posibilidad de cambiar la red con la que trabaja el monitor,
los tiempos, etc.
- El monitor se encuentra dividido en 4 paquetes separados para independizar el funcionamiento de cada uno. 

######RDP:
- Aqui se encuentra todo lo necesario para la ejecucion y evolucion correcta de una red de petri con o sin tiempo. Esta red se
construye a traves de un archivo JSON. 
######Queue:
- Clase encargada de administrar los hilos que se encuentren durmiendo o esperando a que alguien los despierte para continuar
su ejecucion. 
######Policy:
- En la clase policy se encuentran las herramientas para resolver los conflictos que se presenten dentro de la red. Se le proporcionara
los conflictos que hay en la red y quienes estan esperando en las colas. Esta nos devolvera a quien debemos despertar y asi continuar
la ejecucion de la red. La politica implementada es variable y no es posible cambiarla por ahora, pero podra ser cargada en JSON en un futuro.
######Logger:
- En esta pequeña clase se encuentran los recursos para registrar todos los eventos que se producen en la red y en las colas.
Como por ejemplo, cuando un hilo se va a dormir, cuando se levanta, cuando un disparo es exitoso o no, etc.

####Estructura JSON
> N: Cantidad de transiciones
> M: Cantidad de plazas

Campos obligatorios del JSON:
  - Matriz de incidencia I  _\[dim: MxN]_
  - Vector de marcado _\[dim: M]_
  - Matriz y vector de invariantes de plaza
       - Deben ser ambos utilizados o ninguno, funcionan en conjunto
       - Matriz de invariantes de plaza. _\[dim: IPxM]_
         - Cada fila representa un conjunto de de plazas que formar el invariante (IP Invariantes)
         - Cada columna representa con un 1 si forma parte de algun invariante o no (0).
         - El la dimension de la fila es la cantidad de plazas M
  - Matriz de ventana temporal _\[dim: 2xN]_
     - Cada columna es asignada a una transicion.
     - La primera fila es el inicio de la ventana temporal.
     - la otra fila representa el fin de la ventana temporal.
  - Vector de Timestamp _\[dim: N]_
      - Cada elemento representa una transicion.
      - Valor predeterminado a cargar sera -1, la propia red se encargara de actualizar
        los Timestamp en caso de que la transicion se encuentre sensibilizada por tokens.
  - Info
      - Simple string de informacion de la red.      