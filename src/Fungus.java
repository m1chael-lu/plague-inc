package src;

/**
 * Represents a Fungus infection that extends the Infection class.
 * Manages attributes such as environmental growth rate, spore reproduction, and survivability.
 * Provides methods to upgrade these attributes and adjust them in response to external factors.
 */
public class Fungus extends Infection {

    double environmentalGrowthRate;
    double sporeReproduction;
    double survivability;

    /**
     * Initializes a new instance of Fungus with a specified infection name.
     * Sets initial values for environmental growth rate, spore reproduction, and survivability, and
     * recalculates derived metrics.
     *
     * @param infectionName The name of the infection.
     */
    public Fungus(String infectionName) {
        super(infectionName);
        environmentalGrowthRate = 0.6;
        sporeReproduction = 0.7;
        survivability = 0.4;
        recalculate();
    }

    /**
     * Increases the environmental growth rate by 5% and updates the derived metrics.
     */
    @Override
    public void upgradeAttr1() {
        environmentalGrowthRate *= 1.05;
        recalculate();
    }

    /**
     * Increases the spore reproduction rate by 5% and updates the derived metrics.
     */
    @Override
    public void upgradeAttr2() {
        sporeReproduction *= 1.05;
        recalculate();
    }

    /**
     * Increases the survivability by 5% and updates the derived metrics.
     */
    @Override
    public void upgradeAttr3() {
        survivability *= 1.05;
        recalculate();
    }

    /**
     * Reduces a specified attribute by 5% due to medical intervention and provides a message
     * describing the change.
     *
     * @param attribute The attribute index to degrade (1 for environmental growth rate, 2 for spore
     *                  reproduction, 3 for survivability).
     * @return A message indicating which attribute was affected and by how much.
     */
    @Override
    public String attackAttr(int attribute) {
        String toReturn = "Medicine has been upgraded. Your fungus's ";
        if (attribute == 1) {
            environmentalGrowthRate *= 0.95;
            toReturn += "environmental growth rate";
        } else if (attribute == 2) {
            sporeReproduction *= 0.95;
            toReturn += "spore reproduction";
        } else {
            survivability *= 0.95;
            toReturn += "survivability";
        }
        toReturn += " has decreased by a factor of 0.95";
        return toReturn;
    }

    /**
     * Recalculates the derived metrics related to disease transmission based on the current state
     * of the fungus's growth and resistance attributes.
     */
    private void recalculate() {
        double susceptibilityRate = survivability / 3.0;
        double infectionRate = environmentalGrowthRate * 0.3 + (sporeReproduction * 0.45);
        double fatalityRate = ((environmentalGrowthRate + sporeReproduction) / 3.0) * 0.5;

        setFatalityRate(fatalityRate);
        setInfectionRate(infectionRate);
        setSusceptibilityRate(susceptibilityRate);
    }

    /**
     * Returns the current statistics of the fungus infection.
     *
     * @return Formatted string detailing current environmental growth rate, spore reproduction, and
     * survivability.
     */
    @Override
    public String getStats() {
        String toReturn = "Your infection is a fungus. Here are its stats:\n";
        toReturn += "   Environmental growth rate: " + String.format("%.4f",
                environmentalGrowthRate);
        toReturn += "\n   Spore reproduction: " + String.format("%.4f", sporeReproduction);
        toReturn += "\n   Survivability: " + String.format("%.4f", survivability);
        return toReturn;
    }
}
