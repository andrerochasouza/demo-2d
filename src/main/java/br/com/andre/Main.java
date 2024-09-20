package br.com.andre;

import br.com.andre.core.Game;
import br.com.andre.core.Window;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        new Window(Game.GAME_TITLE, Game.WIDTH, Game.HEIGHT, game);
    }
}