package kz.runtime.jpa;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class CreateCategory1 {

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");

        EntityManager manager = factory.createEntityManager();
        try{
            manager.getTransaction().begin();
            System.out.print("Введите название категории: ");
            InputStreamReader inputStreamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String categoryName = bufferedReader.readLine();
            Category category = new Category();
            category.setName(categoryName);
            manager.persist(category);

            System.out.print("Введите характеристики категории (через запятую): ");
            String characteristics = bufferedReader.readLine();
            String[] characteristicsSet = characteristics.split(", ");
            for (int i = 0; i < characteristicsSet.length; i++) {
                String s = characteristicsSet[i];
                characteristicsSet[i] = s.substring(0, 1).toUpperCase() + s.substring(1);

                Characteristics characteristics1 = new Characteristics();
                characteristics1.setName(characteristicsSet[i]);
                characteristics1.setCategory(category);
                manager.persist(characteristics1);
            }
            manager.getTransaction().commit();
        } catch(Exception e){
            manager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }
}
