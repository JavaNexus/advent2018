package pl.javanexus.year2018.day15;

import lombok.Getter;

public class Unit implements Comparable {

    public static final int MIN_HP = 0;

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

    @Getter
    private final UnitType unitType;

    private final int attackPower;

    @Getter
    private int x;
    @Getter
    private int y;
    @Getter
    private int hp;

    public Unit(UnitType unitType, int x, int y, int initialHp, int attackPower) {
        this.unitType = unitType;
        this.x = x;
        this.y = y;
        this.hp = initialHp;
        this.attackPower = attackPower;
    }

    public void move(Tile from, Tile to) {
        from.setUnit(null);
        to.setUnit(this);
        this.x = to.getX();
        this.y = to.getY();
    }

    public boolean attack(Unit target) {
        target.decrementHp(attackPower);
        return !target.isAlive();
    }

    public void decrementHp(int hpDelta) {
        this.hp -= hpDelta;
    }

    public boolean isAlive() {
        return hp > MIN_HP;
    }

    public boolean isDead() {
        return !isAlive();
    }

    @Override
    public int compareTo(Object o) {
        Unit other = (Unit) o;
        int dy = this.getY() - other.getY();
        return dy == 0 ? this.getX() - other.getX() : dy;
    }
}
