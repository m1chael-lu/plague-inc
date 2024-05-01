package src;

/**
 * Represents a virus as a type of infection with specific attributes like mutation rate,
 * host dependency factor, and transmission effectiveness. This class manages the behavior and
 * evolution of the virus during a simulation.
 */
public class Virus extends Infection {

    double mutationRate;
    double hostDependencyFactor;
    double transmissionEffectiveness;

    /**
     * Constructs a Virus instance with a given name and initializes its characteristics.
     *
     * @param infectionName The name of the virus.
     */
    public Virus(String infectionName) {
        super(infectionName);
        mutationRate = 0.3;
        hostDependencyFactor = 0.5;
        transmissionEffectiveness = 0.3;
        recalculate();
    }

    /**
     * Upgrades the mutation rate of the virus and recalculates derived stats.
     */
    @Override
    public void upgradeAttr1() {
        mutationRate *= 1.02;
        recalculate();
    }

    /**
     * Upgrades the host dependency factor of the virus and recalculates derived stats.
     */
    @Override
    public void upgradeAttr2() {
        hostDependencyFactor *= 1.01;
        recalculate();
    }

    /**
     * Upgrades the transmission effectiveness of the virus and recalculates derived stats.
     */
    @Override
    public void upgradeAttr3() {
        transmissionEffectiveness *= 1.01;
        recalculate();
    }

    /**
     * Responds to an attack on a specific attribute by reducing its value and recalculating derived
     * stats.
     *
     * @param attribute The attribute to be targeted by the attack (1: mutation rate, 2: host
     *                  dependency factor,
     *                  3: transmission effectiveness).
     * @return A message describing the effect of the attack on the virus.
     */
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

    /**
     * Recalculates the derived statistics based on the current values of the virus's attributes.
     */
    private void recalculate() {
        double susceptibilityRate = hostDependencyFactor * 0.45;
        double infectionRate = transmissionEffectiveness * mutationRate * 1.5;
        double fatalityRate = mutationRate * (transmissionEffectiveness - 0.225);
        setFatalityRate(fatalityRate);
        setInfectionRate(infectionRate);
        setSusceptibilityRate(susceptibilityRate);
    }

    /**
     * Returns a formatted string with the current stats of the virus.
     *
     * @return A string containing formatted data about mutation rate, host dependency factor, and
     * transmission effectiveness.
     */
    @Override
    public String getStats() {
        String toReturn = "Your infection is a fungus. Here are its stats:\n";
        toReturn += "   Mutation rate: " + String.format("%.4f", mutationRate);
        toReturn += "\n   Host dependency factor: " + String.format("%.4f", hostDependencyFactor);
        toReturn += "\n   Transmission effectiveness: " + String.format("%.4f",
                transmissionEffectiveness);
        return toReturn;
    }
}
