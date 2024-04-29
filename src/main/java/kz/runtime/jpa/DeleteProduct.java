package kz.runtime.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import kz.runtime.jpa.entity.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kz.runtime.jpa.EditProduct.*;

public class DeleteProduct {

    static EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");

    static Map<Integer, String> map = new HashMap<>();

    public static void main(String[] args) {
        int productNumber = productList(map);
        deleteProduct(productNumber);
    }

    static void deleteProduct(int count){
        EntityManager manager = factory.createEntityManager();
        String confirm = confirmation("\nХотите ли вы удалить какой-либо из товаров?");
        if (confirm.equals("да") || confirm.equals("Да")) {
            try{
                manager.getTransaction().begin();
                int num = productChoice(count);
                String productName = map.get(num);

                TypedQuery<Product> productQuery = manager.createQuery(
                        "select p from Product p where p.name = ?1", Product.class
                );
                productQuery.setParameter(1, productName);
                Product product = productQuery.getSingleResult();

                for (CharDescription productCharacteristic : product.getCharDescriptions()) {
                    manager.remove(productCharacteristic);
                }
                manager.remove(product);
                manager.getTransaction().commit();
            }catch (Exception e){
                manager.getTransaction().rollback();
                System.out.println(e.getMessage());
            }

        }
    }

}
