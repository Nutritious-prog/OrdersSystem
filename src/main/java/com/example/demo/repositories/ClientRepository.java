package com.example.demo.repositories;

import com.example.demo.model.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
    //List<Client> findByOrderByIDAsc();
}
