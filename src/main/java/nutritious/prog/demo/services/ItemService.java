package nutritious.prog.demo.services;

import nutritious.prog.demo.model.Item;
import nutritious.prog.demo.repositories.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {
    private ItemRepository repo;

    //TODO add exception throwing to methods
    //TODO write Service tests

    public void saveItem(String name, double price) {
        Item i = new Item(name, price);
        repo.save(i);
    }

    public int deleteItem(Long id){
        Item i = repo.findById(id).get();
        if(i == null) return 1;
        repo.delete(i);
        return 0;
    }

    public int updateItemName(Long id, String name) {
        Item i = repo.findById(id).get();
        if(i == null) return 1;
        i.setName(name);
        return 0;
    }

    public int updateItemPrice(Long id, double price) {
        Item i = repo.findById(id).get();
        if(i == null) return 1;
        i.setPrice(price);
        return 0;
    }

    public Item findItemByID(Long id) {
        Item i = repo.findById(id).get();
        return i;
    }

    public List<Item> findItemsByName(String name) {
        Iterable<Item> items = repo.findAll();
        List<Item> foundItems = new ArrayList<>();
        for(Item i : items) {
            if(i.getName().equals(name)) foundItems.add(i);
        }
        return foundItems;
    }
}
