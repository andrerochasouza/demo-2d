package br.com.andre.core;

import br.com.andre.entities.Enemy;
import br.com.andre.entities.Player;
import br.com.andre.input.KeyManager;
import br.com.andre.map.Map;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;

public class Game extends JPanel implements Runnable {

    // Constantes de tamanho da janela
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String GAME_TITLE = "TOP DOWN DEMO";
    private BufferedImage image;
    private Graphics g;
    private Thread thread;
    private boolean running = false;
    private KeyManager keyManager;
    private GameLoop gameLoop;
    private Map map;
    private Player player;
    private List<Enemy> enemies;

    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();

        keyManager = new KeyManager();
        addKeyListener(keyManager);

        // Inicializar recursos
        init();
    }

    private void init() {
        // Inicializar o buffer de imagem
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = image.getGraphics();

        // Inicializar o mapa
        map = new Map("/images/floor.png", "/images/wall.png");

        // Inicializar o jogador
        player = new Player(100, 100, map, keyManager);

        // Inicializar inimigos
        enemies = new ArrayList<>();
        enemies.add(new Enemy(300, 200, map));
        enemies.add(new Enemy(500, 400, map));
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        gameLoop = new GameLoop(this);
        thread = new Thread(gameLoop);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Atualizar lógica do jogo
    public void update() {
        keyManager.update();
        player.update();
        for (Enemy enemy : enemies) {
            enemy.update();
        }

        // Verificar colisões entre jogador e inimigos
        for (Enemy enemy : enemies) {
            if (player.getBounds().intersects(enemy.getBounds())) {
                System.out.println("Colisão detectada! Implementar lógica de dano ou fim de jogo.");
                // Aqui você pode implementar a lógica de fim de jogo, reduzir a vida do jogador, etc.
            }
        }
    }

    // Renderizar o jogo
    public void render() {
        if (g != null) {
            // Limpar a tela
            g.drawImage(map.getBackground(), 0, 0, WIDTH, HEIGHT, null);

            // Renderizar o mapa
            map.render(g);

            // Renderizar o jogador
            player.render(g);

            // Renderizar inimigos
            for (Enemy enemy : enemies) {
                enemy.render(g);
            }

            // Desenhar o buffer na tela
            Graphics g2 = this.getGraphics();
            if (g2 != null && image != null) {
                g2.drawImage(image, 0, 0, null);
                g2.dispose();
            }
        }
    }

    @Override
    public void run() {
        gameLoop.run();
    }

    public boolean isRunning() {
        return running;
    }
}