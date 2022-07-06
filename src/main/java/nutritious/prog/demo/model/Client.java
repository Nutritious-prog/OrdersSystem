package nutritious.prog.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "Client")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long client_ID;
    @Column(name = "name", columnDefinition = "TEXT")
    private String name;
    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "ID")
    private Address address;
    @Column(name = "discount")
    private double discount;


    public Client(String name, Address address, double discount) {
        this.name = name;
        this.address = address;
        this.discount = discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
