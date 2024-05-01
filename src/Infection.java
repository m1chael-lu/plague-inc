package src;

public class Infection {
    String infectionName;
    double susceptibilityRate;
    double infectionRate;
    double fatalityRate;

    public Infection (String infectionName, double susceptibilityRate, double infectionRate,
                      double fatalityRate) {
       this.infectionName = infectionName;
       this.susceptibilityRate = susceptibilityRate;
       this.infectionRate = infectionRate;
       this.fatalityRate = fatalityRate;
    }
}
