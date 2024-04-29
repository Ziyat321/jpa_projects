package kz.runtime.jpa.entity;

import jakarta.persistence.*;
import kz.runtime.jpa.Characteristics;
import kz.runtime.jpa.Product;

@Entity
@Table(name = "characteristics_descriptions")
public class CharDescription {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "characteristics_id")
    private Characteristics characteristics;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public Characteristics getCharacteristics() {
        return characteristics;
    }

    public Product getProduct() {
        return product;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCharacteristics(Characteristics characteristics) {
        this.characteristics = characteristics;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
