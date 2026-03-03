/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package elbueno;

/**
 *
 * @author sorah
 */
public class ASTyBAL {
    
}



/*

package clasenave;

import java.util.Random;
import java.awt.Graphics;
import java.awt.Color;

public class AST {
    private int x, y;
    private double speed;
    private double yr;
    private Random rand;

    public AST(int _x, int _y) {
        this.x = _x;
        this.y = _y;
        this.yr = (double) _y;
        this.speed = 1.0;
        this.rand = new Random();
    }

    // Al igual que tu compañera, el método mover() solo actualiza coordenadas
    public void mover() {
        yr += speed;
        y = (int) yr;
        
        // Si el asteroide sale por abajo (ajustado a píxeles de ventana)
        if (y > 600) { 
            x = rand.nextInt(750) + 30; // Posición aleatoria en X
            y = -20;                    // Aparece arriba de la pantalla
            yr = -20.0;
        }
    }

    // Este es el reemplazo de tu void pintar(), igual que el de la BALA
    public void pintar(Graphics g) {
        g.setColor(Color.RED); // Equivalente a tu setColor(12)
        
        // Dibujamos un círculo relleno (fillOval) para el asteroide
        // Usamos un tamaño de 25x25 píxeles para que sea un blanco justo
        g.fillOval(x, y, 25, 25); 
    }

    // Lógica de colisión con la NAVE
    // Tu compañero integrador usará el getX/getY para el motor de geometría
    public void verificarChoque(ClaseNave nave) {
        // Lógica de proximidad en píxeles
        if (x >= nave.getX() && x < nave.getX() + 40 && y >= nave.getY() && y < nave.getY() + 40) {
            nave.quitarEnergia(); // Equivalente a tu N.HP()
            
            // Reiniciar asteroide tras el choque
            x = rand.nextInt(750) + 30;
            y = -20;
            yr = -20.0;
        }
    }

    public void setSpeed(double s) {
        this.speed = s;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
*/