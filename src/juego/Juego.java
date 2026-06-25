package juego;


import java.awt.Color;
import java.awt.Image;
import java.util.Arrays;

import entorno.Entorno;
import entorno.InterfaceJuego;
import entorno.Herramientas;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private boolean mostrandoInicio; // pantalla de inicio atributo
	private Castillo castillo;
	private Princesa princesa;
	//private Proyectil proyectil;
    private Isla[] islas;
	private int islasPiso;
	private Enemigo[] enemigos;
	//private Item item; // Item que puede soltar el enemigo y recoger la princesa
	private boolean juegoGanado; // boolean para pantalla ganadora
	private boolean juegoPerdido; // boolean para pantalla de derrota
	private double respawnX; // para reiniciar a la princesa
	private double respawnY;

	private Image imagenDerrota; // Imagen para la pantalla de derrota
	private Image imagenVictoria; // Imagen para la pantalla de victoria
	private Image portada; // Imagen para la pantalla de inicio
	private Image fondo; // Imagen de fondo del juego

	private double velocidadMapa;
	private int puntuacion;           // sistema de puntos
	private int proximaVidaExtra;
	private int ticksMensaje;
	private int duracionMensaje;
	// Variables y métodos propios de cada grupo
	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "baez-gomez-rivera-tp-p1", 1280, 720);
		this.mostrandoInicio = true; //Pantalla de inicio
		princesa = new Princesa(200, 100, 30, 80, 10, entorno);
		this.islasPiso = 15;
		this.islas = inicializarIslas(this.islasPiso);
		this.velocidadMapa = 3;
		this.enemigos = new Enemigo[20];
		//this.item = null; // El item comienza como null, se asignará cuando un enemigo muera
		//this.portada = Herramientas.cargarImagen("portada.png"); // Carga la imagen de portada para la pantalla de inicio
		//this.fondo = Herramientas.cargarImagen("fondo.png"); // Carga la imagen de fondo del juego

		this.juegoGanado = false; // boolean para pantalla ganadora
		//this.imagenDerrota = Herramientas.cargarImagen("derrota.png"); // Carga la imagen de derrota
		this.juegoPerdido = false;
		//this.imagenVictoria = Herramientas.cargarImagen("victoria.png"); // Carga la imagen de victoria

		this.respawnX = entorno.ancho() / 2;
		this.respawnY = entorno.alto() / 2 + 50;


		this.puntuacion = 0;
		this.proximaVidaExtra = 100;
		this.ticksMensaje = 0;
		this.duracionMensaje = 200;


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

		
		// Inicia el juego!
		this.entorno.iniciar();
	}

	private Isla[] inicializarIslas(int islasPisoParametro) {
		
		Isla[] islas = new Isla[500]; // cantidad maxima de islas que se pueden crear

		int islasPiso = islasPisoParametro;
		//double islasFlotantes = 30;

		// creamos todas las islas de piso
		for (int i = 0; i < islasPiso; i++){
			islas[i] = new Isla(i *250 , entorno.alto() - 10, 200, 20);
			
			// Accomodamos las islas para que solo se vean dentro de la pantalla
			islas[i].setX(islas[i].getX() + islas[i].getAncho()/2);
			//islas[i].setX(islas[i].bordeIzquierdo());
			
			if (i == islasPiso -1) { // Si es la última isla de piso, colocamos el castillo sobre ella
				double x = islas[i].getX();
				double y = islas[i].getY() - islas[i].getAlto() / 2; // Coloca el castillo justo encima de la isla
				
				this.castillo = new Castillo(x, y, 160, 200);
				this.castillo.setY(this.castillo.getY() - this.castillo.getAlto()/2);
			}
		}
		
		// Accomodamos las islas para que solo se vean dentro de la pantalla
		/*for (int i = 0; i < islasPiso; i++){
			islas[i].setX(islas[i].getX() + islas[i].getAncho()/2);
			//islas[i].setX(islas[i].bordeIzquierdo());
			
			if (i == islasPiso -1) { // Si es la última isla de piso, colocamos el castillo sobre ella
				double x = islas[i].getX();
				double y = islas[i].getY() - islas[i].getAlto() / 2; // Coloca el castillo justo encima de la isla
				
				this.castillo = new Castillo(x, y, 160, 200);
				this.castillo.setY(this.castillo.getY() - this.castillo.getAlto()/2);
			}
		}*/

		int indice = 1;
		for (int i = islasPiso; i < (islas.length); i++){
			double yIslas1 = 200;
			double yIslas2 = 350;
			double yIslas3 = 500;
			//double distanciaMinimaEnX = 100;

			double anchoIsla = Math.random() * 200;
			while (anchoIsla < 90){
				anchoIsla = Math.random() * 200;
			}
			double random = Math.random();
			if (random<0.33){
				islas[i] = new Isla(indice*280 , yIslas1, anchoIsla, 20);
				if (islas[i].bordeDerecho()>islas[islasPiso-1].bordeIzquierdo()){
					//islas[i].setX(islas[islasPiso-2].getX());
					islas[i] = null;
				}
				if(islas[i] != null && islas[i-1]!= null){
				if (Math.random() < 0.45 && islas[i-1] != islas[islasPiso-1] && islas[i-1].getY() != yIslas1){
					islas[i].setX(islas[i-1].getX());
					indice -= 1;
				}}
			}
			else if (random>0.66){
				islas[i] = new Isla(indice*260 , yIslas2, anchoIsla, 20);
				if (islas[i].bordeDerecho()>islas[islasPiso-1].bordeIzquierdo()){
					//islas[i].setX(islas[islasPiso-2].getX());
					islas[i] = null;
				}
				if(islas[i] != null && islas[i-1]!= null){
				if (Math.random() < 0.45 && islas[i-1] != islas[islasPiso-1] && islas[i-1].getY() != yIslas2){
					islas[i].setX(islas[i-1].getX());
					indice -= 1;
				}}
			}
			else{
				islas[i] = new Isla(indice*250  , yIslas3, anchoIsla, 20);
				if (islas[i].bordeDerecho()>islas[islasPiso-1].bordeIzquierdo()){
					//islas[i].setX(islas[islasPiso-2].getX());
					islas[i] = null;
				}
				if(islas[i] != null && islas[i-1]!= null){
					if (Math.random() < 0.45 && islas[i-1] != islas[islasPiso-1] && islas[i-1].getY() != yIslas3){
						islas[i].setX(islas[i-1].getX());
						indice -= 1;
					}
				}
			}
			indice+=1;
		}
		return islas;
	}


		/*Isla[] islasPiso = new Isla[islas.length/4];

		Isla[] islas1 = new Isla[islas.length/4];
		Isla[] islas2 = new Isla[islas.length/4];
		Isla[] islas3 = new Isla[islas.length/4];*/

		/*double islasPiso = 10; // Cantidad de islas de piso
		double islas1 = 20; // Cantidad de islas flotantes de primera fila
		double islas2 = 30; // Cantidad de islas flotantes de seunda fila
		double islas3 = 40; // Cantidad de islas flotantes de tercera fila
		
		double distanciaEntreX = 250;
		double anchoI = 200;

		// creamos todas las islas de piso
		for (int i = 0; i < islasPiso; i++){
			islas[i] = new Isla(i *250 , entorno.alto() - 10, 200, 20);
		}
		
		// Accomodamos las islas para que solo se vean dentro de la pantalla
		for (int i = 0; i < islasPiso; i++){
			islas[i].setX(islas[i].getX() + islas[i].getAncho()/2);
			//islas[i].setX(islas[i].bordeIzquierdo());
			
			if (i == islasPiso -1) { // Si es la última isla de piso, colocamos el castillo sobre ella
				double x = islas[i].getX();
				double y = islas[i].getY() - islas[i].getAlto() / 2; // Coloca el castillo justo encima de la isla
				
				this.castillo = new Castillo(x, y, 160, 200);
				this.castillo.setY(this.castillo.getY() - this.castillo.getAlto()/2);
			}
		}

		// creamos islas1
		int indice1 = 0; 
		for (int i = 10; i < islas1-1; i++){
			double anchoIsla = Math.random() * 300;
			while (anchoIsla < 90){
				anchoIsla = Math.random() * 300;
			}
			islas[i] = new Isla((indice1) *250 , 500, anchoIsla, 20);
			indice1 += 1;
		}

		// creamos islas2
		int indice2 = 0; 
		for (int i = 20; i < islas2-1; i++){
			double anchoIsla = Math.random() * 300;
			while (anchoIsla < 90){
				anchoIsla = Math.random() * 300;
			}
			islas[i] = new Isla((indice2) *250 , 350, anchoIsla, 20);
			indice2 += 1;
		}

		// creamos islas3
		int indice3 = 0; 
		for (int i = 30; i < islas3-1; i++){
			double anchoIsla = Math.random() * 300;
			while (anchoIsla < 90){
				anchoIsla = Math.random() * 300;
			}
			islas[i] = new Isla((indice3) *250 , 200, anchoIsla, 20);
			indice3 += 1;
		}*/

		//return islas;
	//}





		//cantidad de Islas total: 200 (10 de piso + 190 distribuidas en el cielo)
		//Isla[] misIslas1 = new Isla[cantIslasPiso * 10]; // Aumentamos el tamaño para tener más plataformas
	    //int indice = 0;

	    // 1. ISLAS DE PISO (Para que el jugador no caiga al inicio)
	    /*for (int i = 0; i < cantIslasPiso; i++) {
	        misIslas1[indice] = new Isla(i * 250, entorno.alto() - 10, 200, 20);
			if (i == cantIslasPiso -1) { // Si es la última isla de piso, colocamos el castillo sobre ella
				double x = misIslas1[indice].getX();
				double y = misIslas1[indice].getY() - misIslas1[indice].getAlto() / 2; // Coloca el castillo justo encima de la isla
				
				this.castillo = new Castillo(x, y, 160, 200);
				this.castillo.setY(this.castillo.getY() - this.castillo.getAlto()/2);
			}
			indice++;
	    }*/

	    // 2. GENERACIÓN POR "COLUMNAS" (Evita superposición)
	    /*double avanceX = entorno.ancho() / 2; // Empezamos después del piso inicial
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
	    
	    return misIslas1;*/
	//}

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

		if (mostrandoInicio) {
			//System.out.println(Arrays.toString(entorno.fontDisponibles));
			this.entorno.colorFondo(new Color(146, 197, 252));
		    // Configuramos el estilo del texto y el color 
			this.entorno.cambiarFont("Akira Expanded", 60,new Color(0, 150,255)); 
		    // Escribimos el título en pantalla
			this.entorno.escribirTexto("Super Elizabeth Sis", 40, 300);

			this.entorno.cambiarFont("Arial", 30, Color.GREEN);
			this.entorno.escribirTexto("Presione ENTER para jugar", 40, 350);

			// Detecta si el usuario presiona la tecla ENTER para cambiar el estado
			if (this.entorno.estaPresionada(this.entorno.TECLA_ENTER)) {
				this.mostrandoInicio = false;
			}
		} 
		else if (juegoPerdido) {
			
			String mensajeDePerdida= "GAME OVER";
			int largoMensaje = mensajeDePerdida.length();
			this.entorno.cambiarFont("Arial Black", 60, Color.RED, 0);
			this.entorno.escribirTexto(mensajeDePerdida, entorno.ancho() / 2 - (largoMensaje * 18), entorno.alto() / 2);
		}
		else {
			
			this.entorno.colorFondo(new Color(146, 197, 252));
			this.entorno.cambiarFont("Arial Black", 30, Color.RED, 0);
			this.entorno.escribirTexto("vidas", 30, 40);
			this.castillo.dibujar(entorno);


		if(princesa != null) {
			princesa.dibujar(entorno);
			princesa.actualizarFisica(islas);
			princesa.dibujarVidas(entorno);
		}
		// física (gravedad y caída) con límite inferior de pantalla
		//if (princesa != null) {
			if (princesa.bordeSuperior() > entorno.alto()) {
				//la princesa cayó al vacío, la reiniciamos al medio
				princesa.perderVida();
				if (!princesa.estaViva()) {
					juegoPerdido = true;
					princesa = null; // La princesa desaparece al perder todas las vidas
				} else {
					princesa.reiniciarPosicion(respawnX, respawnY);
				}
			}
		//}
		
		//if(princesa != null){
			if(princesa.getProyectil() != null) {
				princesa.getProyectil().dibujar(entorno);			
			}
		//}

		// Dibujar princesa
		// Dibujar islas
		for (int i = 0; i < islas.length; i++) {
			if (islas[i] != null) { 
	            // Cada isla sabe cómo dibujarse a sí misma
				islas[i].dibujar(entorno); 
			}
		}


		this.renovarEnemigos();

		for (int i = 0; i < this.enemigos.length; i++) {
			if (this.enemigos[i] != null) {
				this.enemigos[i].actualizar(0);
				this.enemigos[i].dibujar(this.entorno);
			}
		}

		/*for (int i = 0; i < this.enemigos.length; i++) {
			if (this.enemigos[i] != null) {
				
				// 1. Avanzan según su propia dirección (si es 1 suma X, si es -1 resta X)
				double nuevaX = this.enemigos[i].getX() + (2.0 * this.enemigos[i].getDireccion());
				
				// 2. EFECTO SCROLL (Se arrastran con las teclas de tus compañeros)
				/*if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
					nuevaX -= 2; 
				}
				if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
					nuevaX += 2; 
				}
				
				// Guardamos la posición en el objeto
				this.enemigos[i].setX(nuevaX);
				
				// 3. Lo dibujamos
				this.enemigos[i].dibujar(this.entorno);
			}
		}*/



		//movimiento de la princesa
		if (princesa != null) {

			for (int i = 0; i < enemigos.length; i++) {
				if (princesa != null && (princesa.colisionaPorAbajo(enemigos[i]) || princesa.colisionaPorArriba(enemigos[i]) || princesa.colisionaPorDerecha(enemigos[i]) || princesa.colisionaPorIzquierda(enemigos[i]))) {
					princesa.perderVida();
					enemigos[i] = null; // El enemigo desaparece al colisionar con la princesa

					if (!princesa.estaViva()) {
						princesa = null; // La princesa desaparece al perder todas las vidas
						juegoPerdido = true;
						return;
					}
				}
			}

			if (princesa != null) {
				if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && princesa.getX() - princesa.getAncho()/2 > 0) { //limitamos el movimiento para que no se salga de la pantalla
					//if (princesa.colisionaPorIzquierda(islas)) {
							
					if (princesa.puedeMover(-princesa.getVelocidadX(), 0, islas)) {
					princesa.moverIzquierda();
					}
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

		// Movimiento DERECHA
				if (entorno.estaPresionada(entorno.TECLA_DERECHA) && princesa.bordeDerecho() < entorno.ancho()) { 
					// Si la princesa se puede mover y no está en el limite de movimiento
					if (princesa.puedeMover(princesa.getVelocidadX(), 0, islas) && !princesa.estaEnlimiteMovimiento(princesa.getVelocidadX(), 0)){
						princesa.moverDerecha();
					}
					
					// Si la princesa se puede mover y está en el limite de movimiento
					else if (princesa.puedeMover(princesa.getVelocidadX(), 0, islas) && princesa.estaEnlimiteMovimiento(princesa.getVelocidadX(), 0)){
						// Si el castillo no está dentro de la pantalla
						if ((/*castillo.bordeDerecho()*/ this.islas[islasPiso-1].bordeDerecho() > entorno.ancho())){
							// Movemos el castillo y los elementos en pantalla en esta parte para hacerlo solo cuando se aprieta la tecla derecha
							this.castillo.mover(-this.velocidadMapa);
							for (int i = 0; i < islas.length; i++) {
								if (islas[i] != null) { // Solo mueve las islas si la princesa está más allá del centro de la pantalla
									islas[i].mover(-this.velocidadMapa); //mueve cada isla en la dirección opuesta al movimiento de la princesa para simular desplazamiento del mapa
								}
							}

							/*if (this.item != null){
								this.item.mover(-this.velocidadMapa);
							*/

						}
						// Si el castillo está dentro de la pantalla
						else {
							this.velocidadMapa = 0;
							princesa.moverDerecha();
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
		
			//solo salta si está apoyada
		if (princesa != null) {
			if (entorno.estaPresionada(entorno.TECLA_ARRIBA) && princesa.estaApoyado(islas)) {
				princesa.saltar();
			}
		}

			// disparo con botón izquierdo solo si no hay proyectil activo
			if (entorno.mousePresente() && entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO) && princesa.getProyectil() == null) {
				princesa.disparar(entorno.mouseX(), entorno.mouseY());

				/*double mouseX = entorno.mouseX();
				double mouseY = entorno.mouseY();
				proyectil = new Proyectil(princesa.getX(), princesa.getY(), 10, mouseX, mouseY); //empieza en la misma x e y que la princesa
				proyectil.dispararHacia(mouseX, mouseY); //dispara hacia la dirección del mouse*/
			}
			
			if (princesa != null){
				if (princesa.getProyectil() != null) { //si no es null (está activo), lo movemos y dibujamos
					princesa.getProyectil().mover();
					princesa.getProyectil().dibujar(entorno);

					for (int i = 0; i < enemigos.length; i++) {
						if (princesa.getProyectil() != null) {
							if (enemigos[i] != null && princesa.getProyectil().colisionaConEnemigo(enemigos[i])) {
							enemigos[i] = null; // El enemigo desaparece al ser impactado
							renovarEnemigos(); // Renovamos los enemigos para llenar el espacio del enemigo eliminado
						
							princesa.setProyectil(null); // El proyectil desaparece al impactar
							}
						}
					}

					if (princesa.getProyectil() != null){ // Primeros chequeamos que no se haya eliminado por colision
						if (princesa.getProyectil().estaFueraDePantalla(entorno)) { // si sale de la pantalla, lo eliminamos y se vuelve null
							princesa.setProyectil(null);
						}
					}
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
}

		    private void renovarEnemigos() {
        	for (int i = 0; i < this.enemigos.length; i++) {
            	if (this.enemigos[i] != null) {
                	double ex = this.enemigos[i].getX();
                	if (ex < -100 || ex > entorno.ancho() + 100) {
                    	this.enemigos[i] = null;
                	}
            	}
            	if (this.enemigos[i] == null && entorno.numeroDeTick() % 120 == i*15) {
                	this.enemigos[i] = crearEnemigoNuevo();
            	}
        	}
    	}

    	private Enemigo crearEnemigoNuevo() {
        	int direccion;
        	double x;
        	if (Math.random() < 0.5) {
            	direccion = 1;
            	x = -60;
        	} else {
            	direccion = -1;
            	x = entorno.ancho() + 60;
        	}
        	double y = buscarAlturaLibre();
        	return new Enemigo(x, y, 35, 35, 2.0, direccion);
    	}

    	private double buscarAlturaLibre() {
        	double y;
        	boolean mismaAltura;
        	do {
            	y = 60 + Math.random() * 400;
            	mismaAltura = false;
            	for (int i = 0; i < islas.length; i++) {
                	if (islas[i] != null) {
                    	if (Math.abs(y - islas[i].getY()) < 50) {
                        	mismaAltura = true;
                    	}
                	}
            	}
        	} while (mismaAltura);
        		return y;
    	}		
		
		// Procesamiento de un instante de tiempo
		// ...
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
