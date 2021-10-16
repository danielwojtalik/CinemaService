package cinemaservice;

import exceptions.CustomException;
import model.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.impl.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class CustomerServiceTest {

    // ta adnotacja oznacza ze do tego obiektu zostana wstrzykniete inne pola instancyjne klasy testowej oznaczone adnotacja @Mock
    @InjectMocks
    private CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;

    @ParameterizedTest
    @MethodSource("customerProvider")
    void shouldFindCustomerById(Customer customer) {
        // when
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(customer));
        // then
        Assertions.assertEquals(customer, customerService.findCustomerById(1000));
    }

    @Test
    void shouldReturnCustomExceptionIfIdLessThenZero() {
        // when
        Assertions.assertThrows(CustomException.class, () -> customerService.findCustomerById(-1));
    }

    @ParameterizedTest
    @MethodSource("customersProvider")
    void shouldFindCustomerByName(List<Customer> customers) {
        // given

        // when
        when(customerRepository.findBySurname(anyString())).thenReturn(customers);

        // then
        Assertions.assertEquals(customerService.findBySurname("Adam"), customers);
    }

    @Test
    void shouldThrowsExceptionIfNameIsNull() {
        // then
        Assertions.assertThrows(CustomException.class, () -> customerService.findByName(null));
    }

    @ParameterizedTest
    @MethodSource("customersProvider")
    void shouldFindCustomerBySurname(List<Customer> customers) {
        // when
        when(customerRepository.findBySurname(anyString())).thenReturn(customers);

        //then
        Assertions.assertEquals(customers, customerService.findBySurname("anyString"));
    }

    @Test
    void shouldThrowsExceptionIfSurnameIsNull() {
        // then
        Assertions.assertThrows(CustomException.class, () -> customerService.findBySurname(null));
    }

    @ParameterizedTest
    @MethodSource("customersProvider")
    void shouldFindCustomersByAge(List<Customer> customers) {
        // when
        when(customerRepository.findByAge(anyInt())).thenReturn(customers);

        // then
        Assertions.assertEquals(customerService.findByAge(55), customers);
    }

    @ParameterizedTest
    @MethodSource("ageProviderForAgeFailureTest")
    void shouldThrowsExceptionIfAgeIsOutOfRange(Integer age) {
        // then
        Assertions.assertThrows(CustomException.class, () -> customerService.findByAge(age));
    }

    @ParameterizedTest
    @MethodSource("customersProvider")
    void shouldFindAllCustomers(List<Customer> customers) {
        // when
        when(customerRepository.findAll()).thenReturn(customers);

        // then
        Assertions.assertEquals(customers, customerService.findAll());
    }

    @ParameterizedTest
    @MethodSource("customerProvider")
    void shouldFindCustomersByNameSurnameEmail(Customer customer) {
        // when
        when(customerRepository.findByNameSurnameEmail(anyString(), anyString(), anyString())).thenReturn(Optional.of(customer));

        // then
        Assertions.assertEquals(customerService.findByNameSurnameEmail("test", "test", "test@test.com"), customer);
    }

    @ParameterizedTest
    @MethodSource("customersProviderForFailureNameSurnameEmailTest")
    void shouldThrowsErrorIfAtLeastOneOfNameSurnameOrEmailIsNull(String name, String surname, String email) {
        // then
        Assertions.assertThrows(CustomException.class, () -> customerService.findByNameSurnameEmail(name, surname, email));
    }

    @Test
    void shouldThrowCustomExceptionIfAddNullCustomer(){
        // then
        Assertions.assertThrows(CustomException.class, () -> customerService.addCustomer(null));
    }

    @ParameterizedTest
    @MethodSource("customerProvider")
    void shouldUseRepositoryWhenAddUser(Customer customer) {
        // when
        customerService.addCustomer(customer);
        // then
        verify(customerRepository, times(1)).add(customer);
    }

    @Test
    void shouldThrowExceptionIfUpdatedCustomerDoesNotExistInDB() {
        // given
        var customer = Customer.builder()
                .id(142123874)
                .build();
        // when
        when(customerRepository.findById(anyInt())).thenThrow(CustomException.class);

        // then
        Assertions.assertThrows(CustomException.class, () -> customerService.update(customer));
        verify(customerRepository, times(1)).findById(anyInt());
    }

    @ParameterizedTest
    @MethodSource("customerProvider")
    void shouldUpdateCustomer(Customer customer) {
        // when
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(customer));
        when(customerRepository.update(customer)).thenReturn(Optional.of(customer));
        // then
        Assertions.assertEquals(customer, customerService.update(customer));

    }

    @ParameterizedTest
    @MethodSource("customerProvider")
    void shouldReturnDeletedCustomer(Customer customer) {
        // when
        when(customerRepository.deleteByID(anyInt())).thenReturn(Optional.of(customer));
        // then
        Assertions.assertEquals(customer, customerService.deleteById(45));
    }

    @Test
    void shouldThrowExceptionIfCustomerToDeleteHasWrongId() {

        // then
        Assertions.assertThrows(CustomException.class, () -> customerService.deleteById(-3));
    }


    static Stream<Arguments> customersProvider() {
        Customer customer = Customer.builder()
                .id(1)
                .name("Test")
                .surname("Test")
                .age(18)
                .email("test@test.com")
                .loyaltyCardId(18)
                .build();
        final List<Customer> customers = List.of(customer);

        return Stream.of(
                Arguments.of(customers)
        );
    }

    static Stream<Arguments> ageProviderForAgeFailureTest() {

        return Stream.of(Arguments.of(-3), Arguments.of(156));
    }

    static Stream<Arguments> customerProvider() {
        Customer customer = Customer.builder()
                .id(34)
                .name("Test2")
                .surname("Test2")
                .age(-3)
                .email("test2@test.com")
                .loyaltyCardId(18)
                .build();

        Customer customer2 = Customer.builder()
                .id(4)
                .name("Test3")
                .surname("Test3")
                .age(150)
                .email("test3@test.com")
                .loyaltyCardId(56)
                .build();

        return Stream.of(Arguments.of(customer, customer2));
    }

    static Stream<Arguments> customersProviderForFailureNameSurnameEmailTest() {
        return Stream.of(
                Arguments.of(null, "test", "test"),
                Arguments.of("test", null, "test"),
                Arguments.of("test", "test", null)
        );
    }

}