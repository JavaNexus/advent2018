package pl.javanexus.day15;

import lombok.Data;

@Data
public class Unit implements Comparable {

    enum UnitType {
        ELF {
            @Override
            public UnitType getEnemyType() {
                return GOBLIN;
            }
        },
        GOBLIN {
            @Override
            public UnitType getEnemyType() {
                return ELF;
            }
        };

        public abstract UnitType getEnemyType();
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

    public void move(Tile from, Tile to) {
        from.setUnit(null);
        to.setUnit(this);
        setX(to.getX());
        setY(to.getY());
    }

    @Override
    public int compareTo(Object o) {
        Unit other = (Unit) o;
        int dy = this.getY() - other.getY();
        return dy == 0 ? this.getX() - other.getX() : dy;
    }
}
