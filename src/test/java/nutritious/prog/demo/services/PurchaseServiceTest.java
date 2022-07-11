package nutritious.prog.demo.services;

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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    void deletePurchase() {
    }

    @Test
    void findPurchaseByID() {
    }
}