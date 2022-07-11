package nutritious.prog.demo.services;

import nutritious.prog.demo.exceptions.InvalidArgumentException;
import nutritious.prog.demo.exceptions.ObjectNotFoundException;
import nutritious.prog.demo.model.Address;
import nutritious.prog.demo.model.Client;
import nutritious.prog.demo.model.Item;
import nutritious.prog.demo.model.Purchase;
import nutritious.prog.demo.repositories.PurchaseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;
    private AutoCloseable autoCloseable;
    private PurchaseService underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new PurchaseService(purchaseRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    Item testItem = new Item("Apple", 2.50);
    Address testAddress = new Address("Ladna 13c", "Lodz", "Lodzkie", "98-078", "Polska");
    Client testClient = new Client("Jan Kowalski", testAddress, 0.3);

    @Test
    void savePurchase() {
        //given
        Purchase purchase = new Purchase(testItem, testClient, 14.99);

        //when
        underTest.savePurchase(testItem, testClient, 14.99);

        //then
        ArgumentCaptor<Purchase> argumentCaptor = ArgumentCaptor.forClass(Purchase.class);

        verify(purchaseRepository).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).isEqualTo(purchase);
    }

    @Test
    void saveItemWithNullItem() {
        assertThatThrownBy(
                () -> underTest.savePurchase(null, testClient, 14.99))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void saveItemWithNullClient() {
        assertThatThrownBy(
                () -> underTest.savePurchase(testItem, null, 14.99))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void saveItemWithNegativeShippingPrice() {
        assertThatThrownBy(
                () -> underTest.savePurchase(testItem, testClient, -2.50))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void deletePurchase() {
        // given
        long id = 10;
        given(purchaseRepository.existsById(id)).willReturn(true);
        // when
        underTest.deletePurchase(id);
        // then
        verify(purchaseRepository).deleteById(id);
    }

    @Test
    void deletePurchaseWithNegativeId() {
        assertThatThrownBy(
                () -> underTest.deletePurchase(-2L))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(purchaseRepository, never()).deleteById(any());
    }

    @Test
    void deletePurchaseTrowingExcWhenPurchaseNotFound() {
        long id = 10;

        given(purchaseRepository.existsById(id)).willReturn(false);

        assertThatThrownBy(
                () -> underTest.deletePurchase(id))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Object not found in db.");

        verify(purchaseRepository, never()).deleteById(any());
    }

    @Test
    void findPurchaseByID() {
        Purchase p = new Purchase(testItem, testClient, 14.99);
        long id = 10;

        given(purchaseRepository.findById(id)).willReturn(Optional.of(p));

        underTest.findPurchaseByID(id);
        verify(purchaseRepository).findById(id);
    }
}