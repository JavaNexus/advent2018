package pl.javanexus.year2018.day15;

public class UnitFactory {

    public static final int DEFAULT_MAX_HP = 200;
    public static final int DEFAULT_ATTACK = 3;

    private final Unit.UnitType unitType;
    private final int attackPower;
    private final int initialHp;

    public UnitFactory(Unit.UnitType unitType) {
        this(unitType, DEFAULT_ATTACK, DEFAULT_MAX_HP);
    }

    public UnitFactory(Unit.UnitType unitType, int attackPower) {
        this(unitType, attackPower, DEFAULT_MAX_HP);
    }

    public UnitFactory(Unit.UnitType unitType, int attackPower, int initialHp) {
        this.unitType = unitType;
        this.attackPower = attackPower;
        this.initialHp = initialHp;
    }

    public Unit createUnit(int x, int y) {
        return new Unit(unitType, x, y, initialHp, attackPower);
    }

    public Unit.UnitType getUnitType() {
        return unitType;
    }
}
