import java.util.*;

class BookingRecord {
    private String guestName;
    private String roomType;
    private String roomId;
    private Date bookingTime;

    public BookingRecord(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.bookingTime = new Date();
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

    public Date getBookingTime() {
        return bookingTime;
    }

    @Override
    public String toString() {
        return "BookingRecord{" +
                "guestName='" + guestName + '\'' +
                ", roomType='" + roomType + '\'' +
                ", roomId='" + roomId + '\'' +
                ", bookingTime=" + bookingTime +
                '}';
    }
}

class BookingHistory {
    private List<BookingRecord> bookings;

    public BookingHistory() {
        this.bookings = new ArrayList<>();
    }

    public void add(BookingRecord booking) {
        bookings.add(booking);
    }

    public List<BookingRecord> getAll() {
        return Collections.unmodifiableList(bookings);
    }

    public List<BookingRecord> getByRoomType(String roomType) {
        List<BookingRecord> filtered = new ArrayList<>();
        for (BookingRecord booking : bookings) {
            if (booking.getRoomType().equalsIgnoreCase(roomType)) {
                filtered.add(booking);
            }
        }
        return Collections.unmodifiableList(filtered);
    }

    public int size() {
        return bookings.size();
    }

class BookingReportService {
    public void printHistory(BookingHistory bookingHistory) {
        System.out.println("-- Booking History (chronological) --");
        for (BookingRecord record : bookingHistory.getAll()) {
            System.out.println(record);
        }
    }

    public void printSummary(BookingHistory bookingHistory) {
        System.out.println("-- Booking Summary Report --");
        System.out.println("Total confirmed bookings: " + bookingHistory.size());

        Map<String, Integer> countByType = new HashMap<>();
        for (BookingRecord record : bookingHistory.getAll()) {
            countByType.put(record.getRoomType(), countByType.getOrDefault(record.getRoomType(), 0) + 1);
        }

        System.out.println("Confirmed bookings by room type:");
        for (Map.Entry<String, Integer> entry : countByType.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }

        if (bookingHistory.size() > 0) {
            BookingRecord first = bookingHistory.getAll().get(0);
            BookingRecord last = bookingHistory.getAll().get(bookingHistory.size() - 1);
            System.out.println("First confirmed booking: " + first.getGuestName() + " (" + first.getRoomId() + ")");
            System.out.println("Last confirmed booking: " + last.getGuestName() + " (" + last.getRoomId() + ")");
        }
    }
}

class InventoryServiceNew {
    private Map<String, Integer> inventory;

    public InventoryServiceNew() {
        inventory = new LinkedHashMap<>();
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
        System.out.println("Inventory status: " + inventory);
    }
}

class BookingServiceWithHistory {
    private Queue<BookingRequest> requestQueue = new LinkedList<>();
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();
    private Set<String> allAllocatedIds = new HashSet<>();

    private InventoryServiceNew inventory;
    private BookingHistory bookingHistory;

    public BookingServiceWithHistory(InventoryServiceNew inventory, BookingHistory bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }

    public void addRequest(String guestName, String roomType) {
        requestQueue.offer(new BookingRequest(guestName, roomType));
    }

    private String generateRoomId(String roomType) {
        String id;
        do {
            id = roomType.substring(0, 1).toUpperCase() + (int) (Math.random() * 1000);
        } while (allAllocatedIds.contains(id));
        return id;
    }

    public void processBookings() {
        while (!requestQueue.isEmpty()) {
            BookingRequest req = requestQueue.poll();
            String type = req.getRoomType();

            if (inventory.hasAvailability(type)) {
                String roomId = generateRoomId(type);
                allAllocatedIds.add(roomId);

                allocatedRooms.putIfAbsent(type, new HashSet<>());
                allocatedRooms.get(type).add(roomId);

                inventory.decrement(type);

                BookingRecord record = new BookingRecord(req.getCustomerName(), type, roomId);
                bookingHistory.add(record);

                System.out.println("Reservation confirmed for " + req.getCustomerName() + " -> Room " + roomId);
            } else {
                System.out.println("No rooms available for " + req.getCustomerName() + " (" + type + ")");
            }
        }
    }

    public void displayAllocations() {
        System.out.println("Allocated rooms: " + allocatedRooms);
    }
}

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

public class UseCase8BookingHistoryReport {
    public static void main(String[] args) {
        System.out.println("--------------- Use Case 8: Booking History & Reporting ---------------");

        InventoryServiceNew inventory = new InventoryServiceNew();
        BookingHistory bookingHistory = new BookingHistory();
        BookingServiceWithHistory bookingService = new BookingServiceWithHistory(inventory, bookingHistory);
        BookingReportService reportService = new BookingReportService();

        bookingService.addRequest("Alice", "Single");
        bookingService.addRequest("Bob", "Double");
        bookingService.addRequest("Charlie", "Single");
        bookingService.addRequest("David", "Suite");
        bookingService.addRequest("Eva", "Single");
        bookingService.addRequest("Frank", "Double");

        bookingService.processBookings();

        System.out.println();
        bookingService.displayAllocations();
        inventory.displayInventory();

        System.out.println();
        reportService.printHistory(bookingHistory);

        System.out.println();
        reportService.printSummary(bookingHistory);

        System.out.println("--------------- End of Use Case 8 ---------------");
    }
}
