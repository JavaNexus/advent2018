package pl.javanexus.year2018.day15;

import lombok.Data;

import java.util.List;

@Data
public class BattleOutcome {

    private final int numberOfTurns;
    private final List<Unit> allUnits;

    public int getResult() {
        return numberOfTurns * getTotalHp();
    }

    private int getTotalHp() {
        return allUnits.stream().filter(Unit::isAlive).mapToInt(Unit::getHp).sum();
    }

    public boolean areAllUnitsOfTypeAlive(Unit.UnitType unitType) {
        return !allUnits.stream()
                .filter(unit -> unit.getUnitType() == unitType)
                .filter(Unit::isDead)
                .findAny().isPresent();
    }

    public long getNumberOfUnitsAlive(Unit.UnitType unitType) {
        return allUnits.stream()
                .filter(unit -> unit.getUnitType() == unitType)
                .filter(Unit::isAlive)
                .count();
    }
}
