package br.com.andre.core;

import br.com.andre.camera.Camera;
import br.com.andre.entities.Enemy;
import br.com.andre.entities.NPC;
import br.com.andre.entities.Player;
import br.com.andre.input.KeyManager;
import br.com.andre.map.MapLoader;
import br.com.andre.map.MapStructure;
import br.com.andre.map.GameMap;
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
    private GameMap gameMap;

    // Entidades
    private Player player;
    private List<Enemy> enemies = new ArrayList<>();
    private List<NPC> npcs = new ArrayList<>();

    // Câmera
    private Camera camera;

    // Portal Manager
    private PortalManager portalManager;

    // MapLoader
    private MapLoader mapLoader;

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

        // Inicializar o MapLoader com struct.txt e npc.txt
        mapLoader = new MapLoader("/maps/struct.txt", "/maps/npc.txt");

        // Inicializar a câmera
        camera = new Camera(WIDTH, HEIGHT, gameMap);

        // Inicializar o PortalManager
        portalManager = new PortalManager(gameMap, this);

        // Inicializar o jogador
        int playerStartX = 100;
        int playerStartY = 100;
        player = new Player(playerStartX, playerStartY, gameMap, keyManager);

        // Definir o mapa inicial
        mapLoader.getMaps();
        if (!mapLoader.getMaps().isEmpty()) {
            setCurrentMap(mapLoader.getMaps().get(0).getName());
        }

        // Inicializar inimigos e NPCs
        initializeEntities();
    }

    private void initializeEntities() {
        enemies.clear();
        npcs.clear();

        MapStructure currentMap = gameMap.getCurrentMap();
        if (currentMap == null) return;

        // Associar o mapa às entidades
        for (Enemy enemy : currentMap.getEnemies()) {
            enemy.setGameMap(gameMap);
            enemies.add(enemy);
        }

        for (NPC npc : currentMap.getNpcs()) {
            npc.setGameMap(gameMap);
            npcs.add(npc);
        }
    }

    // Método para trocar de mapa
    public void switchMap(String mapName) {
        gameMap.setCurrentMap(mapName);
        // Reposicionar o jogador conforme necessário
        player.setPosition(100, 100);

        // Re-inicializar inimigos e NPCs
        initializeEntities();
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
        for (NPC npc : npcs) {
            npc.update();
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
            gameMap.render(g, camera);

            // Renderizar o jogador
            player.render(g, camera);

            // Renderizar inimigos
            for (Enemy enemy : enemies) {
                enemy.render(g, camera);
            }

            // Renderizar NPCs
            for (NPC npc : npcs) {
                npc.render(g, camera);
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

    private void setCurrentMap(String mapName) {
        gameMap = new GameMap(mapLoader, mapName);
        camera.setGameMap(gameMap);
        portalManager.setGameMap(gameMap);
        // Reposicionar o jogador no novo mapa
        player.setGameMap(gameMap);
        switchMap(mapName);
    }
}