package nutritious.prog.demo.services;

import nutritious.prog.demo.exceptions.InvalidArgumentException;
import nutritious.prog.demo.exceptions.ObjectAlreadyExistsException;
import nutritious.prog.demo.exceptions.ObjectNotFoundException;
import nutritious.prog.demo.model.Item;
import nutritious.prog.demo.repositories.ItemRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    private AutoCloseable autoCloseable;
    private ItemService underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ItemService(itemRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void saveItem() {
        //given
        Item i = new Item("Apple", 2.50);

        //when
        underTest.saveItem("Apple", 2.50);

        //then
        ArgumentCaptor<Item> argumentCaptor = ArgumentCaptor.forClass(Item.class);

        verify(itemRepository).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).isEqualTo(i);
    }

    @Test
    void saveItemWithEmptyName() {
        assertThatThrownBy(
                () -> underTest.saveItem("", 2.50))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(itemRepository, never()).save(any());
    }

    @Test
    void saveItemWithNegativePrice() {
        assertThatThrownBy(
                () -> underTest.saveItem("Apple", -2.50))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(itemRepository, never()).save(any());
    }

    @Test
    void saveItemThrowingExcWhenItemAlreadyExists() {
        Item i = new Item("Apple", 2.50);
        Item i2 =new Item("Apple", 2.50);
        List<Item> items = new ArrayList<>();
        items.add(i);
        items.add(i2);

        underTest.saveItem("Apple", 2.50);

        given(itemRepository.findAll()).willReturn(items);

        assertThatThrownBy(
                () -> underTest.saveItem("Apple", 2.50))
                .isInstanceOf(ObjectAlreadyExistsException.class)
                .hasMessageContaining("Object with the same data already exists in db.");

        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void deleteItem() {
        // given
        long id = 10;
        given(itemRepository.existsById(id)).willReturn(true);
        // when
        underTest.deleteItem(id);
        // then
        verify(itemRepository).deleteById(id);
    }

    @Test
    void deleteItemThrowingExcWhenIdLessThanZero(){
        long id = -10;

        assertThatThrownBy(() -> underTest.deleteItem(id))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(itemRepository, never()).deleteById(any());
    }

    @Test
    void deleteItemThrowingExcWhenClientNotFound(){
        long id = 10;
        given(itemRepository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> underTest.deleteItem(id))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Object not found in db.");

        verify(itemRepository, never()).deleteById(any());
    }

    @Test
    void updateItem() {
        Item i = new Item("Apple", 2.5);
        long id = 10;
        given(itemRepository.findById(id)).willReturn(Optional.of(i));

        String newName = "Orange";
        double newPrice = 5.20;

        underTest.updateItem(id, newName, newPrice);

        assertThat(i.getName()).isEqualTo(newName);
        assertThat(i.getPrice()).isEqualTo(newPrice);

        verify(itemRepository).save(i);
    }

    @Test
    void updateItemWithNegativeId() {
        assertThatThrownBy(
                () -> underTest.updateItem(-10L,"Apple", 2.50))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(itemRepository, never()).save(any());
    }

    @Test
    void updateItemWithEmptyName() {
        assertThatThrownBy(
                () -> underTest.updateItem(10L,"", 2.50))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(itemRepository, never()).save(any());
    }

    @Test
    void updateItemWithNegativePrice() {
        assertThatThrownBy(
                () -> underTest.updateItem(10L,"Apple", -2.50))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");

        verify(itemRepository, never()).save(any());
    }

    @Test
    void findItemByID() {
        Item i = new Item("Apple", 2.50);
        long id = 10;

        given(itemRepository.findById(id)).willReturn(Optional.of(i));

        underTest.findItemByID(id);
        verify(itemRepository).findById(id);
    }

    @Test
    void findItemsByName() {
        Item i = new Item("Apple", 2.50);
        Item i2 = new Item("Apple", 3.50);
        Item i3 = new Item("Orange", 4.50);
        List<Item> items = new ArrayList<>(); // List imitating all records in db
        List<Item> expectedItems = new ArrayList<>(); // List with expected items with the same name

        items.add(i);
        items.add(i2);
        items.add(i3);

        expectedItems.add(i);
        expectedItems.add(i2);

        given(itemRepository.findAll()).willReturn(items);

        assertEquals(expectedItems, underTest.findItemsByName("Apple"));
    }

    @Test
    void findItemsByNmeWithEmptyName() {
        assertThatThrownBy(
                () -> underTest.findItemsByName(""))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("Invalid arguments.");
    }
}