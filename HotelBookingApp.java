import java.util.HashMap;
import java.util.Map;

abstract class Room {
    private String roomType;
    private int numberOfBeds;
    private double sizeInSqFt;
    private double pricePerNight;

    public Room(String roomType, int numberOfBeds, double sizeInSqFt, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.sizeInSqFt = sizeInSqFt;
        this.pricePerNight = pricePerNight;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getSizeInSqFt() {
        return sizeInSqFt;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public abstract String getFeatures();

    public void displayRoomDetails() {
        System.out.println("Room Type       : " + roomType);
        System.out.println("Number of Beds  : " + numberOfBeds);
        System.out.println("Size (sq.ft)    : " + sizeInSqFt);
        System.out.println("Price per Night : Rs." + pricePerNight);
        System.out.println("Features        : " + getFeatures());
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 180.0, 2000.0);
    }

    @Override
    public String getFeatures() {
        return "Suitable for 1 guest, compact and comfortable";
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 300.0, 3500.0);
    }

    @Override
    public String getFeatures() {
        return "Suitable for 2 guests, ideal for couples";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 500.0, 6000.0);
    }

    @Override
    public String getFeatures() {
        return "Premium room with living area and luxury amenities";
    }
}

class RoomInventory {
    private HashMap<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();
    }

    public void addRoomType(String roomType, int availableCount) {
        availabilityMap.put(roomType, availableCount);
    }

    public int getAvailability(String roomType) {
        Integer count = availabilityMap.get(roomType);
        if (count == null) {
            return 0;
        }
        return count;
    }

    public void updateAvailability(String roomType, int newCount) {
        if (availabilityMap.containsKey(roomType)) {
            availabilityMap.put(roomType, newCount);
        }
    }

    public void displayInventory() {
        System.out.println("=== Current Room Inventory ===");
        for (Map.Entry<String, Integer> entry : availabilityMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " rooms available");
        }
    }
}

public class UseCase3InventorySetup {
    public static void main(String[] args) {
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        RoomInventory inventory = new RoomInventory();

        inventory.addRoomType(singleRoom.getRoomType(), 10);
        inventory.addRoomType(doubleRoom.getRoomType(), 6);
        inventory.addRoomType(suiteRoom.getRoomType(), 3);

        System.out.println("=== Book My Stay App ===");
        System.out.println("Use Case 3: Centralized Room Inventory Management");
        System.out.println();

        singleRoom.displayRoomDetails();
        System.out.println("Availability    : " + inventory.getAvailability(singleRoom.getRoomType()));
        System.out.println("--------------------------------------------");

        doubleRoom.displayRoomDetails();
        System.out.println("Availability    : " + inventory.getAvailability(doubleRoom.getRoomType()));
        System.out.println("--------------------------------------------");

        suiteRoom.displayRoomDetails();
        System.out.println("Availability    : " + inventory.getAvailability(suiteRoom.getRoomType()));
        System.out.println("--------------------------------------------");

        System.out.println();
        inventory.displayInventory();

        System.out.println();
        System.out.println("Updating Double Room availability to 5...");
        inventory.updateAvailability("Double Room", 5);

        System.out.println();
        inventory.displayInventory();
    }
}
