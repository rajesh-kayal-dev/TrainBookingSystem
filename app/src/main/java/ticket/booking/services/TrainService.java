package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TrainService {
    private List<Train> trainList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAINS_PATH = "app/src/main/java/ticket/booking/localDb/trains.json";

    public TrainService() throws IOException {
        this.trainList = loadTrains();
    }

    public List<Train> loadTrains() throws IOException {
        File trains = new File(TRAINS_PATH);
        if (trains.exists()) {
            return objectMapper.readValue(trains, new TypeReference<List<Train>>() {});
        } else {
            throw new IOException("Train data file not found.");
        }
    }

    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream()
                .filter(train -> validTrain(train, source, destination))
                .collect(Collectors.toList());
    }

    private boolean validTrain(Train train, String source, String destination) {
        List<String> stations = train.getStations();
        int sourceIndex = stations.indexOf(source);
        int destinationIndex = stations.indexOf(destination);
        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }

    public Set<String> getAllStations() {
        return trainList.stream()
                .flatMap(train -> train.getStations().stream())
                .collect(Collectors.toSet());
    }
}