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

public class EditProduct {

    static EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
    static Map<Integer, String> map = new HashMap<>();

    public static void main(String[] args) {
        int productNumber = productList(map);
        editProduct(productNumber);
    }

    static public int productList(Map<Integer, String> productsMap) {
        EntityManager manager = factory.createEntityManager();
        TypedQuery<String> productQuery = manager.createQuery(
                "select p.name from Product p", String.class
        );
        List<String> products = productQuery.getResultList();
        int count = 0;
        System.out.println("Товары в наличии: ");
        for (String product : products) {
            System.out.println(++count + ") " + product);
            productsMap.put(count, product);
        }
        return count;
    }


    static public String confirmation(String question) {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String confirm = "";
        System.out.println(question);
        do {
            System.out.print("Наберите да/нет: ");
            try {
                confirm = bufferedReader.readLine();
            } catch (Exception e) {
            }
        } while (!confirm.equals("да") && !confirm.equals("Да") &&
                !confirm.equals("нет") && !confirm.equals("Нет"));
        return confirm;
    }

    static String setParameter(String updatedParameter) {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parameterName = "";
        do {
            System.out.print(updatedParameter);
            try {
                parameterName = bufferedReader.readLine();
            } catch (Exception e) {
            }
        } while (parameterName.equals(""));
        return parameterName;
    }

    static long setParameterNumber(String updatedParameter) {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String number = "";
        long numberChanged = 0;
        do {
            System.out.print(updatedParameter);
            try {
                number = bufferedReader.readLine();
                numberChanged = Long.parseLong(number.trim());
            } catch (Exception e) {
            }
        } while (numberChanged <= 0);
        return numberChanged;
    }

    static public int productChoice(int count) {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String number;
        int num = 0;
        do {
            try {
                System.out.printf("Выберите товар (введите число от 1 до %d): ", count);
                number = bufferedReader.readLine();
                num = Integer.parseInt(number);
            } catch (Exception e) {
            }
        } while (num <= 0 || num > count);
        return num;
    }


    static void editProduct(int count1) {
        EntityManager manager = factory.createEntityManager();
        try {
            manager.getTransaction().begin();
            InputStreamReader inputStreamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


            int num = productChoice(count1);
            String productName1 = map.get(num);

            TypedQuery<Product> productQuery = manager.createQuery(
                    "select p from Product p where p.name = ?1", Product.class
            );
            productQuery.setParameter(1, productName1);
            Product product = productQuery.getSingleResult();


            // название товара
            System.out.println("\nНазвание товара: " + product.getName());
            String confirm = confirmation("Хотите ли вы изменить название товара?");
            if (confirm.equals("да") || confirm.equals("Да")) {
                String productName = setParameter("Введите новое название товара: ");
                //сохранить новое название
                product.setName(productName);
            }

            // цена товара
            System.out.println("\nЦена товара: " + product.getPrice());
            confirm = confirmation("Хотите ли вы изменить цену товара?");
            if (confirm.equals("да") || confirm.equals("Да")) {
                long priceChanged = setParameterNumber("Введите новую цену товара: ");
                product.setPrice(priceChanged);
            }

            // характеристики товара
            TypedQuery<Characteristics> characteristicsQuery = manager.createQuery(
                    "select ch from Characteristics ch where ch.category.id = ?1", Characteristics.class
            );
            characteristicsQuery.setParameter(1, product.getCategory().getId());
            List<Characteristics> characteristics = characteristicsQuery.getResultList();


            int count = 1;
            System.out.println("\nХарактеристики товара:");
            for (Characteristics characteristic : characteristics) {

                TypedQuery<CharDescription> productCharacteristicQuery1 = manager.createQuery(
                        "select pc from CharDescription pc where pc.characteristics.id = ?1" +
                                " and pc.product.id = ?2", CharDescription.class
                );
                productCharacteristicQuery1.setParameter(1, characteristic.getId());
                productCharacteristicQuery1.setParameter(2, product.getId());

                CharDescription productCharacteristic;
                boolean characteristicExists;
                try {
                    productCharacteristic = productCharacteristicQuery1.getSingleResult();
                    System.out.printf("\n%d) %s: %s\n", count++,
                            characteristic.getName(),
                            productCharacteristic.getDescription());
                    characteristicExists = true;
                } catch (Exception e) {
                    productCharacteristic = new CharDescription();
                    productCharacteristic.setProduct(product);
                    productCharacteristic.setCharacteristics(characteristic);
                    System.out.printf("\n%d) Характеристка \"%s\" отсутсвует.\n", count++,
                            characteristic.getName());
                    characteristicExists = false;
                }
                if(characteristicExists){
                    confirm = confirmation("Хотите ли вы изменить значение данной характеристики товара?");
                } else {
                    confirm = confirmation("Хотите ли вы добавить значение данной характеристики товара?");
                }
                if (confirm.equals("да") || confirm.equals("Да")) {
                    String descriptionChanged = setParameter("Введите новое описание характеристики товара: ");
                    productCharacteristic.setDescription(descriptionChanged);
                    manager.persist(productCharacteristic);
                }
            }

            manager.persist(product);
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            System.out.printf(e.getMessage());
        }


    }
}


