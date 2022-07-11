package nutritious.prog.demo.services;

import nutritious.prog.demo.exceptions.InvalidArgumentException;
import nutritious.prog.demo.exceptions.ObjectAlreadyExistsException;
import nutritious.prog.demo.exceptions.ObjectNotFoundException;
import nutritious.prog.demo.model.Address;
import nutritious.prog.demo.repositories.AddressRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;
    private AutoCloseable autoCloseable;
    private AddressService underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AddressService(addressRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void saveAddress() {
        //given
        Address a = new Address("Ladna 13c", "Lodz", "Lodzkie", "98078", "Polska");

        //when
        underTest.saveAddress("Ladna 13c", "Lodz", "Lodzkie", "98078", "Polska");

        //then
        ArgumentCaptor<Address> argumentCaptor = ArgumentCaptor.forClass(Address.class);

        verify(addressRepository).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).isEqualTo(a);
    }

    @Test
    void saveAddressWithInvalidStreet() {
        assertThatThrownBy(
                () -> underTest.saveAddress("", "Lodz", "Lodzkie", "98078", "Polska"))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(addressRepository, never()).save(any());
    }

    @Test
    void saveAddressWithInvalidCity() {
        assertThatThrownBy(
                () -> underTest.saveAddress("Ladna 13c", "", "Lodzkie", "98078", "Polska"))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(addressRepository, never()).save(any());
    }

    @Test
    void saveAddressWithInvalidVoivodeship() {
        assertThatThrownBy(
                () -> underTest.saveAddress("Ladna 13c", "Lodz", "", "98078", "Polska"))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(addressRepository, never()).save(any());
    }

    @Test
    void saveAddressWithInvalidPostalCode() {
        assertThatThrownBy(
                () -> underTest.saveAddress("Ladna 13c", "Lodz", "Lodzkie", "123456", "Polska"))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(addressRepository, never()).save(any());
    }

    @Test
    void saveAddressWithInvalidCountry() {
        assertThatThrownBy(
                () -> underTest.saveAddress("Ladna 13c", "Lodz", "Lodzkie", "98078", ""))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(addressRepository, never()).save(any());
    }

    @Test
    void saveAddressThrowingExcWhenAddressAlreadyExists() {
        Address a1 = new Address("Colourful 5b", "London", "London", "12345", "England");
        Address a2 = new Address("Colourful 5b", "London", "London", "12345", "England");
        List<Address> addresses = new ArrayList<>();
        addresses.add(a1);
        addresses.add(a2);

        underTest.saveAddress("Colourful 5b", "London", "London", "12345", "England");

        given(addressRepository.findAll()).willReturn(addresses);

        assertThatThrownBy(
                () -> underTest.saveAddress("Colourful 5b", "London", "London", "12345", "England"))
                .isInstanceOf(ObjectAlreadyExistsException.class)
                .hasMessageContaining("Object with the same data already exists in db.");

        verify(addressRepository, times(1)).save(any());
    }

    @Test
    void deleteAddress() {
        // given
        long id = 10;
        given(addressRepository.existsById(id)).willReturn(true);
        // when
        underTest.deleteAddress(id);
        // then
        verify(addressRepository).deleteById(id);
    }

    @Test
    void deleteAddressThrowingExcWhenIdLessThanZero(){
        long id = -10;

        assertThatThrownBy(() -> underTest.deleteAddress(id))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(addressRepository, never()).deleteById(any());
    }

    @Test
    void deleteAddressThrowingExcWhenClientNotFound(){
        long id = 10;
        given(addressRepository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> underTest.deleteAddress(id))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Object not found in db.");

        verify(addressRepository, never()).deleteById(any());
    }


    @Test
    void updateAddress() {
        Address a1 = new Address("Colourful 5b", "London", "London", "12345", "England");
        long id = 10;
        given(addressRepository.findById(id)).willReturn(Optional.of(a1));

        String newStreet = "Bright 8a";
        String newCity = "Brighton";
        String newVoivodeship = "Brighton and Hove";
        String newPostalCode = "98765";
        String newCountry = "Ireland";

        underTest.updateAddress(id, newStreet, newCity, newVoivodeship, newPostalCode, newCountry);

        assertThat(a1.getStreet()).isEqualTo(newStreet);
        assertThat(a1.getCity()).isEqualTo(newCity);
        assertThat(a1.getVoivodeship()).isEqualTo(newVoivodeship);
        assertThat(a1.getPostalCode()).isEqualTo(newPostalCode);
        assertThat(a1.getCountry()).isEqualTo(newCountry);

        verify(addressRepository).save(a1);
    }

    @Test
    void updateAddressWithInvalidStreet() {
        assertThatThrownBy(
                () -> underTest.updateAddress(1L,"", "Lodz", "Lodzkie", "98-078", "Polska"))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(addressRepository, never()).save(any());
    }

    @Test
    void updateAddressWithInvalidCity() {
        assertThatThrownBy(
                () -> underTest.updateAddress(1L, "Ladna 13c", "", "Lodzkie", "98-078", "Polska"))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(addressRepository, never()).save(any());
    }

    @Test
    void updateAddressWithInvalidVoivodeship() {
        assertThatThrownBy(
                () -> underTest.updateAddress(1L, "Ladna 13c", "Lodz", "", "98-078", "Polska"))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(addressRepository, never()).save(any());
    }

    @Test
    void updateAddressWithInvalidPostalCode() {
        assertThatThrownBy(
                () -> underTest.updateAddress(1L, "Ladna 13c", "Lodz", "Lodzkie", "123456", "Polska"))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(addressRepository, never()).save(any());
    }

    @Test
    void updateAddressWithInvalidCountry() {
        assertThatThrownBy(
                () -> underTest.updateAddress(1L, "Ladna 13c", "Lodz", "Lodzkie", "98-078", ""))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(addressRepository, never()).save(any());
    }

    @Test
    void findAddressByID() {
        Address a1 = new Address("Colourful 5b", "London", "London", "12345", "England");
        long id = 10;

        given(addressRepository.findById(id)).willReturn(Optional.of(a1));

        underTest.findAddressByID(id);
        verify(addressRepository).findById(id);
    }

    @Test
    void isPostalCodeValidWithValidInput(){
        assertThat(underTest.isPostalCodeValid("12345")).isTrue();
    }

    @Test
    void isPostalCodeValidWithInvalidInput(){
        assertThat(underTest.isPostalCodeValid("12-345")).isFalse();
    }
}