package com.vobject.vaadin.addressbook.sample.domain;

import com.vobject.vaadin.addressbook.sample.repository.PersonRepository;
import com.vobject.vaadin.addressbook.sample.service.PersonService;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Transactional
public class PersonIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private PersonDataOnDemand dod;

	@Autowired
    PersonService personService;

	@Autowired
    PersonRepository personRepository;

	@Test
    public void testCountAllPeople() {
        Assert.assertNotNull("Data on demand for 'Person' failed to initialize correctly", dod.getRandomPerson());
        long count = personService.countAllPeople();
        Assert.assertTrue("Counter for 'Person' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindPerson() {
        Person obj = dod.getRandomPerson();
        Assert.assertNotNull("Data on demand for 'Person' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Person' failed to provide an identifier", id);
        obj = personService.findPerson(id);
        Assert.assertNotNull("Find method for 'Person' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Person' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllPeople() {
        Assert.assertNotNull("Data on demand for 'Person' failed to initialize correctly", dod.getRandomPerson());
        long count = personService.countAllPeople();
        Assert.assertTrue("Too expensive to perform a find all test for 'Person', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Person> result = personService.findAllPeople();
        Assert.assertNotNull("Find all method for 'Person' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Person' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindPersonEntries() {
        Assert.assertNotNull("Data on demand for 'Person' failed to initialize correctly", dod.getRandomPerson());
        long count = personService.countAllPeople();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Person> result = personService.findPersonEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Person' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Person' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Person obj = dod.getRandomPerson();
        Assert.assertNotNull("Data on demand for 'Person' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Person' failed to provide an identifier", id);
        obj = personService.findPerson(id);
        Assert.assertNotNull("Find method for 'Person' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyPerson(obj);
        Integer currentVersion = obj.getVersion();
        personRepository.flush();
        Assert.assertTrue("Version for 'Person' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testUpdatePersonUpdate() {
        Person obj = dod.getRandomPerson();
        Assert.assertNotNull("Data on demand for 'Person' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Person' failed to provide an identifier", id);
        obj = personService.findPerson(id);
        boolean modified =  dod.modifyPerson(obj);
        Integer currentVersion = obj.getVersion();
        Person merged = personService.updatePerson(obj);
        personRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Person' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSavePerson() {
        Assert.assertNotNull("Data on demand for 'Person' failed to initialize correctly", dod.getRandomPerson());
        Person obj = dod.getNewTransientPerson(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Person' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Person' identifier to be null", obj.getId());
        personService.savePerson(obj);
        personRepository.flush();
        Assert.assertNotNull("Expected 'Person' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDeletePerson() {
        Person obj = dod.getRandomPerson();
        Assert.assertNotNull("Data on demand for 'Person' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Person' failed to provide an identifier", id);
        obj = personService.findPerson(id);
        personService.deletePerson(obj);
        personRepository.flush();
        Assert.assertNull("Failed to remove 'Person' with identifier '" + id + "'", personService.findPerson(id));
    }
}
