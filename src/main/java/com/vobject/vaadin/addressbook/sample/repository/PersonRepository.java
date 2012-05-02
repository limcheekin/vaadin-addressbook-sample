package com.vobject.vaadin.addressbook.sample.repository;

import com.vobject.vaadin.addressbook.sample.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaSpecificationExecutor<Person>, JpaRepository<Person, Long> {
}
