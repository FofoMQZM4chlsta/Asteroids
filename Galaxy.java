package logica;
import javax.swing.JFrame;// Librería para crear ventanas nativas
import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList; // Sustituye a <list>
import java.util.Scanner;    // Sustituye a <stdio.h>
import java.lang.Math;       // Sustituye a <cmath>
import java.awt.event.KeyEvent;


public class Galaxy {
    // Definimos las constantes (Sustituyen a los #define de C++)
    // Usamos los códigos estándar de Java (VK = Virtual Key)
    public static final int ARRIBA = KeyEvent.VK_UP;    // 38
    public static final int ABAJO = KeyEvent.VK_DOWN;   // 40
    public static final int IZQUIERDA = KeyEvent.VK_LEFT; // 37
    public static final int DERECHA = KeyEvent.VK_RIGHT; // 39
    
    public void pintarLimites(Graphics g) {
    g.setColor(Color.WHITE); // Color de los bordes
    
    // En C++ usabas de x=2 a 97 y y=3 a 30. 
    // En píxeles (suponiendo escala de 10), sería algo como:
    int x1 = 20, y1 = 30;
    int ancho = 950; // Ajusta según tu ventana
    int alto = 300;
    
    // Dibujar el rectángulo de los límites (Sustituye a todos tus bucles for)
    g.drawRect(x1, y1, ancho, alto); 
    
    // Si quieres que las líneas sean más gruesas (como el doble borde %c 205)
    g.drawRect(x1 - 1, y1 - 1, ancho + 2, alto + 2);
}
    
    public void limpiarPantallaArea(Graphics g, int x, int y, int ancho, int alto) {
    g.setColor(Color.BLACK);
    g.fillRect(x, y, ancho, alto);
}
    
    public static void main(String[] args) {
        // C++: asteroids() y system("pause")
        // En Java, la interfaz gráfica se lanza creando un objeto JFrame.
        JFrame ventana = new JFrame("Asteroides 2D - Java Edition");
        
        // Creamos la instancia de la pantalla (el lienzo de dibujo)
        VentanaJuego panelDeJuego = new VentanaJuego();
        
        // Configuración de la ventana (reemplaza los comandos de consola)
        ventana.add(panelDeJuego);          // Metemos el juego dentro de la ventana
        ventana.setSize(800, 600);          // Definimos resolución en píxeles (no en caracteres)
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Al cerrar la ventana, el proceso termina
        ventana.setLocationRelativeTo(null); // Centra la ventana en el monitor
        ventana.setResizable(false);        // Evita que el usuario deforme el área de juego
        ventana.setVisible(true);           // Hace que la ventana aparezca (como quitar el "OcultarCursor")
    }
}    


