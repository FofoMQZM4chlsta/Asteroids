package logica;

import java.util.Random;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent; // Necesario para el Timer
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList; // Reemplaza a <list> de C++
import java.util.Iterator;  // Reemplaza a los iteradores itA, itB de C++
import javax.swing.Timer;    // CAMBIO IMPORTANTE: Usamos el Timer de Swing, no el de util.
import javax.swing.JPanel;
import javax.swing.JFrame;   // Para poder probarlo en un main

// --- CLASE ASTEROIDE (Equivalente a la clase AST en C++) ---
public class ObjEsp {
    private int x, y;
    private int speed; // Ahora usamos double para permitir velocidades decimales (1.4, 1.79)
    private Random rand;  // Reemplaza a srand() y rand()

    public ObjEsp(int _x, int _y) {
        this.x = _x;
        this.y = _y;
        this.speed = 5;
        this.rand = new Random();
    }

    public void mover() {
        y += speed;
        if (y > 600) {    // Límite de la pantalla (en C++ era 30, aquí es 600 píxeles)
            x = rand.nextInt(750) + 30; // rand.nextInt(750) es como rand()%750
            y = -20;
        }
    }

    public void pintar(Graphics g) {
        g.setColor(Color.RED); // Reemplaza a setColor(red)
        g.fillOval(x, y, 25, 25); // Reemplaza a printf("*") o caracteres
    }

    public void verificarChoque(Nave nave) {
        // Simulación de colisión por área (Hitbox)
        // En C++ usabas coordenadas de texto, aquí usamos píxeles
        if (x + 20 > nave.getX() && x < nave.getX() + 35 &&
            y + 20 > nave.getY() && y < nave.getY() + 30) {
            nave.BajarHp(); // Llama a la función HP() de tu C++
            x = rand.nextInt(750) + 30;
            y = -20;
        }
    }

    public void setSpeed(int s) { this.speed = s; }
    public int getX() { return x; }
    public int getY() { return y; }
}

// --- CLASE NAVE (Equivalente a NAVE N(20,24,3,3)) ---
class Nave {
    private int x, y, hp, vidas;

    public Nave(int x, int y, int hp, int vidas) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.vidas = vidas;
    }

    // Getters necesarios porque los atributos son private
    public int getX() { return x; }
    public int getY() { return y; }
    public int getVidas() { return vidas; }

    public void BajarHp() { if (hp > 0) hp--; }
    public void sumarVidas(int v) { vidas += v; }

    public void procesarMovimiento(int keyCode) {
        // En Java usamos constantes como KeyEvent.VK_LEFT en lugar de códigos ASCII (72, 80...)
        if (keyCode == KeyEvent.VK_LEFT && x > 10) x -= 10;
        if (keyCode == KeyEvent.VK_RIGHT && x < 750) x += 10;
        if (keyCode == KeyEvent.VK_UP && y > 10) y -= 10;
        if (keyCode == KeyEvent.VK_DOWN && y < 530) y += 10;
    }

    public void pintar(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, 35, 30); // Dibuja un rectángulo en lugar de caracteres "^"
    }

    public void morir() {
        if (hp <= 0) { // Lógica de morir() de C++
            vidas--;
            hp = 3;
        }
    }

    public String obtenerHUD() {
        return String.format("VIDAS %d  HP %d", vidas, hp);
    }
}

// --- CLASE BALA (Tu clase BALA) ---
class Bala {
    private int x, y;

    public Bala(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void mover() { y -= 7; } // Sube 7 píxeles por frame

    public void pintar(Graphics g) {
        g.setColor(Color.YELLOW); // Reemplaza a setColor(14)
        g.fillRect(x, y, 3, 10);  // Reemplaza a printf("|")
    }

    public boolean fuera() { return y <= 0; } // Reemplaza y <= 4 de consola
    public int getX() { return x; }
    public int getY() { return y; }
}

// --- CLASE JUEGO (Equivalente a tu función bool exe()) ---
class Juego {
    private Nave nave;
    private ArrayList<ObjEsp> asteroides; // Reemplaza a list<AST*> A
    private ArrayList<Bala> balas;        // Reemplaza a list<BALA*> B
    private int puntos, nivel;
    private int actSpeed;
    private boolean gameOver;
    private Random rand;

    public Juego() {
        rand = new Random();
        nave = new Nave(400, 500, 3, 3);
        asteroides = new ArrayList<>();
        balas = new ArrayList<>();
        puntos = 0;
        nivel = 1;
        actSpeed = 5;
        gameOver = false;

        // Bucle inicial de creación (for(int i=0; i<6; ++i))
        for (int i = 0; i < 6; i++) {
            asteroides.add(new ObjEsp(rand.nextInt(750) + 30, rand.nextInt(300) - 300));
        }
    }

    public void actualizar(int keyCode) {
        if (gameOver) return;

        // Disparo: si c == 32 (Espacio) en C++
        if (keyCode == KeyEvent.VK_SPACE) {
            balas.add(new Bala(nave.getX() + 15, nave.getY()));
        } else {
            nave.procesarMovimiento(keyCode);
        }

        // Mover balas: reemplaza el for de itB
        Iterator<Bala> itB = balas.iterator();
        while (itB.hasNext()) {
            Bala b = itB.next();
            b.mover();
            if (b.fuera()) itB.remove(); // itB = B.erase(itB) y delete (*itB) se hacen solos
        }

        // Mover asteroides y verificar choque con nave
        for (ObjEsp ast : asteroides) {
            ast.mover();
            ast.verificarChoque(nave);
        }

        // Colisión Bala - Asteroide (El doble for con abs() de C++)
        for (int i = 0; i < asteroides.size(); i++) {
            ObjEsp ast = asteroides.get(i);
            for (int j = 0; j < balas.size(); j++) {
                Bala b = balas.get(j);
                
                // abs(ast->Y() - b->Y()) se traduce igual en Java
                if (Math.abs(ast.getX() - b.getX()) < 25 && Math.abs(ast.getY() - b.getY()) < 25) {
                    balas.remove(j); // Borra bala
                    // En lugar de borrar asteroide y crear otro, lo reseteamos arriba
                    asteroides.set(i, new ObjEsp(rand.nextInt(750) + 30, -20));
                    asteroides.get(i).setSpeed(actSpeed);
                    
                    puntos += 25;
                    subirNivel(); // Lógica de niveles (if puntos >= 100...)
                    break;
                }
            }
        }

        nave.morir();
        if (nave.getVidas() <= 0) gameOver = true;
    }

    private void subirNivel() {
        // Aquí está la lógica de cambio de velocidad y color de C++
        if (puntos >= 100 && nivel == 1) { nivel = 2; actSpeed = 8; }
        else if (puntos >= 200 && nivel == 2) { nivel = 3; actSpeed = 10; }
        
        for (ObjEsp a : asteroides) a.setSpeed(actSpeed);
    }

    // Este método reemplaza a todas las llamadas de pintar() individuales
    public void dibujarTodo(Graphics g) {
        nave.pintar(g);
        for (ObjEsp ast : asteroides) ast.pintar(g);
        for (Bala b : balas) b.pintar(g);
    }

    public int getPuntos() { return puntos; }
    public int getNivel() { return nivel; }
    public boolean isGameOver() { return gameOver; }
    public Nave getNave() { return nave; }
}

// JPanel es el área de dibujo. 
// ActionListener permite que el Timer funcione. 
// KeyListener reemplaza a kbhit() y getch() para detectar teclado.
class VentanaJuego extends JPanel implements ActionListener, KeyListener {
    private Juego juego;   // Contenedor de la lógica (Nave, Asteroides, Balas)
    private Timer timer;   // EL CAMBIO CLAVE: Reemplaza al bucle while() y al Sleep()

    public VentanaJuego() {
        juego = new Juego(); 
        setFocusable(true);     // Permite que este panel "escuche" el teclado
        addKeyListener(this);   // Conecta los eventos de tecla con este panel
        
        // En C++ hacías Sleep(30). 
        // Aquí el Timer dice: "Cada 30ms ejecuta el método actionPerformed".
        timer = new Timer(30, this); 
        timer.start();          // Inicia el flujo constante del juego
    }

    // C++: system("cls") + pintar()
    // Este método se llama automáticamente cada vez que pedimos redibujar.
    @Override
    protected void paintComponent(Graphics g) {
        // super.paintComponent(g) limpia lo que se dibujó antes (reemplaza system("cls"))
        super.paintComponent(g); 
        
        // Fondo: En consola era por defecto, aquí pintamos un rectángulo negro total
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Llamamos al método que recorre todas las listas (balas, asteroides) y las pinta
        juego.dibujarTodo(g);

        // HUD: Dibujamos la información de puntos y HP
        g.setColor(Color.WHITE);
        g.drawString("Puntos: " + juego.getPuntos() + "  Nivel: " + juego.getNivel(), 10, 20);
        g.drawString(juego.getNave().obtenerHUD(), 10, 40);

        // Lógica de Fin de Juego (Sustituye al printf final del main de C++)
        if (juego.isGameOver()) {
            g.setColor(Color.RED);
            g.setFont(new Font("Monospaced", Font.BOLD, 40));
            g.drawString("GAME OVER", 280, 250);
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Puntaje Final: " + juego.getPuntos(), 310, 300);
            g.drawString("Presiona 'R' para Reintentar", 270, 350);
        }
    }

    // C++: El interior del while(jugar)
    // Este método es el "latido" del juego.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!juego.isGameOver()) {
            // Actualiza posiciones de todo (mover asteroides, verificar choques)
            juego.actualizar(-1); 
        }
        // repaint() le dice a Java: "Vuelve a llamar a paintComponent para mostrar los cambios"
        repaint(); 
    }

    // C++: if(kbhit()) { c = getch(); }
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode(); // Obtenemos el código de la tecla presionada
        
        // Si el usuario presiona 'R' cuando perdió, reiniciamos la lógica
        if (juego.isGameOver() && key == KeyEvent.VK_R) {
            juego = new Juego(); 
        } else {
            // Pasamos la tecla a la clase Juego para mover la nave o disparar
            juego.actualizar(key); 
        }
    }

    // Métodos vacíos obligatorios (Java los pide aunque no se usen)
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}