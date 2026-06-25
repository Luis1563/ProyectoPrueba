package juego;

import entorno.Entorno;
import java.awt.Color;
import java.awt.Image;
import entorno.Herramientas;

public class Enemigo {

    private double x;
    private double y;
    private double ancho;
    private double alto;
    private double velocidad = 0;
    private int direccion; // 1 derecha, -1 izquierda
    //private Image imagen;

    public Enemigo(double x, double y, double ancho, double alto, double velocidad, int direccion) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.velocidad = velocidad;
        this.direccion = direccion;
        //this.imagen = imagen;
    }

    public void dibujar(Entorno e) {
        e.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.BLUE);
    }

    public void mover() {
        this.x += this.velocidad * this.direccion; // Mueve horizontalmente
    }

    public void actualizar(double velocidadMapa) {
        this.mover();
        this.x = this.x - velocidadMapa;
    }


    public double bordeDerecho() { 
        return this.x + this.ancho / 2; 
    }

    public double bordeIzquierdo() {
        return this.x - this.ancho / 2; 
    }

    public double bordeInferior() {
        return this.y + this.alto / 2; 
    }

    public double bordeSuperior() { 
        return this.y - this.alto / 2; 
    }
    

    public boolean colisionaConIsla(Isla isla) {
        return this.bordeDerecho() >= isla.bordeIzquierdo() && 
        this.bordeIzquierdo() <= isla.bordeDerecho() && 
        this.bordeInferior() >= isla.bordeSuperior() && 
        this.bordeSuperior() <= isla.bordeInferior();
    }

    public boolean colisionaConPrincesa(Princesa p) {
        return this.bordeDerecho() >= p.bordeIzquierdo() && 
        this.bordeIzquierdo() <= p.bordeDerecho() && 
        this.bordeInferior() >= p.bordeSuperior() && 
        this.bordeSuperior() <= p.bordeInferior();
    }


    
    
    public void setX(double x) { 
        this.x = x; 
    }
    public double getX() {
        return x; 
    }

    public int getDireccion(){
        return direccion;
    }

    public double getY() {
        return y; 
    }

}
