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
}

class BookingService {
    private final Queue<BookingRequest> queue = new LinkedList<>();
    private final InventoryService inventory;
    private final Validator validator;
    private final BookingHistory history;
    private final RoomAllocator allocator;

