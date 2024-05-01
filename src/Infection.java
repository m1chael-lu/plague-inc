package src;

public abstract class Infection {
    private String infectionName;
    private double susceptibilityRate;
    private double infectionRate;
    private double fatalityRate;

    public Infection() {

    }

    public Infection (String infectionName) {
       this.infectionName = infectionName;
    }

    public double getFatalityRate() {
        return fatalityRate;
    }

    public double getSusceptibilityRate() {
        return susceptibilityRate;
    }

    public double getInfectionRate() {
        return infectionRate;
    }

    public void setInfectionRate(double infectionRate) {
        this.infectionRate = infectionRate;
    }

    public void setFatalityRate(double fatalityRate) {
        this.fatalityRate = fatalityRate;
    }

    public void setSusceptibilityRate(double susceptibilityRate) {
        this.susceptibilityRate = susceptibilityRate;
    }

    public String getInfectionName() {
        return infectionName;
    }

    public void upgradeAttr1() { }

    public void upgradeAttr2() { }

    public void upgradeAttr3() { }

}
