import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

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
}

class RoomSearchService {
    private RoomInventory inventory;
    private List<Room> rooms;

    public RoomSearchService(RoomInventory inventory, List<Room> rooms) {
        this.inventory = inventory;
        this.rooms = rooms;
    }

    public void displayAvailableRooms() {
        System.out.println("=== Available Rooms ===");
        boolean found = false;

        for (Room room : rooms) {
            int availability = inventory.getAvailability(room.getRoomType());

            if (availability > 0) {
                room.displayRoomDetails();
                System.out.println("Availability    : " + availability);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms are currently available.");
        }
    }
}

public class RoomSearch {
    public static void main(String[] args) {
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType(singleRoom.getRoomType(), 5);
        inventory.addRoomType(doubleRoom.getRoomType(), 0);
        inventory.addRoomType(suiteRoom.getRoomType(), 2);

        List<Room> roomList = new ArrayList<>();
        roomList.add(singleRoom);
        roomList.add(doubleRoom);
        roomList.add(suiteRoom);

        RoomSearchService searchService = new RoomSearchService(inventory, roomList);

        System.out.println("=== Book My Stay App ===");
        System.out.println("Use Case 4: Room Search & Availability Check");
        System.out.println();

        searchService.displayAvailableRooms();

        System.out.println();
        System.out.println("=== Inventory After Search ===");
        System.out.println("Single Room : " + inventory.getAvailability("Single Room"));
        System.out.println("Double Room : " + inventory.getAvailability("Double Room"));
        System.out.println("Suite Room  : " + inventory.getAvailability("Suite Room"));
    }
}
