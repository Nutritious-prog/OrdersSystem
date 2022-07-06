package nutritious.prog.demo.services;

import nutritious.prog.demo.exceptions.InvalidArgumentException;
import nutritious.prog.demo.exceptions.ObjectAlreadyExistsException;
import nutritious.prog.demo.exceptions.ObjectNotFoundException;
import nutritious.prog.demo.model.Address;
import nutritious.prog.demo.model.Client;
import nutritious.prog.demo.repositories.AddressRepository;
import nutritious.prog.demo.repositories.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {
    private ClientRepository repo;
    private AddressRepository addressRepo;

    //TODO write Service tests

    public void saveClient(String name, Address address, double discount) throws InvalidArgumentException, ObjectAlreadyExistsException {
        if(discount < 0 || address == null || name.length() == 0) throw new InvalidArgumentException("Invalid arguments.");
        Client c = new Client(name, address, discount);

        //Finding if user with exact same data exists
        Iterable<Client> clients = repo.findAll();
        for(Client client : clients) {
            if(c.equals(client)) throw new ObjectAlreadyExistsException("Object with the same data already exists in db.");
        }
        repo.save(c);
    }

    public int deleteClient(Long id) throws InvalidArgumentException, ObjectNotFoundException {
        if(id <= 0) throw new InvalidArgumentException("Invalid arguments.");
        Client c = findClientByID(id);
        if (c == null) throw new ObjectNotFoundException("Object not found in db.");
        repo.delete(c);
        return 0;
    }

    public int updateClientName(Long id, String name) throws InvalidArgumentException, ObjectNotFoundException {
        if(id <= 0 || name.length() == 0) throw new InvalidArgumentException("Invalid arguments.");
        Client c = repo.findById(id).get();
        if(c == null) throw new ObjectNotFoundException("Object not found in db.");
        c.setName(name);
        repo.save(c);
        return 0;
    }
    public int updateClientAddress(Long id, Address address) throws InvalidArgumentException, ObjectNotFoundException {
        if(id <= 0 || address == null) throw new InvalidArgumentException("Invalid arguments.");
        Client c = repo.findById(id).get();
        if(c == null) throw new ObjectNotFoundException("Object not found in db.");
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
    public int updateClientDiscount(Long id, double discount) throws InvalidArgumentException, ObjectNotFoundException{
        if(discount < 0 || id <= 0) throw new InvalidArgumentException("Invalid arguments.");
        Client c = repo.findById(id).get();
        if(c == null) throw new ObjectNotFoundException("Object not found in db.");
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
