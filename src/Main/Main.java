package Main;


import InputOutput.GameInput;
import InputOutput.GameInputLoader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GameInputLoader loader = new GameInputLoader(args[0], args[1]);
        GameInput input = loader.load();
        Game game = new Game(input.getNumOfRounds(), input.getHeroes(), input.getMovements(), input.getGameMap(), args[1]);
        game.start();
    }
}