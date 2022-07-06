package com.example.demo.services;

import com.example.demo.model.Address;
import com.example.demo.model.Client;
import com.example.demo.repositories.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    ClientRepository repo;

    public int updateClient(Long id, String name) {
        Client c = repo.findById(id).get();
        if(c == null) return 1;
        c.setName(name);
        repo.save(c);
        return 0;
    }

    public void saveClient(String name, Address address, double discount) {
        Client c = new Client(name, address, discount);
        repo.save(c);
    }

    public Client findClientByID(Long id) {
        Client c = repo.findById(id).get();
        return c;
    }

    public int deleteClient(Long id){
        Client c = findClientByID(id);
        if (c == null) return 1;
        repo.delete(c);
        return 0;
    }

}
