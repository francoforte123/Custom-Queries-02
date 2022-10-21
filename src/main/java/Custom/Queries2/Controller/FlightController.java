package Custom.Queries2.Controller;

import Custom.Queries2.Entities.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/flights")

public class FlightController {

    @Autowired
    FlightRepository flightRepository;

    @PostMapping
    public String createFlights(@RequestParam(required = false) Integer n) {
        Random random = new Random();
        if (n == null) {
            n = 100;
        }
        for (int i = 0; i < n; ++i) {
            Flight flight = new Flight();
            String randomString = random.ints(10, 65, 91)
                    .collect(StringBuilder::new,
                            StringBuilder::appendCodePoint,
                            StringBuilder::append)
                    .toString(); //Generating a Random String with an IntStream
            flight.setDescription(randomString + " " + randomString + " " + randomString);
            flight.setFromAirport(randomString);
            flight.setToAirport(randomString);
            flight.setStatus(FlightStatus.values()[random.nextInt(0, 3)]); //Getting random Status
            flightRepository.save(flight);
        }
        return "Created " + n + " flights successfully!";
    }

    @GetMapping
    public Page<Flight> getAllFlights(@RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            Sort sorting = Sort.by(Sort.Direction.ASC, "fromAirport");
            Pageable pageable = PageRequest.of(page, size, sorting);
            return flightRepository.findAll(pageable);
        } else return Page.empty();
    }

    @GetMapping("/ontime")
    public List<Flight> getFlightsOnTime() {
        return flightRepository.findByStatus(FlightStatus.ONTIME);
    }

    @GetMapping("/status")
    public List<Flight> getFlightsByStatus(@RequestParam String p1, @RequestParam String p2, HttpServletResponse response) {
        List<Flight> flightList = new ArrayList<>();
        p1 = p1.toUpperCase();
        p2 = p2.toUpperCase();
        List<String> statusList = List.of(FlightStatus.ONTIME.toString(),
                FlightStatus.DELAYED.toString(), FlightStatus.CANCELLED.toString());
        if (statusList.contains(p1) && statusList.contains(p2)) {
            flightList.addAll(flightRepository.findByTwoStatus(p1, p2));
        } else response.setStatus(400);
        return flightList;
    }
}
