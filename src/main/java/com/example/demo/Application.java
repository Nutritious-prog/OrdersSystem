package com.example.demo;

import com.example.demo.model.Address;
import com.example.demo.model.Client;
import com.example.demo.model.Item;
import com.example.demo.model.Purchase;
import com.example.demo.repositories.AddressRepository;
import com.example.demo.repositories.ItemRepository;
import com.example.demo.repositories.PurchaseRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(Application.class, args);

        AddressRepository addressRepository =
                configurableApplicationContext.getBean(AddressRepository.class);

        UserRepository userRepository =
                configurableApplicationContext.getBean(UserRepository.class);

        ItemRepository itemRepository =
                configurableApplicationContext.getBean(ItemRepository.class);

        PurchaseRepository purchaseRepository =
                configurableApplicationContext.getBean(PurchaseRepository.class);

        Address address = new Address("Jugoslowianska 13c", "Lodz", "Lodzkie", "92-720", "Polska");
        addressRepository.save(address);

        Address address2 = new Address("Pomorska 452", "Lodz", "Lodzkie", "92-720", "Polska");
        addressRepository.save(address2);

        Client client = new Client("Jan Kowalski", address, 0.1);
        userRepository.save(client);

        Client client2 = new Client("Adam Kowalski", address, 0.2);
        userRepository.save(client2);

        Client client3 = new Client("Piotr Kowalski", address2, 0.3);
        userRepository.save(client3);

        Client client4 = new Client("Krzysztof Nowak", address2, 0.3);
        userRepository.save(client4);

        Item apple = new Item("Apple", 3.5);
        Item watermelon = new Item("Watermelon", 10.0);

        itemRepository.save(apple);
        itemRepository.save(watermelon);

        Purchase purchase = new Purchase(apple, client3, 14.99);
        Purchase purchase2 = new Purchase(watermelon, client, 14.99);

        purchaseRepository.save(purchase);
        purchaseRepository.save(purchase2);

        System.out.println(userRepository.count());
    }

}
