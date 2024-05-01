package src;

/**
 * Represents a Bacteria infection that extends the Infection class.
 * This class manages the attributes of bacteria such as reproduction rate,
 * resistance, and environmental tolerance, and provides methods to upgrade
 * and reduce these attributes.
 */
public class Bacteria extends Infection {

    double reproductionRate;
    double resistance;
    double environmentalTolerance;

    /**
     * Constructs a new Bacteria object with specified infection name.
     * Initializes reproduction rate, resistance, and environmental tolerance to 0.2 and
     * recalculates the metrics.
     *
     * @param infectionName The name of the infection.
     */
    public Bacteria(String infectionName) {
        super(infectionName);
        reproductionRate = 0.2;
        resistance = 0.2;
        environmentalTolerance = 0.2;
        recalculate();
    }

    /**
     * Upgrades the reproduction rate by 5% and recalculates the derived metrics.
     */
    @Override
    public void upgradeAttr1() {
        reproductionRate *= 1.05;
        recalculate();
    }

    /**
     * Upgrades the resistance by 5% and recalculates the derived metrics.
     */
    @Override
    public void upgradeAttr2() {
        resistance *= 1.05;
        recalculate();
    }

    /**
     * Upgrades the environmental tolerance by 5% and recalculates the derived metrics.
     */
    @Override
    public void upgradeAttr3() {
        environmentalTolerance *= 1.05;
        recalculate();
    }

    /**
     * Reduces a specified attribute by 5% due to medicine upgrade and returns a message describing
     * the change.
     *
     * @param attribute The attribute to downgrade (1 for reproduction rate, 2 for resistance, 3 for
     *                  environmental tolerance).
     * @return A string message indicating which attribute was reduced and by what factor.
     */
    @Override
    public String attackAttr(int attribute) {
        String toReturn = "Medicine has been upgraded. Your bacteria's ";
        if (attribute == 1) {
            reproductionRate *= 0.95;
            toReturn += "reproduction rate";
        } else if (attribute == 2) {
            resistance *= 0.95;
            toReturn += "resistance";
        } else {
            environmentalTolerance *= 0.95;
            toReturn += "environmental tolerance";
        }
        toReturn += " has decreased by a factor of 0.95";
        return toReturn;
    }

    /**
     * Recalculates derived metrics such as susceptibility rate, infection rate, and fatality rate
     * based on the current values of reproduction rate, resistance, and environmental tolerance.
     */
    private void recalculate() {
        double susceptibilityRate = Math.pow(environmentalTolerance * resistance, 0.5);
        double infectionRate = reproductionRate * (1 + environmentalTolerance);
        double fatalityRate = Math.pow(resistance, resistance);
        setFatalityRate(fatalityRate);
        setInfectionRate(infectionRate);
        setSusceptibilityRate(susceptibilityRate);
    }

    /**
     * Returns the current statistics of the Bacteria infection.
     *
     * @return A string containing formatted information about reproduction rate, resistance, and
     * environmental tolerance.
     */
    @Override
    public String getStats() {
        String toReturn = "Your infection is a bacteria. Here are its stats:\n";
        toReturn += "   Reproduction rate: " + String.format("%.4f", reproductionRate);
        toReturn += "\n   Resistance: " + String.format("%.4f", resistance);
        toReturn += "\n   Environmental tolerance: " + String.format("%.4f",
                environmentalTolerance);
        return toReturn;
    }
}
