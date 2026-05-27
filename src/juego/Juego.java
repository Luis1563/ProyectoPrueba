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
	//private Castillo castillo;
	//private Proyectil proyectil;
    private Isla[] islas;
	private Enemigo[] enemigos;
	private boolean juegoTerminado;
	private double respawnX;
	private double respawnY;
	// Variables y métodos propios de cada grupo
	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 1280, 720);
        //princesa = new Princesa(640, 360, 20, 50);
		princesa = new Princesa(640, 360, 30, 50, 3); // Le damos 3 vidas a la princesa
        islas = inicializarIslas();

		//proyectil = null; // No hay proyectil activo al inicio
		// Inicializar lo que haga falta para el juego
		// ...

		this.enemigos = new Enemigo[10]; 
    	//this.enemigos = new Enemigo[6]; 
		for (int i = 0; i < this.enemigos.length; i++) {
			double xInicial;
			int direccionBicho; // Tipo int, igual que en tu constructor
			
			// Altura al azar en el cielo
			double yInicial = 80 + (Math.random() * 400); 
			
			if (i % 2 == 0) {
				// Los pares nacen en la IZQUIERDA y van a la DERECHA
				xInicial = -100 - (i * 200); 
				direccionBicho = 1; 
			} else {
				// Los impares nacen en la DERECHA y van a la IZQUIERDA
				xInicial = 1380 + (i * 200); 
				direccionBicho = -1; 
			}
			
			// Pasamos los 6 parámetros EXACTOS que te pide tu clase Enemigo:
			// x, y, ancho, alto, velocidad, direccion
			this.enemigos[i] = new Enemigo(xInicial, yInicial, 35, 35, 2.0, direccionBicho);
		}




		this.juegoTerminado = false;
		this.respawnX = entorno.ancho() / 2;
		this.respawnY = entorno.alto() / 2;

		// Inicia el juego!
		this.entorno.iniciar();
	}

	private Isla[] inicializarIslas() {
		//Isla[] misIslas = new Isla[20]; // Ejemplo con 10 islas
        
        // 1. Islas de piso (fijas)
		Isla[] misIslas1 = new Isla[200]; // Aumentamos el tamaño para tener más plataformas
	    int indice = 0;

	    // 1. ISLAS DE PISO (Para que el jugador no caiga al inicio)
	    for (int i = 0; i < 20; i++) {
	        misIslas1[indice] = new Isla(i * 250, entorno.alto() - 10, 200, 20);
	        indice++;
	    }

	    // 2. GENERACIÓN POR "COLUMNAS" (Evita superposición)
	    double avanceX = entorno.ancho() / 2; // Empezamos después del piso inicial
	    double distanciaEntreColumnas = 220; 
	    
	    while (indice < misIslas1.length) {
	        // Decidimos cuántas islas habrá en esta coordenada X (2 o 3)
	        int cantidadEnEstaLinea = (int)(Math.random() * 2) + 2; // Da 2 o 3

	        for (int i = 0; i < cantidadEnEstaLinea; i++) {
	            if (indice < misIslas1.length) {
	                // Niveles de altura fijos para que no se superpongan verticalmente
	                // Nivel 0: 450px, Nivel 1: 300px, Nivel 2: 150px
	                double alturaFija = 475 - (i * 150); 
	                
	                // Agregamos una pequeña variación en X para que no sea una línea perfecta
	                double variacionX = (Math.random() * 50) - 500; 
	                
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
		
		if(princesa != null /* && juegoPerdido == false */) {
			princesa.dibujar(entorno);
			princesa.actualizarFisica(islas, entorno.alto());
			princesa.dibujarVidas(entorno);
		}
		// física (gravedad y caída) con límite inferior de pantalla
		if (princesa != null) {
			if (princesa.bordeSuperior() > entorno.alto()) {
				//la princesa cayó al vacío, la reiniciamos al medio
				princesa.perderVida();
				if (!princesa.estaViva()) {
					juegoTerminado = true;
					princesa = null; // La princesa desaparece al perder todas las vidas
				} else {
					princesa.reiniciarPosicion(respawnX, respawnY);
				}
			}
		}
		
		if(princesa != null){
			if(princesa.getProyectil() != null) {
				princesa.getProyectil().dibujar(entorno);			
			}
		}

		// Dibujar princesa
		// Dibujar islas
		for (int i = 0; i < islas.length; i++) {
			if (islas[i] != null) { 
	            // Cada isla sabe cómo dibujarse a sí misma
				islas[i].dibujar(entorno); 
			}
		}


		for (int i = 0; i < this.enemigos.length; i++) {
			if (this.enemigos[i] != null) {
				
				// 1. Avanzan según su propia dirección (si es 1 suma X, si es -1 resta X)
				double nuevaX = this.enemigos[i].getX() + (2.0 * this.enemigos[i].getDireccion());
				
				// 2. EFECTO SCROLL (Se arrastran con las teclas de tus compañeros)
				/*if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
					nuevaX -= 2; 
				}
				if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
					nuevaX += 2; 
				}*/
				
				// Guardamos la posición en el objeto
				this.enemigos[i].setX(nuevaX);
				
				// 3. Lo dibujamos
				this.enemigos[i].dibujar(this.entorno);
			}
		}



		//movimiento de la princesa
		if (princesa != null) {

			if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && princesa.getX() - princesa.getAncho()/2 > 0) { //limitamos el movimiento para que no se salga de la pantalla
				//if (princesa.colisionaPorIzquierda(islas)) {
						
				if (princesa.puedeMover(-princesa.getVelocidadX(), 0, islas)) {
				princesa.moverIzquierda();
				}
			}
			/*if(entorno.estaPresionada(entorno.TECLA_DERECHA) && princesa.getX() + princesa.getAncho()/2 < entorno.ancho()) {
				double desplazamientoIslas = princesa.getVelocidadX()/2; // La velocidad a la que se mueven las islas es la mitad de la velocidad de desplazamiento de la princesa para un efecto de parallax
				double dezplazamientoSuma = princesa.getVelocidadX() + desplazamientoIslas; // La suma del desplazamiento de la princesa y el desplazamiento de las islas para calcular el movimiento total
			
				if (princesa.puedeMover(dezplazamientoSuma, 0, islas)) { //usamos la suma del desplazamiento de la princesa y el desplazamiento de las islas para verificar si el movimiento total es posible sin colisiones
					princesa.moverDerecha();
					if(princesa.getX() > entorno.ancho() / 2) { // Solo mueve las islas si la princesa está más allá del centro de la pantalla
						for (int i = 0; i < islas.length; i++) {
							if (islas[i] != null && princesa.getX() > entorno.ancho() / 2) { // Solo mueve las islas si la princesa está más allá del centro de la pantalla
							islas[i].mover(-desplazamientoIslas); //mueve cada isla en la dirección opuesta al movimiento de la princesa para simular desplazamiento del mapa
							}
						}
					}
				}
			}*/

		
			if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
				double limiteMovimiento = entorno.ancho() * 0.55; // La princesa puede moverse libremente hasta el 55% del ancho de la pantalla
				if (princesa.getX() < limiteMovimiento && princesa.puedeMover(princesa.getVelocidadX(), 0, islas)) {
					princesa.moverDerecha();
    			}
				else if (princesa.getX() >= limiteMovimiento && princesa.puedeMover(princesa.getVelocidadX(), 0, islas)) {
        			// La princesa ya está cerca del borde, se queda fija y el mapa avanza
        			for (int i = 0; i < islas.length; i++) {
            			if (islas[i] != null) {
                			islas[i].mover(-princesa.getVelocidadX() * 0.6); // Mueve las islas a la mitad de la velocidad de la princesa para un efecto de parallax
            			}
        			}
    			}
			}


			//desplazamienmto del mapa
			//if (entorno.estaPresionada(entorno.TECLA_ABAJO) && princesa.getY() + princesa.getAlto()/2 < entorno.alto()) {
				//if (princesa.colisionaPorAbajo(islas)) {
				//if (princesa.puedeMover(0, 5, islas)) {
					//princesa.moverAbajo();
				//}
			//}
		
			//if (entorno.estaPresionada(entorno.TECLA_ARRIBA) && princesa.getY() - princesa.getAlto()/2 > 0) {
				//if (princesa.colisionaPorArriba(islas)) {
				//if (princesa.puedeMover(0, -5, islas)) {
					//princesa.saltar();
					//princesa.moverArriba();
				//} 
			//}
		
			// Salto: solo salta si está apoyado
			if (entorno.estaPresionada(entorno.TECLA_ARRIBA) && princesa.estaApoyado(islas)) {
				princesa.saltar();
			}

			// disparo con botón izquierdo solo si no hay proyectil activo
			if (entorno.mousePresente() && entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO) && princesa.getProyectil() == null) {
				princesa.disparar(entorno.mouseX(), entorno.mouseY());
			
				/*double mouseX = entorno.mouseX();
				double mouseY = entorno.mouseY();
				proyectil = new Proyectil(princesa.getX(), princesa.getY(), 10, mouseX, mouseY); //empieza en la misma x e y que la princesa
				proyectil.dispararHacia(mouseX, mouseY); //dispara hacia la dirección del mouse*/
			}
			
			
			if (princesa.getProyectil() != null) { //si no es null (está activo), lo movemos y dibujamos
				princesa.getProyectil().mover();
				princesa.getProyectil().dibujar(entorno);
				if (princesa.getProyectil().estaFueraDePantalla(entorno)) { // si sale de la pantalla, lo eliminamos y se vuelve null
					princesa.setProyectil(null);
				}
			}
		}
		//if (princesa != null){
		//}

		/*if (princesa.getProyectil() != null) {
			for (int i = 0; i < enemigos.length; i++) {
				if (enemigos[i] != null && princesa.getProyectil().colisionaConEnemigo(enemigos[i])) {
					princesa.setProyectil(null); // El proyectil desaparece al impactar
					enemigos[i] = null; // El enemigo desaparece al ser impactado
				}
			}
		}*/


		
		
	}
		
		// Procesamiento de un instante de tiempo
		// ...
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
