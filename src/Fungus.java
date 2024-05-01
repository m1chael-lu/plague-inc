package src;

public class Fungus extends Infection {

    double environmentalGrowthRate;
    double sporeReproduction;
    double survivability;

    public Fungus (String infectionName) {
        super(infectionName);
        environmentalGrowthRate = 0.6;
        sporeReproduction = 0.7;
        survivability = 0.4;
        recalculate();
    }

    @Override
    public void upgradeAttr1() {
        environmentalGrowthRate *= 1.05;
        recalculate();
    }

    @Override
    public void upgradeAttr2() {
        sporeReproduction *= 1.05;
        recalculate();
    }

    @Override
    public void upgradeAttr3() {
        survivability *= 1.05;
        recalculate();
    }

    private void recalculate() {
        double susceptibilityRate = survivability / 3.0;
        double infectionRate = environmentalGrowthRate * 0.3 + (sporeReproduction * 0.45);
        double fatalityRate = ((environmentalGrowthRate + sporeReproduction) / 2.0) * 0.5;

        setFatalityRate(fatalityRate);
        setInfectionRate(infectionRate);
        setSusceptibilityRate(susceptibilityRate);
    }
}
