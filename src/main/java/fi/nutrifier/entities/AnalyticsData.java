package fi.nutrifier.entities;

import lombok.Data;

@Data
public class AnalyticsData {
    private double actual;
    private double goal;

    public AnalyticsData() {
        this.actual = 0.0;
        this.goal = 0.0;
    }

    public AnalyticsData(double actual, double goal) {
        this.actual = actual;
        this.goal = goal;
    }

    public void appendActualAndGoal(double actual, double goal) {
        this.actual += actual;
        this.goal += goal;
    }
}
