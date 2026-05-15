package juego;


import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.InterfaceJuego;
import entorno.Herramientas;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
    private Princesa princesa;
	private Enemigo enemigo;
	private Castillo castillo;
	private Proyectil proyectil;
    private Isla[] islas;
	// Variables y métodos propios de cada grupo
	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
        princesa = new Princesa(640, 360, 20, 50);
        islas = inicializarIslas();
        
		
		// Inicializar lo que haga falta para el juego
		// ...

		// Inicia el juego!
		this.entorno.iniciar();
	}

	private Isla[] inicializarIslas() {
		//Isla[] misIslas = new Isla[20]; // Ejemplo con 10 islas
        
        // 1. Islas de piso (fijas)
		Isla[] misIslas1 = new Isla[20]; // Aumentamos el tamaño para tener más plataformas
	    int indice = 0;

	    // 1. ISLAS DE PISO (Para que el jugador no caiga al inicio)
	    for (int i = 0; i < 5; i++) {
	        misIslas1[indice] = new Isla(i * 250, 580, 200, 20);
	        indice++;
	    }

	    // 2. GENERACIÓN POR "COLUMNAS" (Evita superposición)
	    double avanceX = 600; // Empezamos después del piso inicial
	    double distanciaEntreColumnas = 300; 
	    
	    while (indice < misIslas1.length) {
	        // Decidimos cuántas islas habrá en esta coordenada X (2 o 3)
	        int cantidadEnEstaLinea = (int)(Math.random() * 2) + 2; // Da 2 o 3

	        for (int i = 0; i < cantidadEnEstaLinea; i++) {
	            if (indice < misIslas1.length) {
	                // Niveles de altura fijos para que no se superpongan verticalmente
	                // Nivel 0: 450px, Nivel 1: 300px, Nivel 2: 150px
	                double alturaFija = 450 - (i * 150); 
	                
	                // Agregamos una pequeña variación en X para que no sea una línea perfecta
	                double variacionX = (Math.random() * 50) - 25; 
	                
	                misIslas1[indice] = new Isla(avanceX + variacionX, alturaFija, 120, 20);
	                indice++;
	            }
	        }
	        
	        // Avanzamos en X para la siguiente "tanda" de islas
	        avanceX += distanciaEntreColumnas;
	    }
	    
	    return misIslas1;
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{
		// Procesamiento de un instante de tiempo
		// ...
		princesa.dibujar(entorno);

		//princesa.moverAbajo(); // Simula la gravedad

		for (Isla isla : islas) {
			if (isla != null) { 
	            // Cada isla sabe cómo dibujarse a sí misma
				isla.dibujar(entorno); 
	        // Aprovechamos el bucle para verificar si Elizabeth está apoyada
	        // if (elizabeth.estaApoyadaEn(isla)) {
	        // elizabeth.detenerCaida(isla.getY());
			}
		}

		//movimiento de la princesa

		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && princesa.getX() - princesa.getAncho()/2 > 0) { //limitamos el movimiento para que no se salga de la pantalla
			if(princesa.colisionaPorIzquierda(islas)==false) {
				princesa.moverIzquierda();							
			}
		}
		if(entorno.estaPresionada(entorno.TECLA_DERECHA) && princesa.getX() + princesa.getAncho()/2 < entorno.ancho()) {
			if(princesa.colisionaPorDerecha(islas)==false) {
				princesa.moverDerecha();							
			}
		}
		if (entorno.estaPresionada(entorno.TECLA_ABAJO) && princesa.getY() + princesa.getAlto()/2 < entorno.alto()) {
			if (!princesa.colisionaPorAbajo(islas)) {
				princesa.moverAbajo();
			}
		}
		
		if (entorno.estaPresionada(entorno.TECLA_ARRIBA) && princesa.getY() - princesa.getAlto()/2 > 0) {
			if (!princesa.colisionaPorArriba(islas)) {
				princesa.saltar();
			}
		}



	}
		
		// Procesamiento de un instante de tiempo
		// ...
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
