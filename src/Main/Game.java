package Main;

import HeroClasses.*;
import Map.GameMap;
import Map.TerrainType;
import fileio.implementations.FileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class Game {
    int rounds;
    ArrayList<Hero> heroes;
    ArrayList<String> movements;
    GameMap gameMap;
    FileWriter fileWriter;

    public Game(int rounds, ArrayList<Hero> heroes, ArrayList<String> movements, GameMap gameMap, String outputPath) throws IOException {
        this.rounds = rounds;
        this.heroes = heroes;
        this.movements = movements;
        this.gameMap = gameMap;
        this.fileWriter = new FileWriter(outputPath);
    }

    public void start() throws IOException {
        for(int i = 0; i < rounds; ++i) {
//            System.out.println("Round: "+ i);
            // move heroes if not incapacitated
            moveHeroes(movements.get(i));
            // apply over time effects
            applyAllOverTimeEffects();
            // battle
            sustainAllBattles();
//            printPlayerStats();
//            System.out.println("----END ROUND----");
        }
        printPlayerStats();
    }

    private void moveHeroes(String movements) {
        for(int i = 0; i < movements.length(); ++i) {
            char movement = movements.charAt(i);
            switch (movement) {
                case 'U':
                    heroes.get(i).moveUp();
                    break;
                case 'D':
                    heroes.get(i).moveDown();
                    break;
                case 'L':
                    heroes.get(i).moveLeft();
                    break;
                case 'R':
                    heroes.get(i).moveRight();
                    break;
            }
        }
    }

    public void applyAllOverTimeEffects() {
        for(var hero : heroes) {
            hero.applyOverTimeEffects();
        }
    }

    private void battle(Hero hero1, Hero hero2) {
        Hero combatant1;
        Hero combatant2;

        // ensure wizard hits second so he can use deflect
        if(hero1 instanceof Wizard) {
            combatant1 = hero2;
            combatant2 = hero1;
        } else {
            combatant1 = hero1;
            combatant2 = hero2;
        }

        // get terrain type
        int x = combatant1.getX();
        int y = combatant1.getY();
        TerrainType terrain = gameMap.getCellAt(x, y);

        // use abilities
        combatant1.useFirstAbility(combatant2, terrain);
        combatant1.useSecondAbility(combatant2, terrain);
        combatant2.useFirstAbility(combatant1, terrain);
        combatant2.useSecondAbility(combatant1, terrain);

        // add xp if any player died
        int levelCombatant1 = combatant1.getLevel();
        int levelCombatant2 = combatant2.getLevel();

        if(!combatant2.isAlive()) {
            combatant1.addXp(levelCombatant2);
        }

        if(!combatant2.isAlive()) {
            combatant2.addXp(levelCombatant1);
        }
    }

    private void sustainAllBattles() {
        // make battled array, only battle if boolean from hero index is false
        ArrayList<Boolean> battled = new ArrayList<>();
        for(int i = 0; i < heroes.size(); ++i) {
            battled.add(false);
        }

        // check for all collisions
        for(int i = 0; i < heroes.size(); ++i) {
            for(int j = 0; j < heroes.size(); ++j) {
                if(i ==j) {
                    continue;
                }
                if(heroes.get(i).hasSameCoordsAs(heroes.get(j)) && heroes.get(i).isAlive() && heroes.get(j).isAlive()
                        && !battled.get(i) && !battled.get(j)) {
                    battled.set(i, true);
                    battled.set(i, true);
                    battle(heroes.get(i), heroes.get(j));
                }
            }
        }
    }

    private void printPlayerStats() throws IOException {
        for(var hero: heroes) {
            char type;
            if(hero instanceof Knight) {
                type = 'K';
            } else if(hero instanceof Pyromancer) {
                type = 'P';
            } else if(hero instanceof Rogue) {
                type = 'R';
            } else {
                type = 'W';
            }

            fileWriter.writeCharacter(type);
            if(!hero.isAlive()) {
                fileWriter.writeWord(" dead");
                fileWriter.writeNewLine();
            } else {
                fileWriter.writeWord(" " + hero.getLevel() + " " + hero.getXp() + " " + hero.getHp() + " " + hero.getX() + " " + hero.getY());
                fileWriter.writeNewLine();
            }

//            System.out.print(type);
//            if(!hero.isAlive()) {
//                System.out.println(" dead");
//            } else {
//                System.out.println(" " + hero.getLevel() + " " + hero.getXp() + " " + hero.getHp() + " " + hero.getX() + " " + hero.getY());
//            }
        }
        fileWriter.close();
    }
}