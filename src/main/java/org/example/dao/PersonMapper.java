package org.example.dao;

import org.example.model.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
//
//public class PersonMapper implements RowMapper<Person> {
//
//    @Override
//    public Person mapRow(ResultSet resultSet, int i) throws SQLException {
//
//        Person person = new Person(); // тут назначаем что приходит из resultSet
//
//        person.setId(resultSet.getInt("id"));
//        person.setName(resultSet.getString("name"));
//        person.setAge(resultSet.getInt("age"));
//        person.setEmail(resultSet.getString("email"));
//
//        return person;
//    }
//}
