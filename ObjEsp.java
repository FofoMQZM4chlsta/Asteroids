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

// ---Clase  Jefe---
class Jefe {
    private int x, y, vida, speedX, vidaMaxima;
    private boolean activo;

    public Jefe(int nivel) {
        this.x = 350;
        this.y = -100; // Aparece desde arriba
        this.vida = nivel * 10; // Nivel 2 = 20 disparos, Nivel 3 = 30
        this.vidaMaxima = this.vida; // Guardamos el total inicial
        this.speedX = 5;
        this.activo = false;
    }

    public void aparecer() { activo = true; }

public void mover() {
        if (!activo) return;
        if (y < 80) y += 2;
        x += speedX;
        if (x <= 50 || x >= 700) speedX *= -1;
    }

public void pintar(Graphics g) {
        if (!activo) return;
        
        // Cuerpo del Jefe
        g.setColor(Color.MAGENTA);
        g.fillRoundRect(x, y, 100, 80, 20, 20);
        
        // --- BARRA DE VIDA CORREGIDA ---
        g.setColor(Color.RED); // Fondo rojo (daño)
        g.fillRect(x, y - 20, 100, 10);
        
        g.setColor(Color.GREEN); // Parte verde (vida actual)
        // Regla de 3: (vida actual * ancho total) / vida inicial
        int anchoVida = (vida * 100) / vidaMaxima; 
        g.fillRect(x, y - 20, anchoVida, 10);
    }
public void recibirDanio() { if(vida > 0) vida--; }
    public int getVida() { return vida; }
    public boolean isActivo() { return activo; }
    public int getX() { return x; }
    public int getY() { return y; }
}


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
    // 1. Pies (Rojos/Rosa fuerte)
    g.setColor(new Color(255, 0, 102));
   // Usamos fillOval para el cuerpo redondo.
    g.fillOval(x - 5, y + 15, 15, 10); // Pie izquierdo
    g.fillOval(x + 25, y + 15, 15, 10); // Pie derecho

    // 2. Cuerpo (Rosa claro)
    g.setColor(new Color(255, 182, 193));
    g.fillOval(x, y, 35, 30);

    // 3. Mejillas (Rosa un poco más fuerte)
    g.setColor(new Color(255, 105, 180));
    g.fillOval(x + 5, y + 15, 8, 5);  // Mejilla izq
    g.fillOval(x + 22, y + 15, 8, 5); // Mejilla der

    // 4. Ojos (Negros y alargados)
    g.setColor(Color.BLACK);
    g.fillOval(x + 10, y + 8, 4, 8);  // Ojo izq
    g.fillOval(x + 21, y + 8, 4, 8);  // Ojo der
    
    // Brillo de los ojos (Blanco)
    g.setColor(Color.WHITE);
    g.fillOval(x + 11, y + 9, 2, 3);
    g.fillOval(x + 22, y + 9, 2, 3);

    // 5. Boca (Pequeña línea o arco)
    g.setColor(new Color(150, 0, 0));
    g.drawArc(x + 14, y + 18, 7, 5, 0, -180); 
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

    public void mover() { y -= 7; }

    public void pintar(Graphics g) {
        g.setColor(Color.YELLOW);
        
        // Coordenadas para una estrella de 5 puntas
        int[] vx = {x, x+3, x+10, x+4, x+6, x, x-6, x-4, x-10, x-3};
        int[] vy = {y-10, y-3, y-3, y+2, y+10, y+5, y+10, y+2, y-3, y-3};
        
        g.fillPolygon(vx, vy, 10);
        
        // Brillo opcional
        g.setColor(Color.WHITE);
        g.fillOval(x-2, y-2, 4, 4);
    }

    public boolean fuera() { return y <= 0; }
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
    private ArrayList<EstrellaFondo> estrellasDecorativas; // Nueva lista
    private Jefe jefeActual; //Nuevas variables para el jefe
    private boolean modoJefe = false;
    
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
        estrellasDecorativas = new ArrayList<>();
        for (int i = 0; i < 50; i++) { // Creamos 50 estrellas de fondo
            estrellasDecorativas.add(new EstrellaFondo());
        }
    }
    public void manejarInput(int key) {
        if (gameOver) return;
        
        if (key == KeyEvent.VK_SPACE) {
            // Creamos la bala estrella justo en la posición de Kirby
            balas.add(new Bala(nave.getX() + 15, nave.getY()));
        } else {
            // Movemos a Kirby
            nave.procesarMovimiento(key);
        }
    }

public void actualizar() { // Quitamos el keyCode de aquí
    if (gameOver) return;
    
    // Mover el fondo primero
        for (EstrellaFondo ef : estrellasDecorativas) {
            ef.mover();
        }
        //Movimento del jefe
        if (modoJefe && jefeActual != null) {
        jefeActual.mover();
    }
        // Mover asteroides (solo si no estamos en modo jefe, para ahorrar recursos)
    if (!modoJefe) {
        for (ObjEsp ast : asteroides) {
            ast.mover();
            ast.verificarChoque(nave);
        }
    }

    // Mover balas
    Iterator<Bala> itB = balas.iterator();
    while (itB.hasNext()) {
        Bala b = itB.next();
        b.mover();
        if (b.fuera()) itB.remove();
    }
    // Mover asteroides
    for (ObjEsp ast : asteroides) {
        ast.mover();
        ast.verificarChoque(nave);
    }

// 3. Verificar colisiones entre estrellas y asteroides
        verificarColisionesBalas();

    nave.morir();
    if (nave.getVidas() <= 0) gameOver = true;
}

private void verificarColisionesBalas() {
    // CASO A: Estamos en batalla contra un Jefe
    if (modoJefe && jefeActual != null && jefeActual.isActivo()) {
        Iterator<Bala> itB = balas.iterator();
        while (itB.hasNext()) {
            Bala b = itB.next();
            
            // Hitbox del Jefe (100x80 píxeles)
            if (b.getX() > jefeActual.getX() && b.getX() < jefeActual.getX() + 100 &&
                b.getY() > jefeActual.getY() && b.getY() < jefeActual.getY() + 80) {
                
                jefeActual.recibirDanio(); // Le restamos 1 de vida
                itB.remove();             // Eliminamos la estrella de Kirby
                
                // Si derrotamos al jefe
                if (jefeActual.getVida() <= 0) {
                    puntos += 500;
                    modoJefe = false;      // Desactivamos modo jefe
                    
                    // Volver a poblar el espacio con asteroides
                    for (int i = 0; i < 6; i++) {
                        asteroides.add(new ObjEsp(rand.nextInt(750) + 30, -100));
                    }
                }
            }
        }
    } 
    // CASO B: Juego normal (Asteroides)
    else {
        // Usamos un bucle tradicional para los asteroides para poder usar índices
        for (int i = 0; i < asteroides.size(); i++) {
            ObjEsp ast = asteroides.get(i);
            
            // Usamos un Iterator para las balas para evitar errores al borrar mientras recorremos
            Iterator<Bala> itB = balas.iterator();
            while (itB.hasNext()) {
                Bala b = itB.next();
                
                // Lógica de colisión por distancia (Hitbox circular)
                // Si la distancia en X e Y es menor a 25 píxeles, hay impacto
                if (Math.abs(ast.getX() - b.getX()) < 25 && Math.abs(ast.getY() - b.getY()) < 25) {
                    
                    itB.remove(); // Borramos la estrella que impactó
                    
                    // Respawn del asteroide arriba con la velocidad actual del nivel
                    asteroides.set(i, new ObjEsp(rand.nextInt(750) + 30, -20));
                    asteroides.get(i).setSpeed(actSpeed);
                    
                    puntos += 25;  // Sumamos puntaje normal
                    subirNivel();  // Verificamos si este puntaje nos lleva al siguiente Jefe
                    break;         // Salimos del bucle de balas para procesar el siguiente asteroide
                }
            }
        }
    }
}


private void subirNivel() {
    // Solo entramos al modo jefe si NO estamos ya en modo jefe
    if (!modoJefe) {
        if (puntos >= 100 && nivel == 1) {
            nivel = 2;
            iniciarBatallaJefe();
        } else if (puntos >= 300 && nivel == 2) {
            nivel = 3;
            iniciarBatallaJefe();
        }
        // Actualizar velocidad solo para asteroides normales
        for (ObjEsp a : asteroides) a.setSpeed(actSpeed);
    }
}
    //Metodo para el jefe
    private void iniciarBatallaJefe() {
    modoJefe = true;
    jefeActual = new Jefe(nivel);
    jefeActual.aparecer();
    // Limpiamos asteroides para que no estorben
    asteroides.clear(); 
}

    // Este método reemplaza a todas las llamadas de pintar() individuales
    public void dibujarTodo(Graphics g) {
        nave.pintar(g);
        //Primero el fondo y luego lo demas
        for (EstrellaFondo ef : estrellasDecorativas) {
            ef.pintar(g);
        }
        for (ObjEsp ast : asteroides) ast.pintar(g);
        for (Bala b : balas) b.pintar(g);
        // 3. NUEVO: Dibujar al Jefe
    if (modoJefe && jefeActual != null) {
        jefeActual.pintar(g);
    }
    
    }

    public int getPuntos() { return puntos; }
    public int getNivel() { return nivel; }
    public boolean isGameOver() { return gameOver; }
    public Nave getNave() { return nave; }
}
// Clase para el fondo estrellado (Efecto Parallax)
class EstrellaFondo {
    private int x, y, velocidad;
    private Random rand = new Random();

    public EstrellaFondo() {
        this.x = rand.nextInt(800);
        this.y = rand.nextInt(600);
        this.velocidad = rand.nextInt(2) + 1; // Se mueven muy lento (1 o 2 px)
    }

    public void mover() {
        y += velocidad;
        if (y > 600) {
            y = -10;
            x = rand.nextInt(800);
        }
    }

    public void pintar(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, 2, 2); // Estrellitas pequeñas de 2x2 píxeles
    }
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
        // LLAMADA AL MOTOR FÍSICO:
        // Se ejecuta cada 30ms gracias al Timer. 
        // Ya no le pasamos el "key" para que no se acelere.
        juego.actualizar(); 
    }
    repaint(); 
}

    // C++: if(kbhit()) { c = getch(); }
@Override //Esta etiqueta le dice a Java: "Sé que tú ya tienes un método para pintar o detectar teclas, pero ignora el tuyo y usa el mío".
public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode(); 
    
    if (juego.isGameOver() && key == KeyEvent.VK_R) {
        juego = new Juego(); 
    } else {
        // LLAMADA AL INPUT:
        // Aquí solo avisamos que se presionó una tecla.
        juego.manejarInput(key); 
    }
}

    // Métodos vacíos obligatorios (Java los pide aunque no se usen)
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}