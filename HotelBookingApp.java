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

public class UseCase2RoomInitialization {
    public static void main(String[] args) {
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        int singleRoomAvailability = 10;
        int doubleRoomAvailability = 6;
        int suiteRoomAvailability = 3;

        System.out.println("=== Book My Stay App ===");
        System.out.println("Use Case 2: Basic Room Types & Static Availability");
        System.out.println();

        singleRoom.displayRoomDetails();
        System.out.println("Availability    : " + singleRoomAvailability);

        doubleRoom.displayRoomDetails();
        System.out.println("Availability    : " + doubleRoomAvailability);
        suiteRoom.displayRoomDetails();
        System.out.println("Availability    : " + suiteRoomAvailability);
    }
}
