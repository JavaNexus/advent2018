package pl.javanexus.year2019.day1;

import java.util.List;

public class RocketEquation {

    int calculateModuleBaseFuel(List<Integer> modulesMass) {
        int totalFuel = 0;
        for (int mass : modulesMass) {
            totalFuel += calculateFuel(mass);
        }

        return totalFuel;
    }

    int calculateModuleTotalFuel(List<Integer> modulesMass) {
        int totalFuel = 0;
        for (int mass : modulesMass) {
            totalFuel += calculateTotalFuel(mass);
        }

        return totalFuel;
    }

    int calculateFuel(int mass) {
        return (int)Math.floor(mass / 3) - 2;
    }

    int calculateTotalFuel(int moduleMass) {
        int totalFuel = 0;
        int fuelMass = calculateFuel(moduleMass);
        while (fuelMass > 0) {
            totalFuel += fuelMass;
            fuelMass = calculateFuel(fuelMass);
        }

        return totalFuel;
    }
}
