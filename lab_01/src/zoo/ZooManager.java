package zoo;

import java.util.ArrayList;
import java.util.List;

public class ZooManager {
    private final List<Animal> animals = new ArrayList<>();

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public void displayAllAnimals() {
        System.out.println("=== DANH SÁCH ĐỘNG VẬT TRONG VƯỜN THÚ ===");
        for (Animal a : animals) {
            a.showInfo();
        }
    }

    public static void main(String[] args) {
        ZooManager zoo = new ZooManager();

        // Dữ liệu theo đúng ví dụ trong tài liệu Lab 01
        zoo.addAnimal(new Lion("Leo", 300, 5));
        zoo.addAnimal(new Snake("Boa", 50, 5));
        zoo.addAnimal(new Monkey("George", 150, "chuối"));

        zoo.displayAllAnimals();
    }
}
