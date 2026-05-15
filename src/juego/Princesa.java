package juego;

import entorno.Entorno;

import java.awt.Color;
import java.awt.Image;
import entorno.Herramientas;

public class Princesa {
    private double x, y;
    private double ancho, alto; 
    //private Image imagen;
    
    
	public Princesa(double x, double y, double ancho, double alto) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		//this.imagen = imagen;
	}
	public void dibujar(Entorno e) {
		e.dibujarRectangulo(x, y, ancho, alto, 0, Color.RED);
	}

	public void mover(Entorno e) {
        if (e.estaPresionada(e.TECLA_DERECHA)) { this.x += 3; }
        if (e.estaPresionada(e.TECLA_IZQUIERDA)) { this.x -= 3; }

       /* if (e.sePresiono(e.TECLA_ARRIBA) && !enElAire) {
            this.velocidadSalto = -12;
            this.enElAire = true;
        }

        // Aplicar Gravedad
        if (enElAire) {
            this.y += velocidadSalto;
            velocidadSalto += 0.4; // Aceleración de gravedad
        }*/
    }

}
