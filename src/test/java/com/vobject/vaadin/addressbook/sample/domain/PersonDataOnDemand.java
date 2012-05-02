package com.vobject.vaadin.addressbook.sample.domain;

import com.vobject.vaadin.addressbook.sample.repository.PersonRepository;
import com.vobject.vaadin.addressbook.sample.service.PersonService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class PersonDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Person> data;

	@Autowired
    PersonService personService;

	@Autowired
    PersonRepository personRepository;

	public Person getNewTransientPerson(int index) {
        Person obj = new Person();
        setCity(obj, index);
        setEmail(obj, index);
        setFirstName(obj, index);
        setLastName(obj, index);
        setPhoneNumber(obj, index);
        setPostalCode(obj, index);
        setStreetAddress(obj, index);
        return obj;
    }

	public void setCity(Person obj, int index) {
        String city = "city_" + index;
        if (city.length() > 20) {
            city = city.substring(0, 20);
        }
        obj.setCity(city);
    }

	public void setEmail(Person obj, int index) {
        String email = "foo" + index + "@bar.com";
        obj.setEmail(email);
    }

	public void setFirstName(Person obj, int index) {
        String firstName = "firstName_" + index;
        obj.setFirstName(firstName);
    }

	public void setLastName(Person obj, int index) {
        String lastName = "lastName_" + index;
        obj.setLastName(lastName);
    }

	public void setPhoneNumber(Person obj, int index) {
        String phoneNumber = "phoneNumber_" + index;
        obj.setPhoneNumber(phoneNumber);
    }

	public void setPostalCode(Person obj, int index) {
        Integer postalCode = new Integer(index);
        if (postalCode > 99999) {
            postalCode = 99999;
        }
        obj.setPostalCode(postalCode);
    }

	public void setStreetAddress(Person obj, int index) {
        String streetAddress = "streetAddress_" + index;
        if (streetAddress.length() > 80) {
            streetAddress = streetAddress.substring(0, 80);
        }
        obj.setStreetAddress(streetAddress);
    }

	public Person getSpecificPerson(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Person obj = data.get(index);
        Long id = obj.getId();
        return personService.findPerson(id);
    }

	public Person getRandomPerson() {
        init();
        Person obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return personService.findPerson(id);
    }

	public boolean modifyPerson(Person obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = personService.findPersonEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Person' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Person>();
        for (int i = 0; i < 10; i++) {
            Person obj = getNewTransientPerson(i);
            try {
                personService.savePerson(obj);
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            personRepository.flush();
            data.add(obj);
        }
    }
}
