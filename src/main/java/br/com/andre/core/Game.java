package br.com.andre.core;

import br.com.andre.camera.Camera;
import br.com.andre.entities.Enemy;
import br.com.andre.entities.Player;
import br.com.andre.input.KeyManager;
import br.com.andre.map.Map;
import br.com.andre.map.MapStructure;
import br.com.andre.map.TileDefinition;
import br.com.andre.portal.PortalManager;

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
    public static final String GAME_TITLE = "GAME TOP DOWN";

    // Buffer de imagem para renderização
    private BufferedImage image;
    private Graphics g;

    // Thread do jogo
    private Thread thread;
    private boolean running = false;

    // Input
    private KeyManager keyManager;

    // Game Loop
    private GameLoop gameLoop;

    // Mapa
    private Map map;

    // Entidades
    private Player player;
    private List<Enemy> enemies;

    // Câmera
    private Camera camera;

    // Portal Manager
    private PortalManager portalManager;

    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
        setDoubleBuffered(true); // Habilitar double buffering
        keyManager = new KeyManager();
        addKeyListener(keyManager);
        init();
    }

    private void init() {
        // Inicializar o buffer de imagem
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = image.getGraphics();

        // Inicializar o mapa a partir do arquivo de estrutura
        map = new Map("/maps/struct.txt");

        // Definir o mapa inicial
        map.setCurrentMap("Maze"); // Você pode mudar para "Forest" ou outro mapa definido

        // Inicializar a câmera
        camera = new Camera(WIDTH, HEIGHT, map);

        // Inicializar o PortalManager
        portalManager = new PortalManager(map);

        // Inicializar o jogador
        // Localização inicial do jogador (em pixels)
        int playerStartX = 100;
        int playerStartY = 100;
        player = new Player(playerStartX, playerStartY, map, keyManager);

        // Inicializar inimigos com base no mapa
        enemies = new ArrayList<>();
        initializeEnemies();
    }

    private void initializeEnemies() {
        enemies.clear();
        MapStructure currentMap = map.getCurrentMap();
        if (currentMap == null) return;

        int[][] grid = currentMap.getGrid();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                int tileNumber = grid[y][x];
                TileDefinition tileDef = currentMap.getTileDefinition(tileNumber);
                if (tileDef != null && tileDef.isEnemy()) {
                    // Criar inimigo na posição central do tile
                    int enemyX = x * map.getTileSize() + (map.getTileSize() - 32) / 2;
                    int enemyY = y * map.getTileSize() + (map.getTileSize() - 32) / 2;
                    String path = "/images/" + tileDef.getSpriteName() + ".png";
                    enemies.add(new Enemy(enemyX, enemyY, map, path));
                }
            }
        }
    }

    // Método para trocar de mapa
    public void switchMap(String mapName) {
        map.setCurrentMap(mapName);
        // Reposicionar o jogador conforme necessário
        player.setPosition(100, 100); // Exemplo: reposicionar no início
        // Re-inicializar inimigos
        initializeEnemies();
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

        // Atualizar a câmera para seguir o jogador
        camera.update(player);

        // Verificar colisões entre jogador e inimigos
        for (Enemy enemy : enemies) {
            if (player.getBounds().intersects(enemy.getBounds())) {
                System.out.println("Colisão detectada! Implementar lógica de dano ou fim de jogo.");
                // Implementar lógica de dano ou fim de jogo
            }
        }

        // Verificar se o jogador entrou em um portal
        portalManager.checkPortals(player);
    }

    // Renderizar o jogo
    public void render() {
        if (g != null) {
            // Limpar a tela
            g.clearRect(0, 0, WIDTH, HEIGHT);

            // Renderizar o mapa com a câmera
            map.render(g, camera);

            // Renderizar o jogador
            player.render(g, camera);

            // Renderizar inimigos
            for (Enemy enemy : enemies) {
                enemy.render(g, camera);
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