package com.vobject.vaadin.addressbook.sample.service;

import com.vobject.vaadin.addressbook.sample.domain.Person;
import com.vobject.vaadin.addressbook.sample.ui.PersonReference;
import com.vobject.vaadin.addressbook.sample.ui.QueryMetaData;

import java.util.List;

public interface PersonService {

	public abstract long countAllPeople();


	public abstract void deletePerson(Person person);


	public abstract Person findPerson(Long id);


	public abstract List<Person> findAllPeople();


	public abstract List<Person> findPersonEntries(int firstResult, int maxResults);


	public abstract void savePerson(Person person);


	public abstract Person updatePerson(Person person);


	public abstract List<Person> findPersonEntries(QueryMetaData queryMetaData);


}
