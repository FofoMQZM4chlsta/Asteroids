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

class Cohete {
    private int x, y;
    public Cohete(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void mover() { y += 6; } // Cae hacia Kirby
    public void pintar(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRoundRect(x, y, 10, 20, 5, 5); // Cuerpo del cohete
        
        // CORRECCIÓN AQUÍ:
        g.setColor(Color.RED);
        g.fillPolygon(new int[]{x, x + 5, x + 10}, new int[]{y + 20, y + 30, y + 20}, 3); 
    }
    public boolean fuera() { return y > 600; }
    public int getX() { return x; }
    public int getY() { return y; }
}

// ---Clase  Jefe---
class Jefe {
    private int x, y, vida, speedX, vidaMaxima;
    private double angulo = 0; // Para el movimiento ondulado
    private boolean activo;
    private int timerDisparo = 0;
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
        
        // 1. Entrada suave desde arriba
        if (y < 60) y += 1;
        
        // 2. Movimiento Horizontal
        x += speedX;
        if (x <= 30 || x >= 670) speedX *= -1;

        // 3. MOVIMIENTO NO LINEAL (Flotar en ondas)
        // Usamos Math.sin para que suba y baje suavemente
        angulo += 0.05;
        y = 60 + (int)(Math.sin(angulo) * 20); 
    }
    // Método para saber si debe disparar (cada 60 ciclos aprox)
    public boolean debeDisparar() {
        if (!activo) return false;
        timerDisparo++;
        if (timerDisparo >= 50) { // Ajusta este número para más o menos dificultad
            timerDisparo = 0;
            return true;
        }
        return false;
    }

public void pintar(Graphics g) {
    if (!activo) return;
    // --- BARRA DE VIDA MEJORADA (Mantenemos tu lógica original) ---
    // La subimos un poco (y - 30) para que no choque con el halo
    g.setColor(Color.RED); 
    g.fillRect(x, y - 30, 100, 10);
   
    g.setColor(Color.GREEN); 
    // Regla de 3 para calcular el ancho de la vida
    int anchoVida = (vida * 100) / vidaMaxima; 
    g.fillRect(x, y - 30, anchoVida, 10);
    
    // Borde blanco para la barra de vida
    g.setColor(Color.WHITE);
    g.drawRect(x, y - 30, 100, 10);

    // --- DISEÑO DE KING DEDEDE (Basado en la imagen proporcionada) ---
    // Usamos variables locales para que sea más claro dibujar todo centrado en (x,y)
    int width = 100;
    int height = 80;
    
    // 1. Halo (Aro superior en gris y blanco)
    g.setColor(Color.WHITE);
    g.fillOval(x + width/2 - 15, y - 10, 30, 15);
    g.setColor(Color.GRAY);
    g.drawOval(x + width/2 - 15, y - 10, 30, 15);

    // 2. Parte superior de la cabeza (Redonda y Roja)
    g.setColor(new Color(220, 0, 0)); // Rojo sólido
    g.fillArc(x, y, width, 50, 0, 180);

    // 3. Banda de ojos (Onda Azul)
    g.setColor(new Color(0, 140, 255)); // Azul brillante
    g.fillRect(x, y + 20, width, 15);
    g.fillOval(x + 10, y + 30, 80, 10);

    // 4. Mandíbula y Pico (Onda Naranja/Amarilla)
    g.setColor(new Color(255, 170, 0)); // Naranja brillante
    g.fillRoundRect(x + 10, y + 35, 80, 45, 20, 20); // Mandíbula redondeada

    // 5. Cresta Central (Forma de gota Amarilla)
    g.setColor(new Color(255, 220, 0)); // Amarillo vibrante
    g.fillOval(x + width/2 - 20, y - 5, 40, 35); // La parte superior de la gota
    g.fillRect(x + width/2 - 15, y + 15, 30, 20); // El cuerpo de la gota

    // 6. Detalles de la cresta (Línea central)
    g.setColor(Color.BLACK);
    g.drawLine(x + width/2, y - 5, x + width/2, y + 25);
    
    // 7. Línea de separación del pico (Naranja más oscuro)
    g.setColor(new Color(200, 120, 0));
    g.drawLine(x + width/2 - 20, y + 60, x + width/2 + 20, y + 60);

    // 8. Bordes generales (Negro sólido, igual que en la imagen)
    g.setColor(Color.BLACK);
    g.drawArc(x, y, width, 50, 0, 180); // Borde rojo
    g.drawRect(x, y + 20, width, 15); // Borde azul
    g.drawRoundRect(x + 10, y + 35, 80, 45, 20, 20); // Borde mandíbula
    g.drawOval(x + width/2 - 20, y - 5, 40, 35); // Borde cresta
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
    // Cuerpo principal del asteroide (Gris oscuro/marrón)
    g.setColor(new Color(105, 105, 105)); 
    g.fillOval(x, y, 28, 26); // Un poco más irregular que un círculo perfecto

    // Borde para dar volumen
    g.setColor(Color.BLACK);
    g.drawOval(x, y, 28, 26);

    // --- DETALLES: CRÁTERES ---
    g.setColor(new Color(60, 60, 60)); // Gris más oscuro
    // Cráter 1
    g.fillOval(x + 5, y + 6, 6, 6);
    // Cráter 2
    g.fillOval(x + 15, y + 12, 8, 8);
    // Cráter 3
    g.fillOval(x + 8, y + 18, 4, 4);
    
    // Un pequeño brillo para que no se vea plano
    g.setColor(new Color(150, 150, 150, 100)); // Blanco transparente
    g.drawArc(x + 3, y + 3, 20, 20, 100, 50);
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
    private ArrayList<Cohete> cohetesJefe = new ArrayList<>();
    
    public boolean isModoJefe() { return modoJefe; }
    
    public Juego() {
        rand = new Random();
        nave = new Nave(400, 500, 3, 3);
        asteroides = new ArrayList<>();
        balas = new ArrayList<>();
        puntos = 0;
        nivel = 1;
        actSpeed = 5;
        gameOver = false;
        
        Musicachida.Musiquita();
        

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
        // NUEVO: El jefe dispara
        if (jefeActual.debeDisparar()) {
            cohetesJefe.add(new Cohete(jefeActual.getX() + 45, jefeActual.getY() + 70));
        }
        // NUEVO: Mover cohetes y ver si golpean a Kirby
        Iterator<Cohete> itC = cohetesJefe.iterator();
        while (itC.hasNext()) {
            Cohete c = itC.next();
            c.mover();
            
            // Colisión cohete -> Nave
            if (c.getX() > nave.getX() && c.getX() < nave.getX() + 35 &&
                c.getY() > nave.getY() && c.getY() < nave.getY() + 30) {
                nave.BajarHp();
                itC.remove();
            } else if (c.fuera()) {
                itC.remove();
            }
        }
        
        if (nave.getX() + 30 > jefeActual.getX() && nave.getX() < jefeActual.getX() + 100 &&
        nave.getY() + 25 > jefeActual.getY() && nave.getY() < jefeActual.getY() + 80) {
        
        nave.BajarHp(); // Le quita 1 vida cada que lo toca
        
        // --- SOLUCIÓN: EMPUJAR A KIRBY ---
        // Lo mandamos 50 píxeles hacia abajo para sacarlo de la colisión
        // Así el próximo "latido" del Timer (30ms después) ya no estará tocando al jefe
        nave.procesarMovimiento(KeyEvent.VK_DOWN); 
        nave.procesarMovimiento(KeyEvent.VK_DOWN);
        nave.procesarMovimiento(KeyEvent.VK_DOWN);
        nave.procesarMovimiento(KeyEvent.VK_DOWN);
        nave.procesarMovimiento(KeyEvent.VK_DOWN);
        
        /*
        Usa lo que ya tenemis utilizamoss el método 'procesarMovimiento' que ya existe enla clase Nave.
        Evita el daño continuo Al desplazar la coordenada y de la nave bruscamente hacia abajo, la condición de colision (if) 
        dejará de cumplirse en el siguiente ciclo del Timer, dando oportunidad de reaccionar.
        */
        }
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
                    balas.clear(); // <--- AÑADE ESTO: Borra las balas activas para evitar el lag visual
                    cohetesJefe.clear(); // Borra también los cohetes que quedaron en el aire
                    Musicachida.nomusiquita();
                    Musicachida.Musiquita();
                    
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
        } else if (puntos >= 700 && nivel == 2) {
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
    Musicachida.nomusiquita();
    Musicachida.MusiquitaDedede();
    jefeActual = new Jefe(nivel);
    jefeActual.aparecer();
    // Limpiamos asteroides para que no estorben
    asteroides.clear(); 
}

    // Este método reemplaza a todas las llamadas de pintar() individuales
public void dibujarTodo(Graphics g) {
    // Pasamos el estado de modoJefe a las estrellas
    for (EstrellaFondo ef : estrellasDecorativas) {
        ef.pintar(g, modoJefe); 
    }
    
    nave.pintar(g);
    if (!modoJefe) {
        for (ObjEsp ast : asteroides) ast.pintar(g);
    }
    for (Bala b : balas) b.pintar(g);
    if (modoJefe && jefeActual != null) {
        jefeActual.pintar(g);
        for (Cohete c : cohetesJefe) c.pintar(g);
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
    public void pintar(Graphics g, boolean esPasto) {
        if (esPasto) {
            // En modo jefe, las estrellas parecen pequeñas flores o motas de polen
            g.setColor(new Color(255, 255, 255, 150)); 
            g.fillOval(x, y, 3, 3);
            g.setColor(new Color(255, 200, 0));
            g.fillOval(x + 1, y + 1, 1, 1);
        } else {
            // Fondo espacial normal
            g.setColor(Color.WHITE);
            g.fillOval(x, y, 2, 2);
        }
    }

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
// CAMBIO DINÁMICO DE FONDO
    if (juego.isModoJefe()) {
        g.setColor(new Color(100, 200, 50)); // Verde pasto/pradera
    } else {
        g.setColor(Color.BLACK); // Espacio normal
    }
    g.fillRect(0, 0, getWidth(), getHeight());
        // Llamamos al método que recorre todas las listas (balas, asteroides) y las pinta
        juego.dibujarTodo(g);

        // HUD: Dibujamos la información de puntos y HP
        g.setColor(Color.WHITE);
        g.drawString("Puntos: " + juego.getPuntos() + "  Nivel: " + juego.getNivel(), 10, 20);
        g.drawString(juego.getNave().obtenerHUD(), 10, 40);

        // Lógica de Fin de Juego (Sustituye al printf final del main de C++)
        if (juego.isGameOver()) {
            Musicachida.nomusiquita();
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