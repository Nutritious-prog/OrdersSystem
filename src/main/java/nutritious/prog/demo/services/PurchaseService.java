package nutritious.prog.demo.services;

import lombok.AllArgsConstructor;
import nutritious.prog.demo.exceptions.InvalidArgumentException;
import nutritious.prog.demo.exceptions.ObjectAlreadyExistsException;
import nutritious.prog.demo.exceptions.ObjectNotFoundException;
import nutritious.prog.demo.model.Client;
import nutritious.prog.demo.model.Item;
import nutritious.prog.demo.model.Purchase;
import nutritious.prog.demo.repositories.PurchaseRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PurchaseService {
    private PurchaseRepository repo;

    public void savePurchase(Item item, Client client, double shippingPrice) throws InvalidArgumentException, ObjectAlreadyExistsException {
        if(item == null || client == null || shippingPrice < 0) throw new InvalidArgumentException("Invalid arguments.");

        Purchase p = new Purchase(item, client, shippingPrice);

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
