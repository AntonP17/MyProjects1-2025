package org.example.controllers;

import org.example.dao.PersonDAO;
import org.example.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
выаывавыавы
@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) {
        // получим вех людей из DAO и передаем в представление
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        // получаем 1 чела по id из dao и передаем в представление
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(Model model) {
        // тут еще можно исопьзовать вторую форму записи @ModelAttribute "person" Person person
        //  будет тоже самое что и внизу , только она автоматически добавится спрингом

        model.addAttribute("person", new Person());

        return "people/new";
    }

    @PostMapping() // ручной вариант данного метод в файле text1
    public String create(@ModelAttribute("person") Person person) {

        personDAO.save(person);
        return "redirect:/people";

    }
}
