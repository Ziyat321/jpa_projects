package kz.runtime.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
//import jdk.jfr.Category;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JpaMain1 {

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");

        EntityManager manager = factory.createEntityManager();
        /*Category category = manager.find(Category.class, 2);
        System.out.printf("%d) %s\n", category.getId(), category.getName());

        Product product = manager.find(Product.class, 2);
        System.out.printf(
                "%s: %s (%d)\n",
                product.getCategory().getName(),
                product.getName(),
                product.getPrice()
        );*/

        long categoryId = 1;
        long productId = 1;
        Product product1;
        Category category1 = manager.find(Category.class, categoryId);
        System.out.println(category1.getName() + ": ");
        do {
            product1 = manager.find(Product.class, productId++);
            if(product1.getCategory().getId() == 1){
                System.out.println(" - " + product1.getName());
            }
        } while (manager.find(Product.class, productId) != null);


        /*try {
            manager.getTransaction().begin();
            *//*Category category = new Category();
            category.setName("New category");
            manager.persist(category);*//**//*
            Category category = manager.find(Category.class, 9);
            category.setName("Мышки");*//*
            Category category = manager.find(Category.class, 9);
            manager.remove(category);
            manager.getTransaction().commit();
        } catch (Exception e){
            manager.getTransaction().rollback();
            throw new RuntimeException(e);
        }*/

        try{
            manager.getTransaction().begin();
            System.out.print("Введите название категории: ");
            InputStreamReader inputStreamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String categoryName = bufferedReader.readLine();
            Category category = new Category();
            category.setName(categoryName);

            System.out.print("Введите характеристики категории (через запятую): ");
            String characteristics = bufferedReader.readLine();
            String[] characteristicsSet = characteristics.split(", ");
            Characteristics characteristics1 = new Characteristics();
            List<Characteristics> characteristicsList = new ArrayList<>();
            for (int i = 0; i < characteristicsSet.length; i++) {
                String s = characteristicsSet[i];
                characteristicsSet[i] = s.substring(0, 1).toUpperCase() + s.substring(1);
                characteristics1.setName(characteristicsSet[i]);
                characteristicsList.add(characteristics1);
            }
            category.setCharacteristics(characteristicsList);
            manager.persist(category);
            manager.getTransaction().commit();
        } catch(Exception e){
            manager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }
}
