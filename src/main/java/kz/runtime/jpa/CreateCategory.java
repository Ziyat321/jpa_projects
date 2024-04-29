package kz.runtime.jpa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;

public class CreateCategory {

    public static void main(String[] args) throws Exception {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/ziyat_products";
        String jdbcUsername = "postgres";
        String jdbcPassword = "postgres";
        Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);

        System.out.print("Введите название категории: ");
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String category = bufferedReader.readLine();

        String categoryTemp;
        String categorySame = "";
        do {
            categoryTemp = category;
            String queryCheck = """
                select name
                from category c
                where name = ?
                """;
            PreparedStatement preparedStatement2 = connection.prepareStatement(queryCheck);
            preparedStatement2.setString(1, category);
            ResultSet resultSet = preparedStatement2.executeQuery();
            if(resultSet.next()){
                categorySame = resultSet.getString("name");
            }
            if(categorySame.equals("")){
                System.out.print("Введите название категории: ");
                category = bufferedReader.readLine();
            } else if(categorySame.equals(category)){
                System.out.println("Данная категория уже существует.");
                System.out.print("Введите название категории: ");
                category = bufferedReader.readLine();
            }
        } while (categoryTemp.equals(categorySame));


        String query = """
                insert into category (name)
                values (?)
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, category);
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

        long categoryId = 0;
        if(generatedKeys.next()){
            categoryId = generatedKeys.getLong(1);
        }

        System.out.print("Введите характеристики категории (через запятую): ");
        String characteristics = bufferedReader.readLine();
        String[] characteristicsSet = characteristics.split(", ");
        for (int i = 0; i < characteristicsSet.length; i++) {
            String s = characteristicsSet[i];
            characteristicsSet[i] = s.substring(0, 1).toUpperCase() + s.substring(1);
        }

        if(categoryId > 0){
            String query1 = """
                insert into characteristics(category_id, name)
                values (?, ?)
                """;
            for (String s : characteristicsSet) {
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                preparedStatement1.setLong(1, categoryId);
                preparedStatement1.setString(2, s);
                preparedStatement1.executeUpdate();
            }
        }

    }
}
