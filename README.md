
### Escuela Colombiana de Ingeniería
### Arquitecturas de Software - ARSW
## Ejercicio Introducción al paralelismo - Hilos - Caso BlackListSearch


### Dependencias:
####   Lecturas:
*  [Threads in Java](http://beginnersbook.com/2013/03/java-threads/)  (Hasta 'Ending Threads')
*  [Threads vs Processes]( http://cs-fundamentals.com/tech-interview/java/differences-between-thread-and-process-in-java.php)

### Descripción
  Este ejercicio contiene una introducción a la programación con hilos en Java, además de la aplicación a un caso concreto.
  

**Parte I - Introducción a Hilos en Java**

1. De acuerdo con lo revisado en las lecturas, complete las clases CountThread, para que las mismas definan el ciclo de vida de un hilo que imprima por pantalla los números entre A y B.

![image](https://user-images.githubusercontent.com/50029247/106065290-1875d300-60c9-11eb-9e4a-90cf77283551.png)
![image](https://user-images.githubusercontent.com/50029247/106065155-dc427280-60c8-11eb-9c88-7d737098c60b.png)

2. Complete el método __main__ de la clase CountMainThreads para que:
	1. Cree 3 hilos de tipo CountThread, asignándole al primero el intervalo [0..99], al segundo [99..199], y al tercero [200..299].
	
		![image](https://user-images.githubusercontent.com/50029247/106065724-b073bc80-60c9-11eb-8782-e698a7f28b50.png)
	
	2. Inicie los tres hilos con 'start()'.
	
		![image](https://user-images.githubusercontent.com/50029247/106065858-e618a580-60c9-11eb-9841-c1a99a35f5c9.png)
	
	3. Ejecute y revise la salida por pantalla. 
	
		![image](https://user-images.githubusercontent.com/50029247/106071546-8a9fe500-60d4-11eb-97ac-c5386f53fa73.png)
	
	4. Cambie el incio con 'start()' por 'run()'. Cómo cambia la salida?, por qué?.
	
		![image](https://user-images.githubusercontent.com/50029247/106067520-d8185400-60cc-11eb-9ab7-c46612e8f78c.png)

		
		La salida cambia porque el metodo run() espera hasta la que termine la ejecucion de el hilo anterior y despues inicia su hilo. 
		
		![image](https://user-images.githubusercontent.com/50029247/106067185-237e3280-60cc-11eb-9a02-8d89dcefc402.png)
	
		
**Parte II - Ejercicio Black List Search**


Para un software de vigilancia automática de seguridad informática se está desarrollando un componente encargado de validar las direcciones IP en varios miles de listas negras (de host maliciosos) conocidas, y reportar aquellas que existan en al menos cinco de dichas listas. 

Dicho componente está diseñado de acuerdo con el siguiente diagrama, donde:

- HostBlackListsDataSourceFacade es una clase que ofrece una 'fachada' para realizar consultas en cualquiera de las N listas negras registradas (método 'isInBlacklistServer'), y que permite también hacer un reporte a una base de datos local de cuando una dirección IP se considera peligrosa. Esta clase NO ES MODIFICABLE, pero se sabe que es 'Thread-Safe'.

- HostBlackListsValidator es una clase que ofrece el método 'checkHost', el cual, a través de la clase 'HostBlackListDataSourceFacade', valida en cada una de las listas negras un host determinado. En dicho método está considerada la política de que al encontrarse un HOST en al menos cinco listas negras, el mismo será registrado como 'no confiable', o como 'confiable' en caso contrario. Adicionalmente, retornará la lista de los números de las 'listas negras' en donde se encontró registrado el HOST.

![](img/Model.png)

Al usarse el módulo, la evidencia de que se hizo el registro como 'confiable' o 'no confiable' se dá por lo mensajes de LOGs:

INFO: HOST 205.24.34.55 Reported as trustworthy

INFO: HOST 205.24.34.55 Reported as NOT trustworthy


Al programa de prueba provisto (Main), le toma sólo algunos segundos análizar y reportar la dirección provista (200.24.34.55), ya que la misma está registrada más de cinco veces en los primeros servidores, por lo que no requiere recorrerlos todos. Sin embargo, hacer la búsqueda en casos donde NO hay reportes, o donde los mismos están dispersos en las miles de listas negras, toma bastante tiempo.

Éste, como cualquier método de búsqueda, puede verse como un problema [vergonzosamente paralelo](https://en.wikipedia.org/wiki/Embarrassingly_parallel), ya que no existen dependencias entre una partición del problema y otra.

Para 'refactorizar' este código, y hacer que explote la capacidad multi-núcleo de la CPU del equipo, realice lo siguiente:

1. Cree una clase de tipo Thread que represente el ciclo de vida de un hilo que haga la búsqueda de un segmento del conjunto de servidores disponibles. Agregue a dicha clase un método que permita 'preguntarle' a las instancias del mismo (los hilos) cuantas ocurrencias de servidores maliciosos ha encontrado o encontró.

	![image](https://user-images.githubusercontent.com/50029247/106073265-b5d80380-60d7-11eb-94a7-6501c12e504c.png)
	
	![image](https://user-images.githubusercontent.com/50029247/106073372-e91a9280-60d7-11eb-832b-9195d4ac7472.png)



2. Agregue al método 'checkHost' un parámetro entero N, correspondiente al número de hilos entre los que se va a realizar la búsqueda (recuerde tener en cuenta si N es par o impar!). Modifique el código de este método para que divida el espacio de búsqueda entre las N partes indicadas, y paralelice la búsqueda a través de N hilos. Haga que dicha función espere hasta que los N hilos terminen de resolver su respectivo sub-problema, agregue las ocurrencias encontradas por cada hilo a la lista que retorna el método, y entonces calcule (sumando el total de ocurrencuas encontradas por cada hilo) si el número de ocurrencias es mayor o igual a _BLACK_LIST_ALARM_COUNT_. Si se da este caso, al final se DEBE reportar el host como confiable o no confiable, y mostrar el listado con los números de las listas negras respectivas. Para lograr este comportamiento de 'espera' revise el método [join](https://docs.oracle.com/javase/tutorial/essential/concurrency/join.html) del API de concurrencia de Java. Tenga también en cuenta:
	
	![image](https://user-images.githubusercontent.com/50029247/106073457-18c99a80-60d8-11eb-8149-5ba82b2f2d31.png)

	* Dentro del método checkHost Se debe mantener el LOG que informa, antes de retornar el resultado, el número de listas negras revisadas VS. el número de listas negras total (línea 60). Se debe garantizar que dicha información sea verídica bajo el nuevo esquema de procesamiento en paralelo planteado.

	* Se sabe que el HOST 202.24.34.55 está reportado en listas negras de una forma más dispersa, y que el host 212.24.24.55 NO está en ninguna lista negra.


**Parte II.I Para discutir la próxima clase (NO para implementar aún)**

La estrategia de paralelismo antes implementada es ineficiente en ciertos casos, pues la búsqueda se sigue realizando aún cuando los N hilos (en su conjunto) ya hayan encontrado el número mínimo de ocurrencias requeridas para reportar al servidor como malicioso. Cómo se podría modificar la implementación para minimizar el número de consultas en estos casos?, qué elemento nuevo traería esto al problema?

**Parte III - Evaluación de Desempeño**

A partir de lo anterior, implemente la siguiente secuencia de experimentos para realizar las validación de direcciones IP dispersas (por ejemplo 202.24.34.55), tomando los tiempos de ejecución de los mismos (asegúrese de hacerlos en la misma máquina):

1. Un solo hilo.
2. Tantos hilos como núcleos de procesamiento (haga que el programa determine esto haciendo uso del [API Runtime](https://docs.oracle.com/javase/7/docs/api/java/lang/Runtime.html)).
3. Tantos hilos como el doble de núcleos de procesamiento.
4. 50 hilos.
5. 100 hilos.

Al iniciar el programa ejecute el monitor jVisualVM, y a medida que corran las pruebas, revise y anote el consumo de CPU y de memoria en cada caso. ![](img/jvisualvm.png)
1 Hilo.

![1 hilo](https://user-images.githubusercontent.com/50029247/106073617-6c3be880-60d8-11eb-9141-73a24f174156.jpeg)
	
2 Hilos.

![2 hilos](https://user-images.githubusercontent.com/50029247/106074040-2a5f7200-60d9-11eb-8dae-7f3fdceba1c4.jpeg)

4 Hilos.

![4 hilos](https://user-images.githubusercontent.com/50029247/106074123-4f53e500-60d9-11eb-9ded-95c5e7d3410c.jpeg)

50 Hilos.

![50 hilos](https://user-images.githubusercontent.com/50029247/106074064-3ba87e80-60d9-11eb-9e57-b33bc7110a04.jpeg)

100 Hilos.

![100 hilos](https://user-images.githubusercontent.com/50029247/106074168-60045b00-60d9-11eb-98b1-dceff278620d.jpeg)
		
		
Con lo anterior, y con los tiempos de ejecución dados, haga una gráfica de tiempo de solución vs. número de hilos. Analice y plantee hipótesis con su compañero para las siguientes preguntas (puede tener en cuenta lo reportado por jVisualVM):

![image](https://user-images.githubusercontent.com/50029247/106075489-b7a3c600-60db-11eb-8695-d7400eae2867.png)

1. Según la [ley de Amdahls](https://www.pugetsystems.com/labs/articles/Estimating-CPU-Performance-using-Amdahls-Law-619/#WhatisAmdahlsLaw?):

	![](img/ahmdahls.png), donde _S(n)_ es el mejoramiento teórico del desempeño, _P_ la fracción paralelizable del algoritmo, y _n_ el número de hilos, a mayor _n_, mayor debería ser dicha mejora. Por qué el mejor desempeño no se logra con los 500 hilos?, cómo se compara este desempeño cuando se usan 200?. 
	
La ley de Amdahl es la ganancia del rendimiento que se logra al realizar una mejora en nuestro procesador. Adeemás de esto, la segunda variante de esta ley menciona que el incremento del rendimiento será menor si se introduce una mejora sobre un sistema previamente mejorado. En el caso de BlackListSearch la se ve reflejada por el numero de Threads usados durante la ejecución. Cuando se realizó la ejecución del programa con 1 hilo obtuvimos un rendimiento de 4 minutos con 24 segundos. Posteriormente , realizando la ejecución del programa con 4 Threads obtuvimos una mejora significativa al presentarse 56 segundos en el tiempo de ejecución. Es así , a medida que se aumenta el numero de Threads , habrá un incremento del rendimiento menor en el programa. De esta manera , se estara acercándo a un tiempo de ejecución constante.

2. Cómo se comporta la solución usando tantos hilos de procesamiento como núcleos comparado con el resultado de usar el doble de éste?.

Al usar 2 Threads se obtuvo un tiempo de minuto y 9 segundos y usando 4 Threads obtenemos un tiempo de 56 segundos.Existe un punto en el que no importa el numero de Threads que aumentemos , no se obtendra un aumento significativo en el rendimiento. Así lo vemos reflejado en este caso en el que a pesar de usar el doble de threads de procesamiento no obtuvimos un aumento de mas de 15 segundos en el rendimiento.

3. De acuerdo con lo anterior, si para este problema en lugar de 100 hilos en una sola CPU se pudiera usar 1 hilo en cada una de 100 máquinas hipotéticas, la ley de Amdahls se aplicaría mejor?. Si en lugar de esto se usaran c hilos en 100/c máquinas distribuidas (siendo c es el número de núcleos de dichas máquinas), se mejoraría?. Explique su respuesta.

Inicialmente,se obtendra un aumento del rendimiento ya que no está realizando ninguna mejora en ninguna de las computadoras. En segundo lugar, se estaria provocando una disminución en el rendimiento al tener que asegurar la coordinación entre todas las maquinas a la hora de acoplar resultados.
Como se menciono en el punto 1 de la parte 4, existe un punto en el que se puede alcanzar un tiempo constante debido al aumento del numero de Threads durante la ejecución. En este caso, ese punto se puede ver reflejado durante el uso de un numero de threads que es igual a cores del sistema. A medida que se aumente el numero de Threads desde este punto se obtendrá un aumento en el rendimiento en la ejecución del programa que no es significante para quien este utilizando la aplicación



