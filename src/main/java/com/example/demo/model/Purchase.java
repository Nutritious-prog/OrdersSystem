package com.example.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "Purchase")
@NoArgsConstructor
@Getter
@ToString
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
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
        return item.getPrice() + getShippingPrice();
    }
}