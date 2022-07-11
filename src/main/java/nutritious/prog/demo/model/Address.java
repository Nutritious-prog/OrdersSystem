package nutritious.prog.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @Column(name = "street", nullable = false, columnDefinition = "TEXT")
    private String street;
    @Column(name = "city", nullable = false, columnDefinition = "TEXT")
    private String city;
    @Column(name = "voivodeship", nullable = false, columnDefinition = "TEXT")
    private String voivodeship;
    @Column(name = "postal_code", nullable = false, columnDefinition = "TEXT")
    private String postalCode;
    @Column(name = "country", nullable = false, columnDefinition = "TEXT")
    private String country;

    public Address(String street, String city, String voivodeship, String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.voivodeship = voivodeship;
        this.postalCode = postalCode;
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return getStreet().equals(address.getStreet()) && getCity().equals(address.getCity()) && getVoivodeship().equals(address.getVoivodeship()) && getPostalCode().equals(address.getPostalCode()) && getCountry().equals(address.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStreet(), getCity(), getVoivodeship(), getPostalCode(), getCountry());
    }
}
