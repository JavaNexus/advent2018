package pl.javanexus.day11;

public class FuelCell {

    private final int x;
    private final int y;
    private final int powerLevel;

    private int areaPowerLevel;

    public FuelCell(int x, int y, int serialNumber) {
        this.x = x;
        this.y = y;
        this.powerLevel = calculatePowerLevel(serialNumber);
    }

    private int calculatePowerLevel(int serialNumber) {
        int rackId = x + 10;
        return getHundredsDigit(rackId * (rackId * y + serialNumber)) - 5;
    }

    private int getHundredsDigit(int value) {
        String parsed = Integer.toString(value);
        return parsed.charAt(parsed.length() - 3) - '0';
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public int getAreaPowerLevel() {
        return areaPowerLevel;
    }

    public void setAreaPowerLevel(int areaPowerLevel) {
        this.areaPowerLevel = areaPowerLevel;
    }
}
