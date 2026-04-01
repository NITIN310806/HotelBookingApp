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
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return cancelled;
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
    }
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
    }

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
    }
}
