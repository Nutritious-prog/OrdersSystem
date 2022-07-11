package nutritious.prog.demo.services;

import lombok.AllArgsConstructor;
import nutritious.prog.demo.exceptions.InvalidArgumentException;
import nutritious.prog.demo.exceptions.ObjectAlreadyExistsException;
import nutritious.prog.demo.exceptions.ObjectNotFoundException;
import nutritious.prog.demo.model.Item;
import nutritious.prog.demo.repositories.ItemRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemService {
    private ItemRepository repo;

    public void saveItem(@NotNull String name, double price) throws InvalidArgumentException, ObjectAlreadyExistsException {
        if(name.isEmpty() || price <= 0) throw new InvalidArgumentException("Invalid arguments.");
        Item i = new Item(name, price);

        //Checking if the same address already exists in db.
        Iterable<Item> items = repo.findAll();
        for(Item item : items) {
            if(item.equals(i)) {
                throw new ObjectAlreadyExistsException("Object with the same data already exists in db.");
            }
        }

        repo.save(i);
    }

    public int deleteItem(Long id){
        if(id <= 0) throw new InvalidArgumentException("Invalid arguments.");
        boolean exists = repo.existsById(id);
        if (exists == false) throw new ObjectNotFoundException("Object not found in db.");
        repo.deleteById(id);
        return 0;
    }

    public int updateItem(Long id, String newName, double newPrice) throws InvalidArgumentException, ObjectNotFoundException{
        if(id <= 0 || newName.isEmpty() || newPrice <= 0)
            throw new InvalidArgumentException("Invalid arguments.");

        Item i = repo.findById(id).get();

        if(i == null) throw new ObjectNotFoundException("Object not found in db.");

        i.setName(newName);
        i.setPrice(newPrice);

        repo.save(i);
        return 0;
    }

    public Item findItemByID(Long id) {
        Item i = repo.findById(id).get();
        return i;
    }

    public List<Item> findItemsByName(@NotNull String name) throws InvalidArgumentException{
        if(name.isEmpty() )
            throw new InvalidArgumentException("Invalid arguments.");

        Iterable<Item> items = repo.findAll();
        List<Item> foundItems = new ArrayList<>();
        for(Item i : items) {
            if(i.getName().equals(name)) foundItems.add(i);
        }
        return foundItems;
    }
}
