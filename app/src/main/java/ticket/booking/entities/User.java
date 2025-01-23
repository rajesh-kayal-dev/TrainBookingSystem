package ticket.booking.entities;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String name;
    private String password;
    private String hashedPassword;
    private List<String> tickets;
    private String userId;

    // Default constructor
    public User() {
    }

    public User(String name, String password, String hashedPassword, List<String> tickets, String userId) {
        this.name = name;
        this.password = password;
        this.hashedPassword = hashedPassword;
        this.tickets = tickets;
        this.userId = userId;
    }

    public User(String name, String password, String hashedPassword) {
        this.name = name;
        this.password = password;
        this.hashedPassword = hashedPassword;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public List<String> getTickets() {
        return tickets;
    }

    public void setTickets(List<String> tickets) {
        this.tickets = tickets;
    }

    public String getUserId() {
        return userId;
    }

    public void printTickets() {
        System.out.println("Tickets: " + tickets);
    }
}