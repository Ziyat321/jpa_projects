package kz.runtime.jpa;

import jakarta.persistence.*;
import kz.runtime.jpa.entity.CharDescription;

import java.util.List;

@Entity
@Table(name = "characteristics")
public class Characteristics {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "characteristics")
    private List<CharDescription> charDescriptions;

    @Column(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public List<CharDescription> getCharDescriptions() {
        return charDescriptions;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCharDescriptions(List<CharDescription> charDescriptions) {
        this.charDescriptions = charDescriptions;
    }

    public void setName(String name) {
        this.name = name;
    }
}
