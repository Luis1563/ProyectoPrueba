package juego;

import entorno.Entorno;

import java.awt.Color;
import java.awt.Image;
import entorno.Herramientas;

public class Princesa {
    private double x, y;
    private double ancho, alto;
    private double velocidadY;
	private double velocidadX;
    private double velocidadDesplazamiento;
    private double gravedad;
    private double VelocidadMaximaDeCaida;

	private Proyectil proyectil;
	private Vida[] vidas;
    //private Image imagen;
    
    
	public Princesa(double x, double y, double ancho, double alto) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidadY = 0;
		this.velocidadX = 0;
		this.velocidadDesplazamiento = 5;
		this.gravedad = 0.5;
		this.VelocidadMaximaDeCaida = 8;
		this.proyectil = null; // No hay proyectil activo al inicio
		this.vidas = new Vida[]{new Vida(50, 50, 100, 20)}; // Crear una instancia de Vida
		//this.imagen = imagen;
	}

	public void dibujar(Entorno e) {
		e.dibujarRectangulo(x, y, ancho, alto, 0, Color.RED);
	}

	public void moverIzquierda() {
		this.x = this.x -(velocidadDesplazamiento);
	}
	
	public void moverDerecha() {
		this.x = this.x +velocidadDesplazamiento;
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

/*	public boolean colisionaPorIzquierda(Isla[] islas) {
		
		for (int i = 0; i < islas.length; i++) {
            Isla isla = islas[i];
			if (isla != null && bordeIzquierdo() <= isla.bordeDerecho() && bordeDerecho() > isla.bordeDerecho()) {
				if (bordeInferior() > isla.bordeSuperior() && bordeSuperior() < isla.bordeInferior()) {
					return true;				
				}
			}
		}
		return false;
	}
	
	public boolean colisionaPorDerecha(Isla[] islas) {
		
		for (int i = 0; i < islas.length; i++) {
            Isla isla = islas[i];
			if (isla != null && bordeDerecho() >= isla.bordeIzquierdo() && bordeIzquierdo() < isla.bordeIzquierdo()) {
				if (bordeInferior() > isla.bordeSuperior() && bordeSuperior() < isla.bordeInferior()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean colisionaPorAbajo(Isla[] islas) {
		
		for (int i = 0; i < islas.length; i++) {
            Isla isla = islas[i];
			if (isla != null && bordeInferior() >= isla.bordeSuperior() && bordeInferior() < isla.bordeInferior()) {
				if (bordeDerecho() > isla.bordeIzquierdo() && bordeIzquierdo() < isla.bordeDerecho()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean colisionaPorArriba(Isla[] islas) {
		for (int i = 0; i < islas.length; i++) {
            Isla isla = islas[i];
			if (isla != null && bordeSuperior() <= isla.bordeInferior() && bordeSuperior() > isla.bordeSuperior()) {
				if (bordeDerecho() > isla.bordeIzquierdo() && bordeIzquierdo() < isla.bordeDerecho()) {
					return true;
				}
			}
		}
		return false;
	}
*/
	
    
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
    
    
    // Detecta si está sobre una isla (apoyo)
    public boolean estaApoyado(Isla[] islas) {
        double margenDeteccion = 1; // Margen para detectar piso abajo (no se queda pegado a la isla)

        for (int i = 0; i < islas.length; i++) {
            Isla isla = islas[i];
            if (isla != null) {
                // Está apoyado si toca la parte superior de la isla (no desde abajo o dentro)
                if (bordeInferior() >= isla.bordeSuperior() && bordeInferior() <= isla.bordeSuperior() + margenDeteccion &&
                    bordeDerecho() > isla.bordeIzquierdo() && bordeIzquierdo() < isla.bordeDerecho()  &&
                    velocidadY >= 0) { // Solo si no está saltando
                    return true;
                }
            }
        }
        return false;
    }

    // Comprueba si la princesa puede moverse por x e y antes de moverla
    public boolean puedeMover(double x, double y, Isla[] islas) { 
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
    }

	// Aplica gravedad y actualiza posición. altoPantalla es la parte superior de la pantalla para limitar el salto.
	public void actualizarFisica(Isla[] islas, double altoPantalla) {
		// Aplica gravedad
		velocidadY += gravedad;
		if (velocidadY > VelocidadMaximaDeCaida) {
			velocidadY = VelocidadMaximaDeCaida;
		}

		// Calcula nueva posición en y
		double nuevaY = this.y + velocidadY;

		// Limitar por borde superior de la pantalla
		double limiteSuperior = this.alto / 2; //mitad del alto de la princesa para que no se salga
		if (nuevaY - limiteSuperior < 0) {
			// No puede subir más: situar en el borde superior y detener velocidad
			this.y = limiteSuperior;
			velocidadY = 0;
			return;
		}

		// Aplica movimiento vertical si no hay colisión
		if (puedeMover(0, velocidadY, islas)) {
			this.y = nuevaY;
		} else {
			// Colisionó: detener movimiento vertical
			velocidadY = 0;
		}
	}


	public static void dibujarVidas (Entorno entorno, Vida vidas) {

		vidas.dibujar(entorno);
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

	public double getVelocidadX() {
		return velocidadDesplazamiento;
	}

	public void setVelocidadX(double velocidadX) {
		this.velocidadDesplazamiento = velocidadX;
	}

	public Proyectil getProyectil() {
		return proyectil;
	}

	public void setProyectil(Proyectil proyectil) {
		this.proyectil = proyectil;
	}
}
