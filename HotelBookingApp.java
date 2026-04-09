import java.io.*;
import java.util.*;

class BookingRecord12 implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String guestName;
    private final String roomType;
    private final String roomId;
    private final Date bookedAt;
    private boolean cancelled;
    private Date cancelledAt;

    public BookingRecord12(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.bookedAt = new Date();
        this.cancelled = false;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        if (cancelled) return;
        this.cancelled = true;
        this.cancelledAt = new Date();
    }

    @Override
    public String toString() {
        String status = cancelled ? "CANCELLED" : "CONFIRMED";
        String when = cancelled ? ", cancelledAt=" + cancelledAt : "";
        return "BookingRecord{guest='" + guestName + "', roomType='" + roomType + "', roomId='" + roomId + "', bookedAt=" + bookedAt + when + ", status=" + status + '}';
    }
}

class InventoryService12 implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<String, Integer> inventory = new LinkedHashMap<>();

    public InventoryService12() {
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

    public void increment(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public Map<String, Integer> getInventory() {
        return Collections.unmodifiableMap(inventory);
    }

    public void display() {
        System.out.println("Inventory: " + inventory);
    }
}

class BookingHistory12 implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<BookingRecord12> history = new ArrayList<>();

    public void add(BookingRecord12 record) {
        history.add(record);
    }

    public List<BookingRecord12> all() {
        return Collections.unmodifiableList(history);
    }

    public void display() {
        System.out.println("Booking History:");
        for (BookingRecord12 rec : history) {
            System.out.println("  " + rec);
        }
    }
}

class PersistenceService12 {
    private static final String FILE_NAME = "system_state.ser";

    public static void saveState(InventoryService12 inventory, BookingHistory12 history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("System state saved to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving state: " + e.getMessage());
        }
    }

    public static SystemState12 loadState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            InventoryService12 inventory = (InventoryService12) ois.readObject();
            BookingHistory12 history = (BookingHistory12) ois.readObject();
            System.out.println("System state loaded from " + FILE_NAME);
            return new SystemState12(inventory, history);
        } catch (FileNotFoundException e) {
            System.out.println("No saved state found, starting with fresh state.");
            return new SystemState12(new InventoryService12(), new BookingHistory12());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading state: " + e.getMessage() + ". Starting with fresh state.");
            return new SystemState12(new InventoryService12(), new BookingHistory12());
        }
    }
}

class SystemState12 {
    private final InventoryService12 inventory;
    private final BookingHistory12 history;

    public SystemState12(InventoryService12 inventory, BookingHistory12 history) {
        this.inventory = inventory;
        this.history = history;
    }

    public InventoryService12 getInventory() {
        return inventory;
    }

    public BookingHistory12 getHistory() {
        return history;
    }
}

class BookingService12 {
    private final InventoryService12 inventory;
    private final BookingHistory12 history;
    private final Set<String> allocatedIds = new HashSet<>();

    public BookingService12(InventoryService12 inventory, BookingHistory12 history) {
        this.inventory = inventory;
        this.history = history;
        // Restore allocated IDs from history
        for (BookingRecord12 record : history.all()) {
            if (!record.isCancelled()) {
                allocatedIds.add(record.getRoomId());
            }
        }
    }

    public void confirmBooking(String guestName, String roomType) {
        if (!inventory.hasAvailability(roomType)) {
            System.out.println("No rooms available for " + guestName + " (" + roomType + ")");
            return;
        }
        String roomId;
        do {
            roomId = roomType.substring(0, 1).toUpperCase() + (int) (Math.random() * 1000);
        } while (allocatedIds.contains(roomId));
        allocatedIds.add(roomId);
        inventory.decrement(roomType);
        BookingRecord12 record = new BookingRecord12(guestName, roomType, roomId);
        history.add(record);
        System.out.println("Confirmed booking: " + guestName + " -> " + roomId + " (" + roomType + ")");
    }
}

public class UseCase12DataPersistenceRecovery {
    public static void main(String[] args) {
        System.out.println("--- Use Case 12: Data Persistence & System Recovery ---");

        // Simulate initial state
        InventoryService12 inventory = new InventoryService12();
        BookingHistory12 history = new BookingHistory12();
        BookingService12 bookingService = new BookingService12(inventory, history);

        bookingService.confirmBooking("Alice", "Single");
        bookingService.confirmBooking("Bob", "Double");
        bookingService.confirmBooking("Charlie", "Single");

        System.out.println("\nBefore persistence:");
        inventory.display();
        history.display();

        // Save state
        PersistenceService12.saveState(inventory, history);

        // Simulate restart by loading state
        System.out.println("\nSimulating system restart...");
        SystemState12 loadedState = PersistenceService12.loadState();
        InventoryService12 loadedInventory = loadedState.getInventory();
        BookingHistory12 loadedHistory = loadedState.getHistory();

        System.out.println("\nAfter recovery:");
        loadedInventory.display();
        loadedHistory.display();

        // Continue operations
        BookingService12 continuedService = new BookingService12(loadedInventory, loadedHistory);
        continuedService.confirmBooking("David", "Suite");

        System.out.println("\nAfter additional booking:");
        loadedInventory.display();
        loadedHistory.display();

        System.out.println("--- End Use Case 12 ---");
    }
}
