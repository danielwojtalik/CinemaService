package cinema_service;


import model.Customer;
import model.Movie;
import model.MovieType;
import model.SalesStand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import repository.impl.CustomerRepository;
import repository.impl.LoyaltyCardRepository;
import repository.impl.MovieRepository;
import repository.impl.SalesStandsRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StatisticsServiceTest {

    private StatisticsService statisticsService;

    private CustomerService customerService;

    private SalesStandsService salesStandsService;
    @Mock
    MovieService movieService;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    SalesStandsRepository salesStandsRepository;
    @Mock
    LoyaltyCardRepository loyaltyCardRepository;
    @Mock
    MovieRepository movieRepository;


    @BeforeEach
    public void beforeEach() {
        salesStandsService = new SalesStandsService(customerRepository, salesStandsRepository, loyaltyCardRepository, movieRepository);
        customerService = new CustomerService(customerRepository);
        statisticsService = new StatisticsService(salesStandsService, customerService, movieService);
    }

    @Test
    public void retrieveCustomerWithMoviesTest() {
        //given
        Customer c1 = Customer.builder().name("N1").surname("S1").build();
        Customer c2 = Customer.builder().name("N2").surname("S2").build();

        Movie m1 = Movie.builder().title("M1").build();
        Movie m2 = Movie.builder().title("M2").build();
        Movie m3 = Movie.builder().title("M3").build();

        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));
        when(salesStandsRepository.findAllMoviesForCustomer(c1)).thenReturn(List.of(m1, m3));
        when(salesStandsRepository.findAllMoviesForCustomer(c2)).thenReturn(List.of(m2));
        //when
        Map<Customer, List<Movie>> customerWithMovies = statisticsService.retrieveCustomerWithMovies();
        //then
        Map<Customer, List<Movie>> expectedCustomerWithMovies = Map.of(
                c1, List.of(m1, m3),
                c2, List.of(m2)
        );

        Assertions.assertEquals(expectedCustomerWithMovies, customerWithMovies);
    }

    @Test
    public void retrieveBestCustomerForCategoryTest() {

        //given
        Customer c1 = Customer.builder().name("N1").surname("S1").build();
        Customer c2 = Customer.builder().name("N2").surname("S2").build();
        Customer none = Customer.builder().id(0).name("NONE").surname("NONE").build();

        Movie m1 = Movie.builder().title("M1").genre(MovieType.COMEDY).price(new BigDecimal("15")).build();
        Movie m2 = Movie.builder().title("M2").genre(MovieType.THRILLER).price(new BigDecimal("25")).build();
        Movie m3 = Movie.builder().title("M3").genre(MovieType.DRAMA).price(new BigDecimal("30")).build();
        Movie m4 = Movie.builder().title("M4").genre(MovieType.COMEDY).price(new BigDecimal("5")).build();


        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));
        when(salesStandsRepository.findAllMoviesForCustomer(c1))
                .thenReturn(List.of(m1, m2, m3, m4));
        when(salesStandsRepository.findAllMoviesForCustomer(c2))
                .thenReturn(List.of(m2, m2));
        //when
        Map<MovieType, Customer> movieTypeWithBestCustomer = statisticsService.retrieveBestCustomerForCategory();
        Map<MovieType, Customer> expectedMovieTypeWithBestCustomer = Map.of(
                MovieType.COMEDY, c1,
                MovieType.DRAMA, c1,
                MovieType.SCI_FI, none,
                MovieType.THRILLER, c2
        );
        //then
        Assertions.assertEquals(expectedMovieTypeWithBestCustomer, movieTypeWithBestCustomer);
    }

    static Stream<Arguments> itemsProvider() {
        return Stream.of(
                Arguments.of(LocalDate.of(1990, 1, 1), LocalDate.of(1999, 12, 31),
                        Map.of(
                                MovieType.COMEDY, 2L,
                                MovieType.DRAMA, 1L,
                                MovieType.SCI_FI, 1L,
                                MovieType.THRILLER, 1L
                        )),
                Arguments.of(LocalDate.of(2000, 1, 1), LocalDate.of(2004, 12, 31),
                        Map.of(
                                MovieType.COMEDY, 1L,
                                MovieType.SCI_FI, 1L
                        )),
                Arguments.of(LocalDate.of(2005, 1, 1), LocalDate.of(2009, 1, 1),
                        Map.of()
                ));
    }

    @ParameterizedTest
    @MethodSource("itemsProvider")

    public void retrieveAllTicketSalesInEachCategoryInTimeRangeTest(LocalDate startDate, LocalDate finishDate, Map<MovieType, Long> numberOfMovieInCategory) {
        // given
        when(salesStandsRepository.findMoviesInConcreteTimeRange(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(List.of(
                        Movie.builder().title("M1").genre(MovieType.COMEDY).releaseDate(LocalDate.of(1997, 4, 27)).build(),
                        Movie.builder().title("M2").genre(MovieType.COMEDY).releaseDate(LocalDate.of(1990, 9, 15)).build(),
                        Movie.builder().title("M3").genre(MovieType.DRAMA).releaseDate(LocalDate.of(1994, 5, 12)).build(),
                        Movie.builder().title("M4").genre(MovieType.SCI_FI).releaseDate(LocalDate.of(1991, 11, 15)).build(),
                        Movie.builder().title("M5").genre(MovieType.THRILLER).releaseDate(LocalDate.of(1999, 9, 23)).build(),
                        Movie.builder().title("M6").genre(MovieType.COMEDY).releaseDate(LocalDate.of(2001, 9, 17)).build(),
                        Movie.builder().title("M7").genre(MovieType.SCI_FI).releaseDate(LocalDate.of(2004, 5, 11)).build()

                )
                        .stream()
                        .filter(movie -> movie.getReleaseDate().compareTo(startDate) >= 0 && movie.getReleaseDate().compareTo(finishDate) <= 0)
                        .collect(Collectors.toList()));

        // when
        Map<MovieType, Long> numberOfMoviesInEachCategory = statisticsService.retrieveAllTicketSalesInEachCategoryInTimeRange(
                startDate.atStartOfDay(), finishDate.atTime(LocalTime.MAX));
        // then
        Assertions.assertEquals(numberOfMovieInCategory, numberOfMoviesInEachCategory);
    }

    @Test
    public void retrieveNumberOfParticularMovieForClient() {
        //given
        Customer c1 = Customer.builder().name("N1").surname("S1").build();
        Customer c2 = Customer.builder().name("N2").surname("S2").build();

        Movie m1 = Movie.builder().title("M1").genre(MovieType.COMEDY).build();
        Movie m2 = Movie.builder().title("M2").genre(MovieType.THRILLER).build();
        Movie m3 = Movie.builder().title("M3").genre(MovieType.DRAMA).build();
        Movie m4 = Movie.builder().title("M4").genre(MovieType.SCI_FI).build();


        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));
        when(salesStandsRepository.findAllMoviesForCustomer(c1))
                .thenReturn(List.of(m1, m2, m3));
        when(salesStandsRepository.findAllMoviesForCustomer(Customer.builder().name("N2").surname("S2").build()))
                .thenReturn(List.of(m4, m4));

        //when
        Map<Customer, Map<Movie, Long>> numberOfEachMovieForClient = statisticsService.retrieveNumberOfEachMoviesForClient();
        //then
        Assertions.assertEquals(Map.of(
                c1, Map.of(
                        m1, 1L,
                        m2, 1L,
                        m3, 1L
                ),
                c2, Map.of(
                        m4, 2L
                )),
                numberOfEachMovieForClient
        );
    }

    @Test
    public void retrieveTotalTicketPriceSoldInCinemaTest() {
        //given
        when(salesStandsRepository.findAll()).thenReturn(List.of(
                SalesStand.builder().priceWithDiscount(new BigDecimal("120")).build(),
                SalesStand.builder().priceWithDiscount(new BigDecimal("80")).build(),
                SalesStand.builder().priceWithDiscount(new BigDecimal("50")).build()
        ));
        //when
        BigDecimal totalPriceForTickets = statisticsService.retrieveTotalTicketPriceSoldInCinema();
        //then
        Assertions.assertEquals(totalPriceForTickets, new BigDecimal("250"));
    }
}








