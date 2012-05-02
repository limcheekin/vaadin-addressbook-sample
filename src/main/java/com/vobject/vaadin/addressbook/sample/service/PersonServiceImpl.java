package com.vobject.vaadin.addressbook.sample.service;

import com.vobject.vaadin.addressbook.sample.domain.Person;
import com.vobject.vaadin.addressbook.sample.repository.PersonRepository;
import com.vobject.vaadin.addressbook.sample.ui.PersonReference;
import com.vobject.vaadin.addressbook.sample.ui.QueryMetaData;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PersonServiceImpl implements PersonService {

	@Autowired
    PersonRepository personRepository;

	public long countAllPeople() {
        return personRepository.count();
    }

	public void deletePerson(Person person) {
        personRepository.delete(person);
    }

	public Person findPerson(Long id) {
        return personRepository.findOne(id);
    }

	public List<Person> findAllPeople() {
        return personRepository.findAll();
    }

	public List<Person> findPersonEntries(int firstResult, int maxResults) {
        return personRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void savePerson(Person person) {
        personRepository.save(person);
    }

	public Person updatePerson(Person person) {
        return personRepository.save(person);
    }

	public List<Person> findPersonEntries(QueryMetaData queryMetaData) {
		if (queryMetaData.getPropertyName() == null) {
			return findAllPeople();
		}
		return null;
	}

}
