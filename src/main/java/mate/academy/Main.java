package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import mate.academy.lib.Injector;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;

public class Main {
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");

        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);

        Movie fastAndFurious2 = new Movie("Fast and Furious");
        fastAndFurious2.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious2);

        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        LocalDateTime localDateTime = LocalDateTime.of(2006, Month.MARCH, 23, 10, 32);
        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(fastAndFurious);
        movieSession.setShowTime(localDateTime);
        MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);

        System.out.println("ADDING MOVIE SESSION");
        movieSessionService.add(movieSession);

        System.out.println("GETTING MOVIE SESSION");
        movieSessionService.get(1L);

        System.out.println("GETTING ALL AVAILABLE MOVIE SESSIONS");
        movieSessionService.findAvailableSessions(1L,
                LocalDate.from(localDateTime)).forEach(System.out::println);
    }
}
