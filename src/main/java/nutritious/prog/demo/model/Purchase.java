package nutritious.prog.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Purchase")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_ID")
    private Client client;
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_ID")
    private Item item;
    @Column(name = "shipping_price", nullable = false)
    private double shippingPrice;

    public Purchase(Item item, Client client, double shippingPrice) {
        this.item = item;
        this.client = client;
        this.shippingPrice = shippingPrice;
    }

    public double getActualOrderPrice(){
        return item.getPrice() - this.getClient().getDiscount() * item.getPrice() + getShippingPrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Purchase)) return false;
        Purchase purchase = (Purchase) o;
        return Double.compare(purchase.getShippingPrice(), getShippingPrice()) == 0 && getClient().equals(purchase.getClient()) && getItem().equals(purchase.getItem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClient(), getItem(), getShippingPrice());
    }
}