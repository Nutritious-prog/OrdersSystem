package nutritious.prog.demo.services;

import nutritious.prog.demo.model.Address;
import nutritious.prog.demo.model.Client;
import nutritious.prog.demo.repositories.AddressRepository;
import nutritious.prog.demo.repositories.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {
    ClientRepository repo;
    AddressRepository addressRepo;

    //TODO add exception throwing to methods
    //TODO write Service tests

    public void saveClient(String name, Address address, double discount) {
        Client c = new Client(name, address, discount);
        repo.save(c);
    }

    public int deleteClient(Long id){
        Client c = findClientByID(id);
        if (c == null) return 1;
        repo.delete(c);
        return 0;
    }

    public int updateClientName(Long id, String name) {
        Client c = repo.findById(id).get();
        if(c == null) return 1;
        c.setName(name);
        repo.save(c);
        return 0;
    }
    public int updateClientAddress(Long id, Address address){
        Client c = repo.findById(id).get();
        if(c == null) return 1;
        c.setAddress(address);
        repo.save(c);

        // Checking if address already exists in db, if not add it to db.
        Iterable<Address> addresses = addressRepo.findAll();
        for(Address a : addresses) {
            if(a.equals(address)) return 0;
        }
        addressRepo.save(address);
        return 0;
    }
    public int updateClientDiscount(Long id, double discount){
        if(discount < 0) return 2;
        Client c = repo.findById(id).get();
        if(c == null) return 1;
        c.setDiscount(discount);
        repo.save(c);
        return 0;
    }

    public Client findClientByID(Long id) {
        Client c = repo.findById(id).get();
        return c;
    }

    public List<Client> findClientsByName(String name){
        Iterable<Client> clients = repo.findAll();
        List<Client> foundClients = new ArrayList<>();
        for(Client c : clients) {
            if(c.getName().equals(name)) foundClients.add(c);
        }
        return foundClients;
    }
}
