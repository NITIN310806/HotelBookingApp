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
