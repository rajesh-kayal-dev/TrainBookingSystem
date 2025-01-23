package ticket.booking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.services.UserBookingService;
import ticket.booking.services.TrainService;
import ticket.booking.util.UserServiceUtil;

public class App {
    public static void main(String[] args) {
        System.out.println("Train Booking System");
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService;
        TrainService trainService;
        try {
            userBookingService = new UserBookingService();
            trainService = new TrainService();
        } catch (IOException e) {
            System.out.println("Error in initializing the service: " + e.getMessage());
            return;
        }

        while (option != 7) {
            System.out.println("Choose an option:");
            System.out.println("1. Sign Up");
            System.out.println("2. Sign In");
            System.out.println("3. Fetch Booking");
            System.out.println("4. Search Train");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel Booking");
            System.out.println("7. Exit");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Enter your name:");
                    String nameToSignUp = scanner.next();
                    System.out.println("Enter your password:");
                    String passwordToSignUp = scanner.next();
                    String hashedPassword = UserServiceUtil.hashPassword(passwordToSignUp);
                    User newUser = new User(nameToSignUp, passwordToSignUp, hashedPassword, new ArrayList<>(), UUID.randomUUID().toString());
                    userBookingService.signUp(newUser);
                    System.out.println("Sign-up successful!");
                    break;

                case 2:
                    System.out.println("Enter your name:");
                    String nameToLogin = scanner.next();
                    System.out.println("Enter your password:");
                    String passwordToLogin = scanner.next();
                    User loginUser = new User(nameToLogin, passwordToLogin, "");
                    try {
                        userBookingService = new UserBookingService(loginUser);
                        boolean loginSuccess = userBookingService.loginUser();
                        System.out.println(loginSuccess ? "Login successful!" : "Invalid credentials.");
                    } catch (IOException e) {
                        System.out.println("Error during login: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("Fetching booking");
                    userBookingService.fetchBooking();
                    break;

                case 4:
                    System.out.println("Enter source station:");
                    Set<String> stations = trainService.getAllStations();
                    System.out.println("Available stations: " + stations);
                    String source = scanner.next();
                    System.out.println("Enter destination station:");
                    String destination = scanner.next();
                    List<Train> availableTrains = userBookingService.searchTrains(source, destination);
                    System.out.println("Available trains: ");
                    availableTrains.forEach(System.out::println);
                    break;

                case 5:
                    System.out.println("Enter train ID:");
                    String trainId = scanner.next();
                    System.out.println("Enter seat number:");
                    int seatNumber = scanner.nextInt();
                    userBookingService.bookSeat(trainId, seatNumber);
                    break;

                case 6:
                    System.out.println("Enter ticket ID to cancel (format: trainId-seatNumber):");
                    String ticketId = scanner.next();
                    boolean cancelSuccess = userBookingService.cancelBooking(ticketId);
                    System.out.println(cancelSuccess ? "Booking cancelled successfully!" : "Failed to cancel booking.");
                    break;

                case 7:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}