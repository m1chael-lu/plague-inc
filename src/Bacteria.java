package src;

public class Bacteria extends Infection {

    double reproductionRate;
    double resistance;
    double environmentalTolerance;

    public Bacteria(String infectionName) {
        super(infectionName);
        reproductionRate = 0.2;
        resistance = 0.2;
        environmentalTolerance = 0.2;
        recalculate();
    }

    @Override
    public void upgradeAttr1() {
        reproductionRate *= (1.05);
        recalculate();
    }

    @Override
    public void upgradeAttr2() {
        resistance *= (1.05);
        recalculate();
    }

    @Override
    public void upgradeAttr3() {
        environmentalTolerance *= (1.05);
        recalculate();
    }

    @Override
    public String attackAttr(int attribute) {
        String toReturn = "Medicine has been upgraded. Your bacteria's ";
        if (attribute == 1) {
            reproductionRate *= 0.9;
            toReturn += "reproduction rate";
        } else if (attribute == 2) {
            resistance *= 0.9;
            toReturn += "resistance";
        } else {
            environmentalTolerance *= 0.9;
            toReturn += "environmental tolerance";
        }
        toReturn += " has decreased by a factor of 0.9";
        return toReturn;
    }

    private void recalculate() {
        double susceptibilityRate = Math.pow(environmentalTolerance * resistance, 0.5);
        double infectionRate = reproductionRate * (1 + environmentalTolerance);
        double fatalityRate = Math.pow(resistance, resistance);
        setFatalityRate(fatalityRate);
        setInfectionRate(infectionRate);
        setSusceptibilityRate(susceptibilityRate);
    }
}
