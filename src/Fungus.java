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

    @Override
    public String attackAttr(int attribute) {
        String toReturn = "Medicine has been upgraded. Your fungus's ";
        if (attribute == 1) {
            environmentalGrowthRate *= 0.9;
            toReturn += "environmental growth rate";
        } else if (attribute == 2) {
            sporeReproduction *= 0.9;
            toReturn += "spore reproduction";
        } else {
            survivability *= 0.9;
            toReturn += "survivability";
        }
        toReturn += " has decreased by a factor of 0.9";
        return toReturn;
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
