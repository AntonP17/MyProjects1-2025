package org.example.dao;

import org.example.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
// jdbc:postgresql://localhost:5432/first_db наша БАЗА ДАННЫХ
@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // получаем всех
    public List<Person> index() {

        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<Person>(Person.class)); // из таблицы Person

      //  return jdbcTemplate.query("SELECT * FROM Person2", new BeanPropertyRowMapper<>(Person.class)); // из таблицы Person2

        // можно вынести весь код в отдельный класс PersonMapper и сюда его добавить как новый обьект (пример в классе PersonMapper,
        // но тут мы решили упростить и использовать уже готовый )

    }

    // получаем одного , если есть хоть 1 обьект класса Person
    public Person show(int id) {

        return jdbcTemplate.query("SELECT * FROM Person WHERE id = ?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Person.class))
                .stream()  // проходимся по списку если человек найде т то вернет его , если нет то null
                .findAny()
                .orElse(null);

    }

    public void save(Person person) {

        jdbcTemplate.update("INSERT INTO Person (name, age, email) VALUES (?, ?, ?)",
                person.getName(), person.getAge(), person.getEmail());
        // сделал автоинкремент , теперь id будет автоматически увеличиваться


    }

    public void update(int id, Person updatedPerson) {

        jdbcTemplate.update("UPDATE Person SET name = ?, age = ?, email = ? WHERE id = ?", updatedPerson.getName(),
                updatedPerson.getAge(), updatedPerson.getEmail(), id);
    }

    public void delete(int id) {
        // тут стандартная лямбда при условии если человек нашелся по данному айди то происходить удаление
        // у каждого человека вызываем метод getID , если true то удаляется из списка

        jdbcTemplate.update("DELETE FROM Person WHERE id = ?", id);

    }

    /// /////////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////// ТЕСТИРУЕМ ПРОИЗВОИТЕЛЬНОСТЬ ПАКЕТНой вставки /////////////////

     // в консоль выводится время
    /// ///////////////////////////////////////////////////////////////////////////////////



    // обычная вставка заняло время 85 858 мс
    public void testMultipleUpdate() {
        List<Person> persons = create1000People();

        long before = System.currentTimeMillis();

        for (Person person : persons) {
            jdbcTemplate.update("INSERT INTO Person (name, age, email) VALUES (?, ?, ?)",
                    person.getName(), person.getAge(), person.getEmail());
        }

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before) + " ms");
    }


    // пакетная вставка 84 мс
    public void testBatchUpdate() {
        List<Person> persons = create1000People();

        long before = System.currentTimeMillis();

        // обратит внимание на метод batchUpdate (1 парметр - SQL запрос , 2 парметр - список параметров)
        jdbcTemplate.batchUpdate("INSERT INTO Person (name, age, email) VALUES (?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                       // ps.setInt(1, persons.get(i).getId());
                        ps.setString(1, persons.get(i).getName());
                        ps.setInt(2, persons.get(i).getAge());
                        ps.setString(3,persons.get(i).getEmail());
                    }

                    @Override
                    public int getBatchSize() {
                        return persons.size();
                    }
                });

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before) + " ms");

    }


    // иммитация обновления какого то , тут мы просто добавляем 1000 человек = 100 запросов в БД
    private List<Person> create1000People() {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            persons.add(new Person(i, "name" + i, 30, "email" + i));
        }
        return persons;
    }

    // поиск нескольких по 1 конкретному параметру , можно не только возраст а любой например по имени
    // (в sql можно указать сколько угодно колонок хоть name = ?, id = ?, email = ? )
    public List<Person> searchByAge(int age) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE age = ?", new Object[]{age},
                new BeanPropertyRowMapper<>(Person.class));
    }

    // пакетное обновление
    public void batchUpdate(List<Person> persons) { // принимает список людей которые обновляются
        List<Object[]> batch = new ArrayList<>();
        for (Person person : persons) {
            batch.add(new Object[]{person.getName(), person.getAge(), person.getEmail()});
        }
        jdbcTemplate.batchUpdate("INSERT INTO Person(name, age, email) VALUES(?, ?, ?)", batch);
    }

    // TEST TEST TEST пример использования
    //данный код вообще можно запихнуть в контроллер , в метод пост
    // создать для него форму thymeleaf и отправить на него данные
    public void test()
    {
        PersonDAO personDAO = null;

        List<Person> peopleAge30 = personDAO.searchByAge(30); // получаем список людей возрастом 30(по 1 параметру)

        for (Person person : peopleAge30) { // модифицируем , меняем у этих людей имя)
            person.setName("Tom" + person.getEmail());
        }

        personDAO.batchUpdate(peopleAge30); // выполняем пакетное обновление для модифицированных людей

    }


}
