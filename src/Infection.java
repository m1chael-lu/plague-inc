package src;

/**
 * Abstract class representing a general infection model.
 * This class serves as a base for specific types of infections, providing common properties and
 * methods that can be used and overridden in subclasses to model different infection dynamics.
 */
public abstract class Infection {
    private String infectionName;
    private double susceptibilityRate;
    private double infectionRate;
    private double fatalityRate;

    /**
     * Default constructor for creating an infection instance without initializing the infection
     * name.
     */
    public Infection() {

    }

    /**
     * Constructs an infection with a specified name.
     *
     * @param infectionName The name of the infection.
     */
    public Infection(String infectionName) {
        this.infectionName = infectionName;
    }

    /**
     * Returns the fatality rate of the infection.
     *
     * @return The fatality rate as a double.
     */
    public double getFatalityRate() {
        return fatalityRate;
    }

    /**
     * Sets the fatality rate of the infection.
     *
     * @param fatalityRate The new fatality rate to set.
     */
    public void setFatalityRate(double fatalityRate) {
        this.fatalityRate = fatalityRate;
    }

    /**
     * Returns the susceptibility rate of the infection.
     *
     * @return The susceptibility rate as a double.
     */
    public double getSusceptibilityRate() {
        return susceptibilityRate;
    }

    /**
     * Sets the susceptibility rate of the infection.
     *
     * @param susceptibilityRate The new susceptibility rate to set.
     */
    public void setSusceptibilityRate(double susceptibilityRate) {
        this.susceptibilityRate = susceptibilityRate;
    }

    /**
     * Returns the infection rate of the infection.
     *
     * @return The infection rate as a double.
     */
    public double getInfectionRate() {
        return infectionRate;
    }

    /**
     * Sets the infection rate of the infection.
     *
     * @param infectionRate The new infection rate to set.
     */
    public void setInfectionRate(double infectionRate) {
        this.infectionRate = infectionRate;
    }

    /**
     * Returns the name of the infection.
     *
     * @return The infection name as a string.
     */
    public String getInfectionName() {
        return infectionName;
    }

    /**
     * Upgrades the first attribute of the infection. The specific attribute and the upgrade
     * mechanics depend on the subclass implementation.
     */
    public void upgradeAttr1() {
    }

    /**
     * Upgrades the second attribute of the infection. The specific attribute and the upgrade
     * mechanics depend on the subclass implementation.
     */
    public void upgradeAttr2() {
    }

    /**
     * Upgrades the third attribute of the infection. The specific attribute and the upgrade
     * mechanics depend on the subclass implementation.
     */
    public void upgradeAttr3() {
    }

    /**
     * Attacks a specified attribute of the infection, typically reducing its effectiveness.
     * The specific attribute and attack mechanics depend on the subclass implementation.
     *
     * @param attribute The index of the attribute to attack.
     * @return A string describing the result of the attack.
     */
    public String attackAttr(int attribute) {
        return "";
    }

    /**
     * Provides statistical information about the infection.
     * Typically includes rates like infection rate, fatality rate, and susceptibility rate.
     *
     * @return A string containing formatted statistical data about the infection.
     */
    public String getStats() {
        return "";
    }

}
