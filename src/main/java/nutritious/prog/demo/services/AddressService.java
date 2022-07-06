package nutritious.prog.demo.services;

import nutritious.prog.demo.model.Address;
import nutritious.prog.demo.repositories.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    AddressRepository repo;

    //TODO add exception throwing to methods
    //TODO write Service tests

    public void saveAddress(String street, String city,
                            String voivodeship, String postalCode,
                            String country) {
        Address a = new Address(street, city, voivodeship, postalCode, country);
        repo.save(a);
    }

    public int deleteAddress(Long id){
        Address a = repo.findById(id).get();
        if(a == null) return 1;
        repo.delete(a);
        return 0;
    }

    //TODO add all updateAddress methods with different params

    public int updateAddress() {
        return 0;
    }

    public Address findAddressByID(Long id) {
        Address a = repo.findById(id).get();
        return a;
    }
}
