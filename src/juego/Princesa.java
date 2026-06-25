package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Princesa {
	private Entorno entorno;
    private double x, y, escala;
    private double ancho, alto; 
	private double velocidadY;
	private double velocidadX;
	private double gravedad;
	private double velocidadMaximaDeCaida;
	private Proyectil proyectil;
	private boolean[] vidas;
	private int vidasRestantes;
	private int vidasIniciales;
	private Image imagen;
	private Image imagenVidas;
	private Image imagenVidasGrises;

	public Princesa(double x, double y, double ancho, double alto, int vidasIniciales, Entorno entorno) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.entorno = entorno;
		this.velocidadY = 0;
		this.velocidadX = 5;
		//this.velocidadDesplazamiento = 5;
		this.gravedad = 0.5;
		this.velocidadMaximaDeCaida = 8;
		this.proyectil = null; // No hay proyectil activo al inicio

		this.vidas = new boolean[vidasIniciales];
		for (int i = 0; i < this.vidas.length; i++) {
			this.vidas[i] = true;
		}
		this.vidasRestantes = vidasIniciales;
		//this.imagen = imagen;
	}

	public void dibujar(Entorno e) {
		e.dibujarRectangulo(x, y, ancho, alto, 0, Color.getHSBColor(0.56f, 0.8f, 0.9f));
	}

	public void moverIzquierda() {
		this.x = this.x -(velocidadX);
	}
	
	public void moverDerecha() {
		this.x = this.x +velocidadX;
	}
	
    /*public void moverAbajo() {
        this.y=this.y+5;
    }*/
    /*public void moverArriba() {
        this.y=this.y-5;
    }*/
	public void saltar() { //moverArriba
		//this.y=this.y-5;
        this.velocidadY = -17; // Velocidad de salto negativa (va hacia arriba)
	}
	

	public void disparar(int mouseX, int mouseY) {
		double deltaX = mouseX - this.x;
		double deltaY = mouseY - this.y;
		this.proyectil = new Proyectil(this.x, this.y, 8, deltaX, deltaY); // Crea un nuevo proyectil en la posición de la princesa
	}

	public boolean estaEnlimiteMovimiento(double x, double y) {
		double limiteMovimiento = entorno.ancho() * 0.55; // La princesa puede moverse hasta el 55% del ancho de la pantalla

		if (this.x + x > limiteMovimiento){
			return true;
		}
		return false;
	}




	public boolean colisionaPorIzquierda(Enemigo enemigo) {
		
		if (enemigo != null && bordeIzquierdo() <= enemigo.bordeDerecho() && bordeDerecho() > enemigo.bordeDerecho()) {
			if (bordeInferior() > enemigo.bordeSuperior() && bordeSuperior() < enemigo.bordeInferior()) {
				return true;				
			}
		}
		return false;
	}

	
	public boolean colisionaPorDerecha(Enemigo enemigo) {
		
		if (enemigo != null && bordeDerecho() >= enemigo.bordeIzquierdo() && bordeIzquierdo() < enemigo.bordeIzquierdo()) {
			if (bordeInferior() > enemigo.bordeSuperior() && bordeSuperior() < enemigo.bordeInferior()) {
				return true;
			}
		}
		return false;
	}

	public boolean colisionaPorAbajo(Enemigo enemigo) {
		
		if (enemigo != null && bordeInferior() >= enemigo.bordeSuperior() && bordeInferior() < enemigo.bordeInferior()) {
			if (bordeDerecho() > enemigo.bordeIzquierdo() && bordeIzquierdo() < enemigo.bordeDerecho()) {
				return true;
			}
		}
		return false;
	}


	public boolean colisionaPorArriba(Enemigo enemigo) {
		if (enemigo != null && bordeSuperior() <= enemigo.bordeInferior() && bordeSuperior() > enemigo.bordeSuperior()) {
			if (bordeDerecho() > enemigo.bordeIzquierdo() && bordeIzquierdo() < enemigo.bordeDerecho()) {
				return true;
			}
		}
		return false;
	}


	
    
	public double bordeDerecho() {
        return this.x+this.ancho/2;
	}
	public double bordeIzquierdo() {
        return this.x-this.ancho/2;
	}
	public double bordeInferior() {
        return this.y+this.alto/2;
	}
	public double bordeSuperior() {
        return this.y-this.alto/2;
	}
    
    
    // Detecta si está sobre una isla (apoyo), esto es para decidir si puede saltar o no
    public boolean estaApoyado(Isla[] islas) {

        for (int i = 0; i < islas.length; i++) {
            Isla isla = islas[i];
            if (isla != null) {
                // Está apoyado si toca la parte superior de la isla (no desde abajo o dentro)
                if (bordeInferior() >= isla.bordeSuperior() && bordeInferior() <= isla.bordeSuperior() &&
                    bordeDerecho() > isla.bordeIzquierdo() && bordeIzquierdo() < isla.bordeDerecho() &&
                    velocidadY >= 0) { // Solo si no está saltando
                    return true;
                }
            }
        }
        return false;
    }

    // Comprueba si la princesa puede moverse por x e y antes de moverla
    /*public boolean puedeMover(double x, double y, Isla[] islas) { 
        double izquierda = (this.x + x) - this.ancho / 2;
        double derecha = (this.x + x) + this.ancho / 2;
        double arriba = (this.y + y) - this.alto / 2;
        double abajo = (this.y + y) + this.alto / 2;

        for (int i = 0; i < islas.length; i++) {
            Isla isla = islas[i];
            if (isla != null) {
                //bordes de cada isla
                double iIzquierda = isla.bordeIzquierdo();
                double iDerecha = isla.bordeDerecho();
                double iSuperior = isla.bordeSuperior();
                double iInferior = isla.bordeInferior();

                // Si se superponen, no se puede mover
                if ((derecha <= iIzquierda || izquierda >= iDerecha || abajo <= iSuperior || arriba >= iInferior) == false) {
                    return false;
                }
            }
        }
        return true;
    }*/
    // Comprueba si la princesa puede moverse por x e y antes de moverla
    public boolean puedeMover(double x, double y, Isla[] islas) { 
		//calculamos los bordes de la princesa si se moviera por x e y
        double izquierda = this.bordeIzquierdo() + x;
        double derecha = this.bordeDerecho() + x;
        double arriba = this.bordeSuperior() + y;
        double abajo = this.bordeInferior() + y;

        for (int i = 0; i < islas.length; i++) {
            Isla isla = islas[i];
            if (isla != null) {
                //bordes de cada isla
                double iIzquierda = isla.bordeIzquierdo();
                double iDerecha = isla.bordeDerecho();
                double iSuperior = isla.bordeSuperior();
                double iInferior = isla.bordeInferior();

                // Si se superponen, no se puede mover
                if ((derecha <= iIzquierda || izquierda >= iDerecha || abajo <= iSuperior || arriba >= iInferior) == false) {
                    return false;
                }
            }
        }
        return true;
    }

	// Aplica gravedad y actualiza posición. altoPantalla es la parte superior de la pantalla para limitar el salto.
	public void actualizarFisica(Isla[] islas) {
		// Aplica gravedad
		velocidadY += gravedad;
		if (velocidadY > velocidadMaximaDeCaida) {
			velocidadY = velocidadMaximaDeCaida;
		}

		// Calcula nueva posición en y
		double nuevaY = this.y + velocidadY;

		// Usamos la mitad del alto de la princesa para limitar el salto, porque la posición Y es el centro de la princesa
		double mitadAltoPrincesa = this.alto / 2; //mitad del alto de la princesa
		if (nuevaY - mitadAltoPrincesa < 0) { //si la nueva Y menos la mitad de la princesa es menor a 0 entonces se sale por arriba
			
			// no puede subir más entonces el y tiene que ser el máximo permitido y se detiene la velocidad
			this.y = mitadAltoPrincesa; // esto hace que la parte superior de la princesa quede justo en el límite
			velocidadY = 0;
		}

		// Aplica movimiento vertical si no hay colisión
		if (puedeMover(0, velocidadY, islas)) {
			this.y = nuevaY;
		} else {
			// Colisionó: detener movimiento vertical
			velocidadY = 0;
		}
	}


	public void perderVida() {
		/* if (vidasRestantes <= 0) {
			return;
		}*/
		int indice = -1; // se usa para señalar el ultimo indice que estaba en true, para cambiarlo a false
		for (int i = vidas.length - 1; i >= 0; i--) {
			if (vidas[i] == true && indice == -1) {
				indice = i; // Guardamos el índice del corazón que se perdió
			}
		}
		if (indice != -1) {
			vidas[indice] = false; // Cambia el último corazón en true a false
			vidasRestantes--;
		}
	}

	public boolean estaViva() {
		return vidasRestantes > 0;
	}

	public void reiniciarPosicion(double x, double y) {
		this.x = x;
		this.y = y;
		this.velocidadY = 0;
	}

	public void dibujarVidas(Entorno e) {
		for (int i = 0; i < vidas.length; i++) {
			Color color;
			if (vidas[i] == true) {
				color = Color.RED;
			}
			else {
				color = Color.GRAY;
			}

			double x = 150 + i * 36; //36 porque el ancho del corazón es 30 y puse un espacio de 6 entre ellos
			double y = 30;
			e.dibujarRectangulo(x, y, 30, 30, 0, color);
		}
	}


    
    
	public double getX() {
        return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getAncho() {
		return ancho;
	}
	public void setAncho(double ancho) {
		this.ancho = ancho;
	}
	public double getAlto() {
		return alto;
	}
	public void setAlto(double alto) {
		this.alto = alto;
	}

	/*public double getVelocidadDesplazamiento() {
		return velocidadDesplazamiento;
	}

	public void setVelocidadDesplazamiento(double velocidadDesplazamiento) {
		this.velocidadDesplazamiento = velocidadDesplazamiento;
	}*/

	public double getVelocidadX() {
		return velocidadX;
	}

	public void setVelocidadX(double velocidadX) {
		this.velocidadX = velocidadX;
	}

	public Proyectil getProyectil() {
		return proyectil;
	}

	public void setProyectil(Proyectil proyectil) {
		this.proyectil = proyectil;
	}
}
