import java.util.*;

class BookingRequest {
    private String customerName;
    private String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 3);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public boolean hasAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("Inventory: " + inventory);
    }
}

class BookingService {
    private Queue<BookingRequest> requestQueue = new LinkedList<>();
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();
    private Set<String> allAllocatedIds = new HashSet<>();
    private InventoryService inventory;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void addRequest(String name, String roomType) {
        requestQueue.offer(new BookingRequest(name, roomType));
    }

    private String generateRoomId(String roomType) {
        String id;
        do {
            id = roomType.substring(0,1).toUpperCase() + (int)(Math.random()*1000);
        } while(allAllocatedIds.contains(id));
        return id;
    }

    public void processBookings() {
        while(!requestQueue.isEmpty()) {
            BookingRequest req = requestQueue.poll();
            String type = req.getRoomType();

            if(inventory.hasAvailability(type)) {
                String roomId = generateRoomId(type);
                allAllocatedIds.add(roomId);

                allocatedRooms.putIfAbsent(type, new HashSet<>());
                allocatedRooms.get(type).add(roomId);

                inventory.decrement(type);

                System.out.println("Reservation Confirmed: " + req.getCustomerName() + " -> Room " + roomId);
            } else {
                System.out.println("No rooms available for " + req.getCustomerName() + " (" + type + ")");
            }
        }
    }

    public void displayAllocations() {
        System.out.println("Allocated Rooms: " + allocatedRooms);
    }
}

public class UseCase6RoomAllocationService {
    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService(inventory);

        bookingService.addRequest("Alice", "Single");
        bookingService.addRequest("Bob", "Double");
        bookingService.addRequest("Charlie", "Single");
        bookingService.addRequest("David", "Suite");
        bookingService.addRequest("Eva", "Single");

        bookingService.processBookings();

        System.out.println();

        bookingService.displayAllocations();
        inventory.displayInventory();
    }
}
