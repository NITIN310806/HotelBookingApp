
import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class BookingRequest {
    private String guestName;
    private String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class Validator {
    private final Set<String> validRoomTypes;

    public Validator(Set<String> validRoomTypes) {
        this.validRoomTypes = validRoomTypes;
    }

    public void validate(BookingRequest request) throws InvalidBookingException {
        if (request.getGuestName() == null || request.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Invalid guest name: must be non-empty");
        }
        if (request.getRoomType() == null || request.getRoomType().trim().isEmpty()) {
            throw new InvalidBookingException("Invalid room type: must be non-empty");
        }
        if (!validRoomTypes.contains(request.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: '" + request.getRoomType() + "' not supported");
        }
    }
}

class InventoryService {
    private final Map<String, Integer> inventory = new LinkedHashMap<>();

    public InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public boolean hasAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrement(String roomType) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Cannot decrement inventory: invalid room type " + roomType);
        }
        int count = inventory.get(roomType);
        if (count <= 0) {
            throw new InvalidBookingException("Inventory underflow prevented for room type " + roomType);
        }
        inventory.put(roomType, count - 1);
    }

    public Map<String, Integer> getInventory() {
        return Collections.unmodifiableMap(inventory);
    }

    public void display() {
        System.out.println("Current inventory: " + inventory);
    }
}

class BookingHistory {
    private final List<BookingRecord> history = new ArrayList<>();

    public void add(BookingRecord record) {
        history.add(record);
    }

    public List<BookingRecord> all() {
        return Collections.unmodifiableList(history);
    }
}

class BookingRecord {
    private final String guestName;
    private final String roomType;
    private final String roomId;
    private final Date timestamp;

    public BookingRecord(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.timestamp = new Date();
    }

    @Override
    public String toString() {
        return "BookingRecord{" +
                "guest='" + guestName + '\'' +
                ", roomType='" + roomType + '\'' +
                ", roomId='" + roomId + '\'' +
                ", time='" + timestamp + '\'' +
                '}';
    }
}

class RoomAllocator {
    private final Set<String> allocatedIds = new HashSet<>();

    public String allocate(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 1).toUpperCase() + (int) (Math.random() * 1000);
        } while (allocatedIds.contains(roomId));
        allocatedIds.add(roomId);
        return roomId;
    }
}

class BookingService {
    private final Queue<BookingRequest> queue = new LinkedList<>();
    private final InventoryService inventory;
    private final Validator validator;
    private final BookingHistory history;
    private final RoomAllocator allocator;

    public BookingService(InventoryService inventory, Validator validator, BookingHistory history, RoomAllocator allocator) {
        this.inventory = inventory;
        this.validator = validator;
        this.history = history;
        this.allocator = allocator;
    }

    public void addRequest(BookingRequest request) {
        queue.offer(request);
    }

    public void processAll() {
        while (!queue.isEmpty()) {
            BookingRequest request = queue.poll();
            try {
                validator.validate(request);
                if (!inventory.hasAvailability(request.getRoomType())) {
                    throw new InvalidBookingException("No available rooms for type " + request.getRoomType());
                }
                String roomId = allocator.allocate(request.getRoomType());
                inventory.decrement(request.getRoomType());
                history.add(new BookingRecord(request.getGuestName(), request.getRoomType(), roomId));
                System.out.println("Booking confirmed: " + request.getGuestName() + " -> " + roomId + " (" + request.getRoomType() + ")");
            } catch (InvalidBookingException e) {
                System.out.println("Booking failed for request [guest='" + request.getGuestName() + "', roomType='" + request.getRoomType() + "']: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error for request [guest='" + request.getGuestName() + "', roomType='" + request.getRoomType() + "']: " + e.getMessage());
            }
        }
    }
}

public class UseCase9ErrorHandlingValidation {
    public static void main(String[] args) {
        System.out.println("--- Use Case 9: Error Handling & Validation ---");

        InventoryService inventory = new InventoryService();
        Validator validator = new Validator(new HashSet<>(Arrays.asList("Single", "Double", "Suite")));
        BookingHistory history = new BookingHistory();
        RoomAllocator allocator = new RoomAllocator();
        BookingService bookingService = new BookingService(inventory, validator, history, allocator);

        bookingService.addRequest(new BookingRequest("Alice", "Single"));
        bookingService.addRequest(new BookingRequest("", "Single"));
        bookingService.addRequest(new BookingRequest("Bob", "Triple"));
        bookingService.addRequest(new BookingRequest("Charlie", "Suite"));
        bookingService.addRequest(new BookingRequest("David", "Single"));
        bookingService.addRequest(new BookingRequest("Eve", "Single"));

        bookingService.processAll();

        System.out.println();
        inventory.display();

        System.out.println("Booking history records:");
        for (BookingRecord record : history.all()) {
            System.out.println("  " + record);
        }

        System.out.println("--- End Use Case 9 ---");
=======
import java.util.LinkedList;
import java.util.Queue;

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Guest Name : " + guestName);
        System.out.println("Room Type  : " + roomType);
    }
}

class BookingRequestQueueManager {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueueManager() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    public void displayRequests() {
        System.out.println("\n=== Current Booking Request Queue ===");
        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests in the queue.");
            return;
        }

        for (Reservation reservation : requestQueue) {
            reservation.displayReservation();
            System.out.println("-----------------------------");
        }
    }
}

public class BookingRequestQueue {
    public static void main(String[] args) {

        BookingRequestQueueManager bookingQueue = new BookingRequestQueueManager();

        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        Reservation r3 = new Reservation("Charlie", "Suite Room");
        Reservation r4 = new Reservation("David", "Single Room");

        System.out.println("=== Book My Stay App ===");
        System.out.println("Use Case 5: Booking Request Queue (First-Come-First-Served)\n");

        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);
        bookingQueue.addRequest(r4);

        bookingQueue.displayRequests();

    }
}
