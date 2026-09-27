package zoo;

public class Snake extends Animal {
    private double length;

    public Snake(String name, double weight, double length) {
        super(name, weight);
        this.length = length;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    @Override
    public void showInfo() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return String.format("Con rắn %s nặng %.0f cân và dài %.0f mét.",
                name, weight, length);
    }
}
