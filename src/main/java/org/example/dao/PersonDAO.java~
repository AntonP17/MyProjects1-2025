package org.example.dao;

import org.example.model.Person;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private static int PERSON_COUNT;
    private List<Person> people;

    {
        people = new ArrayList<>();

        people.add(new Person(++PERSON_COUNT,"Ivan"));
        people.add(new Person(++PERSON_COUNT,"Petr"));
        people.add(new Person(++PERSON_COUNT,"Sergey"));
        people.add(new Person(++PERSON_COUNT,"Vladimir"));
    }

    public List<Person> index() {
        return people;
    }

    public Person show(int id) {
        return people.stream()
                .filter(person -> person.getId() == id)
                .findAny()
                .orElse(null);
    }

    public void save(Person person) {
        person.setId(++PERSON_COUNT);
        people.add(person);
    }
}
