package nutritious.prog.demo.services;

import nutritious.prog.demo.exceptions.InvalidArgumentException;
import nutritious.prog.demo.exceptions.ObjectAlreadyExistsException;
import nutritious.prog.demo.exceptions.ObjectNotFoundException;
import nutritious.prog.demo.model.Client;
import nutritious.prog.demo.model.Item;
import nutritious.prog.demo.model.Purchase;
import nutritious.prog.demo.repositories.PurchaseRepository;
import org.springframework.stereotype.Service;

@Service
public class PurchaseService {
    private PurchaseRepository repo;

    //TODO write Service tests

    public void savePurchase(Item item, Client client, double shippingPrice) throws InvalidArgumentException, ObjectAlreadyExistsException {
        if(item == null || client == null || shippingPrice < 0) throw new InvalidArgumentException("Invalid arguments.");

        Purchase p = new Purchase(item, client, shippingPrice);

        //Checking if the same address already exists in db.
        Iterable<Purchase> purchases = repo.findAll();
        for(Purchase pur : purchases) {
            if(pur.equals(p)) {
                throw new ObjectAlreadyExistsException("Object with the same data already exists in db.");
            }
        }

        repo.save(p);
    }

    public int deletePurchase(Long id){
        if(id <= 0) throw new InvalidArgumentException("Invalid arguments.");
        boolean exists = repo.existsById(id);
        if (exists == false) throw new ObjectNotFoundException("Object not found in db.");
        repo.deleteById(id);
        return 0;
    }

    public Purchase findPurchaseByID(Long id) {
        Purchase p = repo.findById(id).get();
        return p;
    }

}
