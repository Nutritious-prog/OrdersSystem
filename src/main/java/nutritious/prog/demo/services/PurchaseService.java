package nutritious.prog.demo.services;

import nutritious.prog.demo.model.Client;
import nutritious.prog.demo.model.Item;
import nutritious.prog.demo.model.Purchase;
import nutritious.prog.demo.repositories.PurchaseRepository;
import org.springframework.stereotype.Service;

@Service
public class PurchaseService {
    PurchaseRepository repo;

    //TODO add exception throwing to methods
    //TODO write Service tests

    public void savePurchase(Item item, Client client, double shippingPrice) {
        Purchase p = new Purchase(item, client, shippingPrice);
        repo.save(p);
    }

    public int deletePurchase(Long id){
        Purchase p = repo.findById(id).get();
        if(p == null) return 1;
        repo.delete(p);
        return 0;
    }

    public int updatePurchaseShippingPrice(Long id, double shippingPrice) {
        Purchase p = repo.findById(id).get();
        if(p == null) return 1;
        p.setShippingPrice(shippingPrice);
        return 0;
    }

    public Purchase findPurchaseByID(Long id) {
        Purchase p = repo.findById(id).get();
        return p;
    }

}
