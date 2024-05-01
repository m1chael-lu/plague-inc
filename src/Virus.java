package src;

public class Virus extends Infection {

    double mutationRate;
    double hostDependencyFactor;
    double transmissionEffectiveness;

    public Virus (String infectionName) {
        super(infectionName);
        mutationRate = 0.3;
        hostDependencyFactor = 0.5;
        transmissionEffectiveness = 0.3;
        recalculate();
    }

    @Override
    public void upgradeAttr1() {
        mutationRate *= 1.02;
        recalculate();
    }

    @Override
    public void upgradeAttr2() {
        hostDependencyFactor *= 1.01;
        recalculate();
    }

    @Override
    public void upgradeAttr3() {
        transmissionEffectiveness *= 1.01;
        recalculate();
    }

    @Override
    public String attackAttr(int attribute) {
        String toReturn = "Medicine has been upgraded. Your virus's ";
        if (attribute == 1) {
            mutationRate *= 0.95;
            toReturn += "mutation rate";
        } else if (attribute == 2) {
            hostDependencyFactor *= 0.95;
            toReturn += "host dependency factor";
        } else {
            transmissionEffectiveness *= 0.95;
            toReturn += "transmission effectiveness";
        }
        toReturn += " has decreased by a factor of 0.9";
        return toReturn;
    }

    private void recalculate() {
        double susceptibilityRate = hostDependencyFactor * 0.45;
        double infectionRate = transmissionEffectiveness * mutationRate * 1.5;
        double fatalityRate = mutationRate * (transmissionEffectiveness - 0.225);
        setFatalityRate(fatalityRate);
        setInfectionRate(infectionRate);
        setSusceptibilityRate(susceptibilityRate);
    }

    @Override
    public String getStats() {
        String toReturn = "Your infection is a fungus. Here are its stats:\n";
        toReturn += "   Mutation rate: " + String.format("%.4f", mutationRate);
        toReturn += "\n   Host dependency factor: " + String.format("%.4f", hostDependencyFactor);
        toReturn += "\n   Transmission effectiveness: " + String.format("%.4f", transmissionEffectiveness);
        return toReturn;
    }
}
