package kz.runtime.jpa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Formatter;

public class JpaMain {

    public static void main(String[] args) throws Exception {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/ziyat_products";
        String jdbcUsername = "postgres";
        String jdbcPassword = "postgres";
        Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
        String query = "select c.* from category c";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet =  preparedStatement.executeQuery();
        /*while(resultSet.next()){
            System.out.println(resultSet.getLong("id"));
            System.out.println(resultSet.getString("name"));
        }*/
        String query1 = """
                select c.name, p.name name1, p.price
                from product p
                join category c on p.category_id = c.id
                """;
        PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
        ResultSet resultSet1 = preparedStatement1.executeQuery();
      /*  while (resultSet1.next()){
            Formatter formatter = new Formatter();
            formatter.format("%-14s%-22s%-15d", resultSet1.getString("name"),
                    resultSet1.getString("name1"),
                    resultSet1.getInt("price"));
            System.out.println(formatter);
        }*/



        System.out.print("Введите название категории: ");
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        /*String category = bufferedReader.readLine();
        ResultSet resultSet2 = preparedStatement1.executeQuery();
        double sum = 0;
        int count = 0;
        while (resultSet2.next()){
            if(resultSet2.getString("name").equals(category)){
                sum += resultSet2.getInt("price");
                count++;
            }
        }
        if (count == 0){
            System.out.println("Вы ввели несуществующую категорию.");
        } else{
            double averagePrice = sum/count;
            System.out.println("Средняя цена товаров по категории " + category + " = " + (int)averagePrice);
        }*/
        String category = bufferedReader.readLine();
        System.out.print("Введите процент увеличения стоимости: ");
        String inc = bufferedReader.readLine();
        int percent = Integer.parseInt(inc);
        String query2 = """
                update product p
                set price = price * (100 + ?) / 100
                where p.category_id = (select c.id
                                       from category c
                                       where c.name = ?)
                """;
        PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
        preparedStatement2.setInt(1, percent);
        preparedStatement2.setString(2, category);
        preparedStatement2.executeUpdate();
    }

}
