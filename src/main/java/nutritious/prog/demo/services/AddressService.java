package nutritious.prog.demo.services;

import lombok.AllArgsConstructor;
import nutritious.prog.demo.exceptions.InvalidArgumentException;
import nutritious.prog.demo.exceptions.ObjectAlreadyExistsException;
import nutritious.prog.demo.exceptions.ObjectNotFoundException;
import nutritious.prog.demo.model.Address;
import nutritious.prog.demo.repositories.AddressRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class AddressService {
    private AddressRepository repo;

    public boolean saveAddress(@NotNull String street, @NotNull String city, @NotNull String voivodeship, @NotNull String postalCode, @NotNull String country)
            throws InvalidArgumentException, ObjectAlreadyExistsException {
        if(street.isEmpty() || city.isEmpty() || voivodeship.isEmpty() || postalCode.isEmpty() || !isPostalCodeValid(postalCode) || country.isEmpty()) {
            throw new InvalidArgumentException("Invalid arguments.");
        }

        Address a = new Address(street, city, voivodeship, postalCode, country);

        //Checking if the same address already exists in db.
        Iterable<Address> addresses = repo.findAll();
        for(Address address : addresses) {
            if(address.equals(a)) {
                throw new ObjectAlreadyExistsException("Object with the same data already exists in db.");
            }
        }
        repo.save(a);
        return true;
    }

    public int deleteAddress(Long id) throws InvalidArgumentException, ObjectNotFoundException {
        if(id <= 0) throw new InvalidArgumentException("Invalid arguments.");
        boolean exists = repo.existsById(id);
        if (exists == false) throw new ObjectNotFoundException("Object not found in db.");
        repo.deleteById(id);
        return 0;
    }

    public int updateAddress(Long id, @NotNull String street, @NotNull String city,
                             @NotNull String voivodeship, @NotNull String postalCode,
                             @NotNull String country) throws InvalidArgumentException, ObjectNotFoundException{
        if(street.isEmpty() || city.isEmpty() || voivodeship.isEmpty() || !isPostalCodeValid(postalCode) || country.isEmpty())
            throw new InvalidArgumentException("Invalid arguments.");

        Address a = repo.findById(id).get();

        if(a == null) throw new ObjectNotFoundException("Object not found in db.");

        a.setStreet(street);
        a.setCity(city);
        a.setVoivodeship(voivodeship);
        a.setPostalCode(postalCode);
        a.setCountry(country);

        repo.save(a);
        return 0;
    }

    public Address findAddressByID(Long id) {
        Address a = repo.findById(id).get();
        return a;
    }

    protected boolean isPostalCodeValid(String postalCode) {
        String regex = "^[0-9]{5}$"; // "Available patterns "12345", "09876" etc."

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(postalCode);
        return matcher.matches();
    }
}
