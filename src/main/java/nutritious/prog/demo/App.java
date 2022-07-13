package nutritious.prog.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public abstract class App {

    public static void main(String[] args) {

         ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(App.class, args);

        /*AddressRepository addressRepository =
                configurableApplicationContext.getBean(AddressRepository.class);

        ClientRepository clientRepository =
                configurableApplicationContext.getBean(ClientRepository.class);

        ItemRepository itemRepository =
                configurableApplicationContext.getBean(ItemRepository.class);

        PurchaseRepository purchaseRepository =
                configurableApplicationContext.getBean(PurchaseRepository.class);*/

        /*Address address = new Address("Jugoslowianska 13c", "Lodz", "Lodzkie", "92-720", "Polska");
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
        purchaseRepository.save(purchase2);*/

        /*System.out.println(clientRepository.count());
        System.out.println(userRepository.findById(4L));
        System.out.println(clientRepository.findAll());
        clientRepository.deleteById(4L);
        System.out.println("After delete: " + clientRepository.findAll());*/

//        ClientService clientService =
//                configurableApplicationContext.getBean(ClientService.class);

    }

}
