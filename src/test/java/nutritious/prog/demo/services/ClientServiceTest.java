package nutritious.prog.demo.services;

import nutritious.prog.demo.exceptions.InvalidArgumentException;
import nutritious.prog.demo.exceptions.ObjectAlreadyExistsException;
import nutritious.prog.demo.exceptions.ObjectNotFoundException;
import nutritious.prog.demo.model.Address;
import nutritious.prog.demo.model.Client;
import nutritious.prog.demo.repositories.AddressRepository;
import nutritious.prog.demo.repositories.ClientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private AddressRepository addressRepository;
    private AutoCloseable autoCloseable;
    private ClientService underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ClientService(clientRepository, addressRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    Address testAddress = new Address("Ladna 13c", "Lodz", "Lodzkie", "98-078", "Polska");
    Address testAddress2 = new Address("Colourful 5b", "London", "London", "12345", "England");

    @Test
    void oneArgConstructorTest(){
        ClientService clientService = new ClientService(clientRepository, addressRepository);
        assertThat(clientService.getRepo()).isEqualTo(clientRepository);
        assertThat(clientService.getAddressRepo()).isEqualTo(addressRepository);
    }

    @Test
    void saveClient() {
        //given
        Client c = new Client("Jan Kowalski", testAddress, 0.3);

        //when
        underTest.saveClient("Jan Kowalski", testAddress, 0.3);

        //then
        ArgumentCaptor<Client> argumentCaptor = ArgumentCaptor.forClass(Client.class);

        verify(clientRepository).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).isEqualTo(c);
    }

    @Test
    void saveClientWithObjectParameter(){
        Client c = new Client("Jan Kowalski", testAddress, 0.3);

        underTest.saveClient(c);

        ArgumentCaptor<Client> argumentCaptor = ArgumentCaptor.forClass(Client.class);

        verify(clientRepository).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).isEqualTo(c);
    }

    @Test
    void saveClientWithNullParameter(){
        assertThatThrownBy(
                () -> underTest.saveClient(null))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid argument.");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void saveClientThrowingExcWhenInvalidName(){
        assertThatThrownBy(
                () -> underTest.saveClient("", testAddress, 0.3))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void saveClientThrowingExcWhenInvalidAddress(){
        assertThatThrownBy(
                () -> underTest.saveClient("Jan Kowalski", null, 0.3))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void saveClientThrowingExcWhenInvalidDiscount(){
        assertThatThrownBy(
                () -> underTest.saveClient("Jan Kowalski", testAddress, -0.3))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void saveClientThrowingExcWhenClientAlreadyExists(){
        Client c = new Client("Jan Kowalski", testAddress, 0.3);
        Client c2 = new Client("Piotr Nowak", testAddress2, 0.2);
        List<Client> clients = new ArrayList<>();
        clients.add(c);
        clients.add(c2);

        underTest.saveClient("Jan Kowalski", testAddress, 0.3);

        given(clientRepository.findAll()).willReturn(clients);

        assertThatThrownBy(
                () -> underTest.saveClient("Jan Kowalski", testAddress, 0.3))
                .isInstanceOf(ObjectAlreadyExistsException.class)
                .hasMessageContaining("Object with the same data already exists in db.");

        verify(clientRepository, times(1)).save(any());
    }

    @Test
    void saveClientObjectParameterThrowingExcWhenClientAlreadyExists(){
        Client c = new Client("Jan Kowalski", testAddress, 0.3);
        Client c2 = new Client("Jan Kowalski", testAddress, 0.3);
        List<Client> clients = new ArrayList<>();
        clients.add(c);
        clients.add(c2);

        underTest.saveClient(c);

        given(clientRepository.findAll()).willReturn(clients);

        assertThatThrownBy(
                () -> underTest.saveClient(c2))
                .isInstanceOf(ObjectAlreadyExistsException.class)
                .hasMessageContaining("Object with the same data already exists in db.");

        verify(clientRepository, times(1)).save(any());
    }

    @Test
    void deleteClient() {
        // given
        long id = 10;
        given(clientRepository.existsById(id)).willReturn(true);
        // when
        underTest.deleteClient(id);
        // then
        verify(clientRepository).deleteById(id);
    }

    @Test
    void deleteClientThrowingExcWhenIdLessThanZero(){
        long id = -10;

        assertThatThrownBy(() -> underTest.deleteClient(id))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(clientRepository, never()).deleteById(any());
    }

    @Test
    void deleteClientThrowingExcWhenClientNotFound(){
        long id = 10;
        given(clientRepository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> underTest.deleteClient(id))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Object not found in db.");

        verify(clientRepository, never()).deleteById(any());
    }

    @Test
    void updateClientName() {
        Client c = new Client("Jan Kowalski", testAddress, 0.3);
        long id = 10;
        given(clientRepository.findById(id)).willReturn(Optional.of(c));

        String newName = "Piotr Skarga";

        underTest.updateClientName(id, newName);

        assertThat(c.getName()).isEqualTo(newName);

        verify(clientRepository).save(c);
    }

    @Test
    void updateClientNameThrowingExcWhenInvalidName() {
        assertThatThrownBy(
                () -> underTest.updateClientName(10L,""))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void updateClientNameThrowingExcWhenInvalidId() {
        assertThatThrownBy(
                () -> underTest.updateClientName(-10L,"Michal Pacan"))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void updateClientAddress() {
        Client c = new Client("Jan Kowalski", testAddress, 0.3);
        long id = 10;
        given(clientRepository.findById(id)).willReturn(Optional.of(c));

        Address newAddress = new Address("Colourful 5b", "London", "London", "12345", "England");

        underTest.updateClientAddress(id, newAddress);

        assertThat(c.getAddress()).isEqualTo(newAddress);

        verify(clientRepository).save(c);
    }

    @Test
    void updateClientAddressThrowingExcWhenNullAddressParam() {
        assertThatThrownBy(
                () -> underTest.updateClientAddress(10L,null))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void updateClientAddressThrowingExcWhenInvalidId() {
        assertThatThrownBy(
                () -> underTest.updateClientAddress(-10L,testAddress))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void updateClientAddressNotSavingNewAddressWhenAddressAlreadyExists(){
        Client c = new Client("Jan Kowalski", testAddress, 0.3);

        List<Address> addresses = new ArrayList<>();
        addresses.add(testAddress);
        addresses.add(testAddress2);

        long id = 10;
        given(clientRepository.findById(id)).willReturn(Optional.of(c));

        given(addressRepository.findAll()).willReturn(addresses);

        assertThat(underTest.updateClientAddress(id, testAddress2)).isTrue();
    }

    @Test
    void updateClientDiscount() {
        Client c = new Client("Jan Kowalski", testAddress, 0.3);

        long id = 10;

        given(clientRepository.findById(id)).willReturn(Optional.of(c));

        double newDiscount = 0.2;

        underTest.updateClientDiscount(id, newDiscount);

        assertThat(c.getDiscount()).isEqualTo(newDiscount);

        verify(clientRepository).save(c);
    }

    @Test
    void updateClientDiscountThrowingExcWhenNullAddressParam() {
        assertThatThrownBy(
                () -> underTest.updateClientDiscount(10L,-0.3))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void updateClientDiscountThrowingExcWhenInvalidId() {
        assertThatThrownBy(
                () -> underTest.updateClientDiscount(-10L,0.3))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void findClientByID() {
        Client c = new Client("Jan Kowalski", testAddress, 0.3);
        long id = 10;

        given(clientRepository.findById(id)).willReturn(Optional.of(c));

        underTest.findClientByID(id);
        verify(clientRepository).findById(id);
    }

    @Test
    void findAllClients(){
        underTest.findAllClients();
        verify(clientRepository).findAll();
    }
}