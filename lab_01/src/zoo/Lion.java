package zoo;

public class Lion extends Animal {
    private double meatPerDay;

    public Lion(String name, double weight, double meatPerDay) {
        super(name, weight);
        this.meatPerDay = meatPerDay;
    }

    public double getMeatPerDay() {
        return meatPerDay;
    }

    public void setMeatPerDay(double meatPerDay) {
        this.meatPerDay = meatPerDay;
    }

    @Override
    public void showInfo() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return String.format("Sư tử %s nặng %.0f cân và ăn %.0f cân thịt mỗi ngày.",
                name, weight, meatPerDay);
    }
}
