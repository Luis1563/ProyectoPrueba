package juego;

import entorno.Entorno;
import java.awt.Image;
import entorno.Herramientas;

public class Enemigo {

    private double x, y;
    private double ancho, alto;
    private double velocidadY = 0;
    //private Image imagen;

    public Enemigo(double x, double y, double ancho, double alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.velocidadY = 0;
        //this.imagen = imagen;
    }

}
