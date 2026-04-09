import java.util.*;
import java.util.concurrent.*;

class BookingRequest {
    private final String guestName;
    private final String roomType;

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

class BookingResult {
    private final String guest;
    private final String roomType;
    private final String roomId;

    public BookingResult(String guest, String roomType, String roomId) {
        this.guest = guest;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "[" + guest + ", " + roomType + ", " + roomId + "]";
    }
}

class ConcurrentBookingSystem {
    private final Queue<BookingRequest> requests;
    private final Map<String, Integer> inventory;
    private final Set<String> allocatedRoomIds;

    public ConcurrentBookingSystem(List<BookingRequest> initialRequests) {
        this.requests = new LinkedList<>(initialRequests);
        this.inventory = new LinkedHashMap<>();
        this.inventory.put("Single", 3);
        this.inventory.put("Double", 2);
        this.inventory.put("Suite", 1);
        this.allocatedRoomIds = new HashSet<>();
    }

    public BookingRequest fetchRequest() {
        synchronized (requests) {
            return requests.poll();
        }
    }

    public BookingResult attemptBooking(BookingRequest request) {
        synchronized (this) {
            String roomType = request.getRoomType();
            if (!inventory.containsKey(roomType) || inventory.get(roomType) <= 0) {
                return null;
            }

            String roomId;
            do {
                roomId = roomType.substring(0, 1).toUpperCase() + (int) (Math.random() * 1000);
            } while (allocatedRoomIds.contains(roomId));

            allocatedRoomIds.add(roomId);
            inventory.put(roomType, inventory.get(roomType) - 1);
            return new BookingResult(request.getGuestName(), roomType, roomId);
        }
    }

    public Map<String, Integer> getInventorySnapshot() {
        synchronized (this) {
            return new LinkedHashMap<>(inventory);
        }
    }

    public int getAllocatedCount() {
        synchronized (this) {
            return allocatedRoomIds.size();
        }
    }
}

class BookingWorker implements Runnable {
    private final ConcurrentBookingSystem system;
    private final List<BookingResult> confirmations;

    public BookingWorker(ConcurrentBookingSystem system, List<BookingResult> confirmations) {
        this.system = system;
        this.confirmations = confirmations;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest request = system.fetchRequest();
            if (request == null) {
                break;
            }
            BookingResult result = system.attemptBooking(request);
            if (result != null) {
                synchronized (confirmations) {
                    confirmations.add(result);
                }
            } else {
                System.out.println("No inventory for " + request.getGuestName() + " (" + request.getRoomType() + ")");
            }
        }
    }
}

public class UseCase11ConcurrentBookingSimulation {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- Use Case 11: Concurrent Booking Simulation (Thread Safety) ---");

        List<BookingRequest> requests = Arrays.asList(
                new BookingRequest("Alice", "Single"),
                new BookingRequest("Bob", "Double"),
                new BookingRequest("Charlie", "Single"),
                new BookingRequest("David", "Suite"),
                new BookingRequest("Emma", "Single"),
                new BookingRequest("Frank", "Double"),
                new BookingRequest("Grace", "Single"),
                new BookingRequest("Helen", "Double"),
                new BookingRequest("Ivan", "Single"),
                new BookingRequest("Judy", "Suite")
        );

        ConcurrentBookingSystem system = new ConcurrentBookingSystem(requests);
        List<BookingResult> confirmations = Collections.synchronizedList(new ArrayList<>());

        int workerCount = 4;
        Thread[] workers = new Thread[workerCount];
        for (int i = 0; i < workerCount; i++) {
            workers[i] = new Thread(new BookingWorker(system, confirmations), "Worker-" + (i + 1));
            workers[i].start();
        }

        for (Thread worker : workers) {
            worker.join();
        }

        System.out.println("\nFinal inventory: " + system.getInventorySnapshot());
        System.out.println("Total confirmed bookings: " + confirmations.size());
        System.out.println("Allocated total (set): " + system.getAllocatedCount());
        System.out.println("Confirmed records:");
        for (BookingResult result : confirmations) {
            System.out.println("  " + result);
        }

        System.out.println("--- End Use Case 11 ---");
    }
}
