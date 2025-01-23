package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    private User user;
    private List<User> userList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERS_PATH = "app/src/main/java/ticket/booking/localDb/users.json";

    public UserBookingService(User user1) throws IOException {
        this.user = user1;
        this.userList = loadUsers();
    }

    public UserBookingService() throws IOException {
        this.userList = loadUsers();
    }

    public List<User> loadUsers() throws IOException {
        File users = new File(USERS_PATH);
        if (users.exists()) {
            return objectMapper.readValue(users, new TypeReference<List<User>>() {});
        } else {
            return new ArrayList<>();
        }
    }

    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        if (foundUser.isPresent()) {
            this.user = foundUser.get();
            return true;
        }
        return false;
    }

    public Boolean signUp(User user1) {
        try {
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException ex) {
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
        File userFile = new File(USERS_PATH);
        objectMapper.writeValue(userFile, userList);
    }

    public void fetchBooking() {
        if (user == null) {
            System.out.println("User not logged in. Please log in first.");
            return;
        }
        if (user.getTickets() != null && !user.getTickets().isEmpty()) {
            user.printTickets();
        } else {
            System.out.println("No tickets found.");
        }
    }

    public Boolean cancelBooking(String ticketId) {
        if (user == null) {
            System.out.println("User not logged in. Please log in first.");
            return Boolean.FALSE;
        }
        if (user.getTickets() != null && user.getTickets().removeIf(ticket -> ticket.equals(ticketId))) {
            try {
                saveUserListToFile();
                return Boolean.TRUE;
            } catch (IOException ex) {
                return Boolean.FALSE;
            }
        }
        return Boolean.FALSE;
    }

    public List<Train> searchTrains(String source, String destination) {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void bookSeat(String trainId, int seatNumber) {
        if (user == null) {
            System.out.println("User not logged in. Please log in first.");
            return;
        }
        if (user.getTickets() == null) {
            user.setTickets(new ArrayList<>());
        }
        user.getTickets().add(trainId + "-" + seatNumber);
        try {
            saveUserListToFile();
            System.out.println("Seat booked successfully!");
        } catch (IOException e) {
            System.out.println("Error booking seat: " + e.getMessage());
        }
    }
}