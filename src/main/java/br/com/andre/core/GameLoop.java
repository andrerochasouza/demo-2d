package br.com.andre.core;

public class GameLoop implements Runnable {

    private final Game game;
    private final double FPS = 60.0;
    private final double TIME_PER_UPDATE = 1000000000 / FPS;

    public GameLoop(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        double delta = 0;
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;

        while (game.isRunning()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / TIME_PER_UPDATE;
            lastTime = now;

            while (delta >= 1) {
                game.update();
                updates++;
                delta--;
            }

            game.render();
            frames++;

            // Exibir FPS e UPS no console a cada segundo
            if (System.currentTimeMillis() - timer > 1000) {
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
                timer += 1000;
            }
        }
        game.stop();
    }
}