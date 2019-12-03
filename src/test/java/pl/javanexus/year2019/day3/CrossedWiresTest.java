package pl.javanexus.year2019.day3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CrossedWiresTest {

    public static final Wires firstExample = new Wires(
            new String[]{"R8", "U5", "L5", "D3"},
            new String[]{"U7", "R6", "D4", "L4"});

    public static final Wires secondExample = new Wires(
            new String[]{"R75", "D30", "R83", "U83", "L12", "D49", "R71", "U7", "L72"},
            new String[]{"U62", "R66", "U55", "R34", "D71", "R55", "D58", "R83"});

    public static final Wires thirdExample = new Wires(
            new String[] {"R98", "U47", "R26", "D63", "R33", "U87", "L62", "D20", "R33", "U53", "R51"},
            new String[] {"U98", "R91", "D20", "R16", "D67", "R40", "U7", "R15", "U6", "R7"});

    public static final Wires input = new Wires(
            new String[]{"R998", "U494", "L814", "D519", "R407", "U983", "R307", "D745", "R64", "D29", "L935", "D919", "L272", "D473", "R689", "U560", "L942", "U264", "R816", "U745", "R209", "U227", "R241", "U111", "L653", "D108", "R823", "U254", "L263", "U987", "L368", "D76", "R665", "D646", "L759", "U425", "L581", "D826", "R829", "D388", "L234", "U33", "L48", "U598", "L708", "D764", "L414", "D75", "L163", "U802", "L183", "U893", "L486", "U947", "L393", "D694", "L454", "D600", "R377", "U312", "R89", "D178", "L652", "D751", "R402", "D946", "R213", "U985", "R994", "D336", "R573", "D105", "L442", "U965", "R603", "U508", "L17", "U191", "L37", "U678", "L506", "U823", "R878", "D709", "L348", "U167", "L355", "U314", "L164", "D672", "L309", "U895", "R358", "D769", "R869", "U598", "R63", "D68", "R105", "U133", "R357", "U588", "L154", "D631", "L939", "D235", "R506", "D885", "R958", "D896", "L195", "U292", "L952", "D616", "L824", "D497", "R99", "D121", "R387", "D155", "L70", "U580", "L890", "D368", "L910", "U645", "L786", "U977", "R9", "U781", "L454", "U783", "L382", "U321", "L195", "U196", "L239", "U764", "R18", "D71", "R97", "U77", "L803", "U963", "L704", "U94", "L511", "U747", "L798", "D905", "L679", "D135", "R455", "U650", "R947", "U14", "L722", "D245", "L490", "D183", "L276", "U559", "L901", "D767", "R827", "U522", "L380", "U494", "R402", "U70", "R589", "D582", "R206", "U756", "L989", "U427", "L704", "D864", "R885", "D9", "R872", "U454", "R912", "U752", "R197", "U304", "L728", "U879", "R456", "D410", "L141", "U473", "R246", "U498", "R443", "D297", "R333", "D123", "R12", "D665", "R684", "D531", "R601", "D13", "L260", "U60", "R302", "D514", "R416", "D496", "L562", "D334", "L608", "U74", "R451", "U251", "R961", "U166", "L368", "U146", "R962", "U973", "R120", "U808", "R480", "D536", "L690", "D958", "R292", "U333", "R656", "U305", "R46", "U831", "L756", "D907", "L638", "D969", "L445", "U541", "R784", "U148", "R338", "D264", "R72", "D637", "R759", "D709", "L611", "D34", "R99", "U305", "R143", "D191", "R673", "D753", "R387", "U994", "R720", "D896", "R95", "U703", "L499", "D453", "R96", "U808", "L485", "U127", "L856", "U357", "L543", "U382", "R411", "U969", "L532", "U64", "R303", "U457", "L412", "D140", "R146", "D67", "R147", "D681", "L1", "D994", "L876", "D504", "R46", "U683", "L992", "U640", "L663", "D681", "L327", "U840", "R543", "U97", "R988", "U792", "R36"},
            new String[]{"L999", "U148", "L592", "D613", "L147", "D782", "R594", "U86", "R891", "D448", "R92", "U756", "R93", "D763", "L536", "U906", "L960", "D988", "L532", "U66", "R597", "U120", "L273", "D32", "R525", "U628", "L630", "U89", "L248", "U594", "R886", "D544", "L288", "U380", "L23", "D191", "L842", "U394", "L818", "U593", "L195", "U183", "L863", "D456", "L891", "D653", "R618", "U314", "L775", "D220", "R952", "U960", "R714", "U946", "L343", "D873", "L449", "U840", "R769", "U356", "L20", "D610", "L506", "U733", "R524", "D450", "L888", "D634", "R737", "U171", "R906", "U369", "L172", "D625", "L97", "D437", "R359", "D636", "R775", "U749", "L281", "U188", "R418", "D437", "R708", "D316", "L388", "D717", "R59", "U73", "R304", "U148", "L823", "U137", "R265", "U59", "R488", "D564", "R980", "U798", "L626", "U47", "L763", "U858", "L450", "U663", "R378", "U93", "L275", "U472", "L792", "U544", "R192", "D979", "L520", "U835", "L946", "D615", "L120", "U923", "L23", "U292", "R396", "U605", "L76", "U813", "L388", "U500", "L848", "U509", "L276", "D538", "R26", "D806", "R685", "D319", "R414", "D989", "L519", "U603", "R898", "D477", "L107", "D828", "R836", "U432", "L601", "U888", "L476", "D974", "L911", "U122", "L921", "D401", "L878", "D962", "L214", "D913", "L113", "U418", "R992", "U844", "L928", "U534", "L13", "U457", "L866", "D208", "L303", "D732", "L497", "U673", "R659", "D639", "R430", "D301", "L573", "U373", "L270", "D901", "L605", "D935", "R440", "D183", "R374", "D883", "L551", "U771", "R707", "D141", "R984", "U346", "R894", "U1", "R33", "U492", "R371", "D631", "R95", "D62", "L378", "D343", "R108", "D611", "L267", "D871", "L296", "U391", "R302", "D424", "R160", "U141", "R682", "U949", "R380", "U251", "L277", "D404", "R392", "D210", "L158", "U896", "R350", "D891", "L837", "D980", "R288", "U597", "L292", "D639", "L943", "D827", "L492", "D625", "L153", "D570", "R948", "D855", "L945", "U840", "L493", "U800", "L392", "D438", "R8", "U966", "R218", "U597", "R852", "D291", "L840", "D831", "L498", "U846", "R875", "D244", "R159", "U243", "R975", "D246", "R549", "D304", "R954", "D123", "R58", "U5", "L621", "U767", "R455", "U550", "R800", "D417", "R869", "D184", "L966", "D51", "L383", "U132", "L664", "U220", "L263", "D307", "R716", "U346", "L958", "U84", "L154", "U90", "L726", "U628", "L159", "U791", "L643", "D652", "L138", "U577", "R457", "U655", "L547", "D441", "L21"});

    private CrossedWires crossedWires;

    @Before
    public void setUp() throws Exception {
        this.crossedWires = new CrossedWires();
    }

    /**
     * 8 x 7
     * 238 x 147
     * 179 x 120
     * 10315 x 15120
     */
    @Test
    public void findPoints() {
        crossedWires.findBoundary(firstExample.getFirstWire(), firstExample.getSecondWire());
        crossedWires.findBoundary(secondExample.getFirstWire(), secondExample.getSecondWire());
        crossedWires.findBoundary(thirdExample.getFirstWire(), thirdExample.getSecondWire());
        crossedWires.findBoundary(input.getFirstWire(), input.getSecondWire());
    }

    @Test
    public void testGetIntersection() {
        int d;
        d = crossedWires.readValues(firstExample.getFirstWire(), firstExample.getSecondWire());
        assertEquals(6, d);

        d = crossedWires.readValues(secondExample.getFirstWire(), secondExample.getSecondWire());
        assertEquals(159, d);

        d = crossedWires.readValues(thirdExample.getFirstWire(), thirdExample.getSecondWire());
        assertEquals(135, d);

        d = crossedWires.readValues(input.getFirstWire(), input.getSecondWire());
        assertEquals(709, d);
    }
}
