package main;


import InputOutput.GameInput;
import InputOutput.GameInputLoader;
import Map.TerrainType;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GameInputLoader loader = new GameInputLoader(args[0], args[1]);
        GameInput input = loader.load();

        var heroes = input.getHeroes();
        System.out.println(heroes);
        heroes.get(1).setLevel(25);
        heroes.get(0).takeDamage(550);
        heroes.get(0).hitByFirstAbility(heroes.get(1), TerrainType.Land);
        System.out.println(heroes);



        //System.out.println(input.getGameMap());
        //System.out.println(input.getMovements());
    }
}
