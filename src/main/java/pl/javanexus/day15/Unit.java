package pl.javanexus.day15;

import lombok.Data;

@Data
public class Unit {

    enum UnitType {
        ELF {
            @Override
            public UnitType getEnemy() {
                return GOBLIN;
            }
        },
        GOBLIN {
            @Override
            public UnitType getEnemy() {
                return ELF;
            }
        };

        public abstract UnitType getEnemy();
    }

    public static final int MAX_HP = 200;
    public static final int MAX_ATTACK = 3;

    private final UnitType unitType;

    private int x;
    private int y;
    private int hp;
    private int attack;

    public Unit(UnitType unitType) {
        this.unitType = unitType;
    }

    public void executeTurn() {

    }

    private void selectTargetUnit() {

    }

    private void move() {

    }

    private void attack() {

    }
}
