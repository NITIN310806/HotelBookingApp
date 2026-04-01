
import java.util.*;

class BookingRecord {
    private final String guestName;
    private final String roomType;
    private final String roomId;
    private final Date bookedAt;
    private boolean cancelled;
    private Date cancelledAt;

    public BookingRecord(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.bookedAt = new Date();
        this.cancelled = false;
=======

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

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return cancelled;
=======
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

    public void cancel() {
        if (cancelled) {
            return;
        }
        this.cancelled = true;
        this.cancelledAt = new Date();
    }

    @Override
    public String toString() {
        String status = cancelled ? "CANCELLED" : "CONFIRMED";
        String when = cancelled ? ", cancelledAt=" + cancelledAt : "";
        return "BookingRecord{" +
                "guest='" + guestName + '\'' +
                ", roomType='" + roomType + '\'' +
                ", roomId='" + roomId + '\'' +
                ", bookedAt=" + bookedAt +
                when +
                ", status=" + status +
                '}';
=======
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


class InventoryService10 {
    private final Map<String, Integer> inventory = new LinkedHashMap<>();

    public InventoryService10() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public boolean hasAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
=======
    public Map<String, Integer> getInventory() {
        return Collections.unmodifiableMap(inventory);
    }

    public void display() {
        System.out.println("Current inventory: " + inventory);

    }


    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void display() {
        System.out.println("Inventory: " + inventory);
    }
}

class RoomAllocator {
    private final Set<String> allocatedIds = new HashSet<>();

    public String allocate(String roomType) {
        String id;
        do {
            id = roomType.substring(0, 1).toUpperCase() + (int) (Math.random() * 1000);
        } while (allocatedIds.contains(id));
        allocatedIds.add(id);
        return id;
    }

    public void release(String roomId) {
        allocatedIds.remove(roomId);
    }
}

class BookingHistory {
    private final List<BookingRecord> history = new ArrayList<>();

    public void add(BookingRecord record) {
        history.add(record);
=======
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


    public Optional<BookingRecord> findActiveByGuestAndRoomId(String guestName, String roomId) {
        return history.stream()
                .filter(r -> !r.isCancelled())
                .filter(r -> r.getGuestName().equals(guestName))
                .filter(r -> r.getRoomId().equals(roomId))
                .findFirst();
    }

    public List<BookingRecord> all() {
        return Collections.unmodifiableList(history);
    }
}

class BookingServiceForCancellation {
    private final InventoryService10 inventory;
    private final RoomAllocator allocator;
    private final BookingHistory history;

    public BookingServiceForCancellation(InventoryService10 inventory, RoomAllocator allocator, BookingHistory history) {
        this.inventory = inventory;
        this.allocator = allocator;
        this.history = history;
    }

    public BookingRecord confirmBooking(String guestName, String roomType) {
        if (!inventory.hasAvailability(roomType)) {
            throw new IllegalStateException("No rooms available for type " + roomType);
        }
        String roomId = allocator.allocate(roomType);
        inventory.decrement(roomType);
        BookingRecord booking = new BookingRecord(guestName, roomType, roomId);
        history.add(booking);
        System.out.println("Confirmed booking: " + guestName + " -> " + roomId + " (" + roomType + ")");
        return booking;
=======
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

import java.util.LinkedList;
import java.util.Queue;

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;

    }
}


class CancellationService {
    private final InventoryService10 inventory;
    private final RoomAllocator allocator;
    private final BookingHistory history;
    private final Stack<String> rollbackStack = new Stack<>();

    public CancellationService(InventoryService10 inventory, RoomAllocator allocator, BookingHistory history) {
        this.inventory = inventory;
        this.allocator = allocator;
        this.history = history;
    }

    public void cancelBooking(String guestName, String roomId) {
        Optional<BookingRecord> optional = history.findActiveByGuestAndRoomId(guestName, roomId);
        if (optional.isEmpty()) {
            System.out.println("Cancellation failed: booking not found or already cancelled (" + guestName + ", " + roomId + ")");
            return;
        }

        BookingRecord record = optional.get();
        record.cancel();

        allocator.release(roomId);
        rollbackStack.push(roomId);

        inventory.increment(record.getRoomType());

        System.out.println("Cancelled booking: " + guestName + " -> " + roomId + " (" + record.getRoomType() + ")");
    }

    public void showRollbackStack() {
        System.out.println("Rollback stack (most recent top): " + rollbackStack);
    }
}

public class UseCase10BookingCancellation {
    public static void main(String[] args) {
        System.out.println("--- Use Case 10: Booking Cancellation & Inventory Rollback ---");

        InventoryService10 inventory = new InventoryService10();
        RoomAllocator allocator = new RoomAllocator();
        BookingHistory history = new BookingHistory();
        BookingServiceForCancellation bookingService = new BookingServiceForCancellation(inventory, allocator, history);
        CancellationService cancellationService = new CancellationService(inventory, allocator, history);

        BookingRecord b1 = bookingService.confirmBooking("Alice", "Single");
        BookingRecord b2 = bookingService.confirmBooking("Bob", "Double");
        BookingRecord b3 = bookingService.confirmBooking("Charlie", "Single");

        System.out.println();
        inventory.display();

        System.out.println();
        cancellationService.cancelBooking("Charlie", b3.getRoomId());
        cancellationService.cancelBooking("Alice", b1.getRoomId());

        System.out.println();
        inventory.display();
        cancellationService.showRollbackStack();

        System.out.println();
        System.out.println("Attempt invalid cancellation:");
        cancellationService.cancelBooking("Alice", b1.getRoomId());
        cancellationService.cancelBooking("Eva", "X999");

        System.out.println();
        System.out.println("Final booking history:");
        for (BookingRecord rec : history.all()) {
            System.out.println("  " + rec);
        }

        System.out.println("--- End Use Case 10 ---");
=======
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
