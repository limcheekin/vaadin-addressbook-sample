package com.vobject.vaadin.addressbook.sample.service;

import com.vobject.vaadin.addressbook.sample.domain.Person;
import com.vobject.vaadin.addressbook.sample.repository.PersonRepository;
import com.vobject.vaadin.addressbook.sample.ui.PersonReference;
import com.vobject.vaadin.addressbook.sample.ui.QueryMetaData;

import java.lang.reflect.Method;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
		List<Person> people = null;
		
		if (queryMetaData.getPropertyName() == null) {
			people = findAllPeople();
		} else {
			//people = personRepository.findByLastNameLike('%' + queryMetaData.getSearchTerm() + '%', new PageRequest(0, 10));
			StringBuffer methodName = new StringBuffer(20);
			methodName.append("findBy");
			methodName.append(StringUtils.capitalize(queryMetaData.getPropertyName()));
			Class[] paramTypes = new Class[2];
			Object searchTerm = null;
			if (!queryMetaData.getPropertyName().equals("postalCode")) {
			  methodName.append("Like");
			  paramTypes[0] = String.class;
			  searchTerm = '%' + queryMetaData.getSearchTerm() + '%';
			} else {
				paramTypes[0] = Integer.class;
				searchTerm = Integer.valueOf(queryMetaData.getSearchTerm());
			}
			paramTypes[1] = Pageable.class;
			// System.out.println("personRepository.getClass() = " + personRepository.getClass() + ", methodName = #" + methodName.toString() + "#");
			Method method = ReflectionUtils.findMethod(personRepository.getClass(), methodName.toString(), paramTypes);
			people = (List<Person>)ReflectionUtils.invokeMethod(method, personRepository, new Object[] { searchTerm , new PageRequest(0, 100)});  
		}
		return people;
	}

}
