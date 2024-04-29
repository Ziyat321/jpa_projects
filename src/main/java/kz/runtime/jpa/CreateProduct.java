package kz.runtime.jpa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CreateProduct {

    public static void main(String[] args) throws Exception{

        long categoryId = chooseCategory();
        //System.out.println(categoryId);
        long productId = createProduct(categoryId);
        //System.out.println(productId);
        characteristicsSet(categoryId, productId);
    }

    static long chooseCategory() throws Exception {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/ziyat_products";
        String jdbcUsername = "postgres";
        String jdbcPassword = "postgres";
        Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
        String query = """
                select name
                from category c
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        int count = 0;
        List<String> categoryList = new ArrayList<>();
        System.out.println("Категории товаров: ");
        while (resultSet.next()){
            String categoryName = resultSet.getString("name");
            categoryList.add(categoryName);
            System.out.println(++count + ") " + categoryName);
        }


        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String number;
        int num = 0;
        do{
            try {
                System.out.printf("Выберите категорию (введите число от 1 до %d): ", count);
                number = bufferedReader.readLine();
                num = Integer.parseInt(number);
            } catch (Exception e){
            }
        } while (num <= 0 || num > count);
        String category = categoryList.get(num - 1);
        String query1 = """
                select id
                from category c
                where name = ?
                """;
        PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
        preparedStatement1.setString(1, category);
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        long categoryId = 0;
        if(resultSet1.next()){
            categoryId = resultSet1.getLong("id");
        }
        return categoryId;
    }

    static long createProduct(long categoryId) throws Exception{
        String jdbcUrl = "jdbc:postgresql://localhost:5432/ziyat_products";
        String jdbcUsername = "postgres";
        String jdbcPassword = "postgres";
        Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
        System.out.print("Введите название товара: ");
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String product = bufferedReader.readLine();
        String price;
        long productPrice = 0;
        do{
            try {
                System.out.print("Введите стоимость товара: ");
                price = bufferedReader.readLine();
                productPrice = Long.parseLong(price);
            } catch (Exception e){
            }
        } while (productPrice <= 0);
        String query = """
                insert into product (category_id, name, price)
                values(?, ?, ?)
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(query,  Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setLong(1, categoryId);
        preparedStatement.setString(2, product);
        preparedStatement.setLong(3, productPrice);
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        long productId = 0;
        if(generatedKeys.next()){
            productId = generatedKeys.getLong(1);
        }
        return productId;
    }

    static void characteristicsSet(long categoryId, long productId) throws Exception{
        String jdbcUrl = "jdbc:postgresql://localhost:5432/ziyat_products";
        String jdbcUsername = "postgres";
        String jdbcPassword = "postgres";
        Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
        List<Long> characteristicsIdList = new ArrayList<>();
        List<String> characteristicsNameList = new ArrayList<>();
        String query = """
                select id, name 
                from characteristics c
                where category_id = ?
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, categoryId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            characteristicsIdList.add(resultSet.getLong("id"));
            characteristicsNameList.add(resultSet.getString("name"));
        }

        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> characteristicsDescriptionList = new ArrayList<>();
        System.out.println("Введите описание для следующих характеристик товара.");
        for (int i = 0; i < characteristicsNameList.size(); i++) {
            System.out.print(characteristicsNameList.get(i).trim() + ": ");
            String description = bufferedReader.readLine();
            characteristicsDescriptionList.add(description);
        }

        String query1 = """
                insert into characteristics_descriptions (characteristics_id, product_id, description)
                values (?, ?, ?)
                """;
        for (int i = 0; i < characteristicsIdList.size(); i++) {
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setLong(1, characteristicsIdList.get(i));
            preparedStatement1.setLong(2, productId);
            preparedStatement1.setString(3, characteristicsDescriptionList.get(i));
            preparedStatement1.executeUpdate();
        }
    }
}
