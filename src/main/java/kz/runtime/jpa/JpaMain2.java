package kz.runtime.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class JpaMain2 {

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");

        EntityManager manager = factory.createEntityManager();
        TypedQuery<Product> categoryQuery = manager.createQuery(
                "select count(p) from Product p where p.category.name = 'Процессоры'", Product.class
        );
        System.out.println(categoryQuery.getSingleResult());
    }
}
