package org.example;

import org.example.dto.DataSetupResponse;
import org.example.enums.Language;
import org.example.enums.MovieFormat;
import org.example.enums.SeatType;
import org.example.interfaces.SeatLockProvider;
import org.example.interfaces.pricingstrategy.EveningShowPricingStrategy;
import org.example.models.*;
import org.example.service.*;
import store.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseTest {
    // Stores
    protected BookingStore bookingStore;
    protected MovieStore movieStore;
    protected ScreenStore screenStore;
    protected ShowStore showStore;
    protected TheatreStore theatreStore;
    // Services
    protected TheatreService theatreService;
    protected ShowService showService;
    protected MovieService movieService;
    protected BookingService bookingService;
    protected PaymentService paymentService;
    protected SeatAvailabilityService seatAvailabilityService;
    protected SeatLockProvider seatLockProvider;

    protected void setupStore() {
        bookingStore = new BookingStore();
        movieStore = new MovieStore();
        screenStore = new ScreenStore();
        showStore = new ShowStore();
        theatreStore = new TheatreStore();
    }

    protected void setupControllers() {
        theatreService = new TheatreService(theatreStore, screenStore);
        movieService = new MovieService(movieStore);
        seatLockProvider = new InMemorySeatLockService(5);
        bookingService = new BookingService(bookingStore, seatLockProvider, showStore);
        seatAvailabilityService = new SeatAvailabilityService(seatLockProvider, bookingService);
        showService = new ShowService(showStore, movieStore, theatreService, seatAvailabilityService);
        paymentService = new PaymentService(bookingService, bookingStore);
    }


    protected DataSetupResponse setupData() {
        DataSetupResponse setupResponse = new DataSetupResponse();
        addTheatres(setupResponse);
        addMoviesAndShows(setupResponse);
        return setupResponse;
    }

    protected void addTheatres(DataSetupResponse dataSetupResponse) {
        Theatre theatre1 = theatreService.addTheatre("Sony World", new Location("Amritsar"));
        Screen t1Screen1 = theatreService.addScreen(
                theatre1.getId(),
                "Screen 1",
                Map.of(
                        SeatType.SILVER, 2,
                        SeatType.GOLD, 3
                )
        );
        Theatre theatre2 = theatreService.addTheatre("Phoenix World", new Location("Bengaluru"));
        Theatre theatre3 = theatreService.addTheatre("VR city", new Location("Surat"));
        dataSetupResponse.testSeatIds.addAll(
                t1Screen1.getSeats().stream().map(Seat::getId).toList()
        );
        dataSetupResponse.testScreenId = t1Screen1.getId();
    }

    protected void addMoviesAndShows(DataSetupResponse setupResponse) {
        Movie movie1 = movieService.addMovie("3 Idiots");
        Show show1 = showService.addShow(
                movie1.getId(),
                setupResponse.testScreenId,
                LocalDateTime.now(),
                Duration.ofHours(2),
                Language.HINDI,
                MovieFormat.FORMAT_2D,
                new EveningShowPricingStrategy()
        );
        setupResponse.testShowId = show1.getId();
    }

}
