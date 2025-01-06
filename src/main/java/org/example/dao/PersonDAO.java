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

        people.add(new Person(1, "Ivan", 24, "jPw0x@example.com"));
        people.add(new Person(2, "Petr", 24, "jPw0x@example.com"));
        people.add(new Person(3, "Sveta", 24, "jPw0x@example.com"));
        people.add(new Person(4, "Masha", 24, "jPw0x@example.com"));
        people.add(new Person(5, "Oleg", 24, "jPw0x@example.com"));
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

    public void update(int id, Person updatedPerson) {
        Person personToBeUpdated = show(id);

        personToBeUpdated.setName(updatedPerson.getName());
        personToBeUpdated.setAge(updatedPerson.getAge());
        personToBeUpdated.setEmail(updatedPerson.getEmail());
    }

    public void delete(int id) {
        // тут стандартная лямбда при условии если человек нашелся по данному айди то происходить удаление
        // у каждого человека вызываем метод getID , если true то удаляется из списка
        people.removeIf(person -> person.getId() == id);
    }
}
