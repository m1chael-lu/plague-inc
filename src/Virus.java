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
        mutationRate *= 1.05;
        recalculate();
    }

    @Override
    public void upgradeAttr2() {
        hostDependencyFactor *= 1.05;
        recalculate();
    }

    @Override
    public void upgradeAttr3() {
        transmissionEffectiveness *= 1.05;
        recalculate();
    }

    private void recalculate() {
        double susceptibilityRate = hostDependencyFactor * 0.6;
        double infectionRate = transmissionEffectiveness * mutationRate * 3;
        double fatalityRate = mutationRate * (transmissionEffectiveness - 0.1);
        setFatalityRate(fatalityRate);
        setInfectionRate(infectionRate);
        setSusceptibilityRate(susceptibilityRate);
    }
}
