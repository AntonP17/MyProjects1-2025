package org.example.dao;

import org.example.model.Person;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
// jdbc:postgresql://localhost:5432/first_db наша БАЗА ДАННЫХ
@Component
public class PersonDAO {
    private static int PERSON_COUNT;

    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "297032";

    private static Connection connection;

    static { // наш драйвер для работы с БД
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try { // подключаемся к БД
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // получаем всех
    public List<Person> index() {
        List<Person> people = new ArrayList<>();

        try { // получаем людей из БД , передаем сначала запрос SQL
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM Person";
            ResultSet resultSet = statement.executeQuery(SQL); // результат запроса

            while (resultSet.next()) { // это и есть jdbc , а hibernate делает автоматически
                Person person = new Person();

                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));

                people.add(person);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return people;
    }

    // получаем одного
    public Person show(int id) {

        Person person = null;

        try { // preparedstatement - более безопасно, избегает SQL инъекции , тут указываем сразу SQL
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM Person WHERE id = ?");

            preparedStatement.setInt(1, id);

           ResultSet resultSet = preparedStatement.executeQuery(); // тут так же как в index

            resultSet.next();

            person = new Person(); // тут назначаем что приходит из resultSet

            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setAge(resultSet.getInt("age"));
            person.setEmail(resultSet.getString("email"));


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return person;

    }

    public void save(Person person) {
        try { // preparedstatement - более безопасно, избегает SQL инъекции
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Person VALUES(1, ?, ?, ?)");

            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getAge());
            preparedStatement.setString(3, person.getEmail());

            preparedStatement.executeUpdate(); // его писать обязательно чтобы обновлялся после запроса

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int id, Person updatedPerson) {

        try { // тут все так же логично , раз меняем что то то надо безопасно менять
            // SQL команда для обновления колонки
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE Person SET name = ?, age = ?, email = ? WHERE id = ?");

            preparedStatement.setString(1, updatedPerson.getName());
            preparedStatement.setInt(2, updatedPerson.getAge());
            preparedStatement.setString(3, updatedPerson.getEmail());
            preparedStatement.setInt(4, id);

           preparedStatement.executeUpdate(); // +

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        // тут стандартная лямбда при условии если человек нашелся по данному айди то происходить удаление
        // у каждого человека вызываем метод getID , если true то удаляется из списка

        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("DELETE FROM Person WHERE id = ?");

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate(); // +

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
