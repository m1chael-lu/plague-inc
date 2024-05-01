package src;

public class Infection {
    String infectionName;
    double susceptibilityRate;
    double infectionRate;
    double recoveryRate;
    double fatalityRate;

    public Infection (String infectionName, double susceptibilityRate, double infectionRate,
                      double recoveryRate, double fatalityRate) {
       this.infectionName = infectionName;
       this.susceptibilityRate = susceptibilityRate;
       this.infectionRate = infectionRate;
       this.recoveryRate = recoveryRate;
       this.fatalityRate = fatalityRate;
    }
}
