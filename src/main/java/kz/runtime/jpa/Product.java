package kz.runtime.jpa;

import jakarta.persistence.*;
import kz.runtime.jpa.entity.CharDescription;

import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<CharDescription> charDescriptions;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Long price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<CharDescription> getCharDescriptions() {
        return charDescriptions;
    }

    public void setCharDescriptions(List<CharDescription> charDescriptions) {
        this.charDescriptions = charDescriptions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
