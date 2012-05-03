package com.vobject.vaadin.addressbook.sample.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vobject.vaadin.addressbook.sample.domain.Person;

@Repository
public interface PersonRepository extends JpaSpecificationExecutor<Person>, JpaRepository<Person, Long> {
	List<Person> findByFirstNameLike(String firstName, Pageable pageable);
	List<Person> findByLastNameLike(String lastName, Pageable pageable);
	List<Person> findByEmailLike(String email, Pageable pageable);
	List<Person> findByPhoneNumberLike(String phoneNumber, Pageable pageable);
	List<Person> findByStreetAddressLike(String streetAddress, Pageable pageable);
	List<Person> findByPostalCode(Integer postalCode, Pageable pageable);
	List<Person> findByCityLike(String city, Pageable pageable);
}
