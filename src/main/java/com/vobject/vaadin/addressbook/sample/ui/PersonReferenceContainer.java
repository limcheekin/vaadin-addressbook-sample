package com.vobject.vaadin.addressbook.sample.ui;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vobject.vaadin.addressbook.sample.domain.Person;
import com.vobject.vaadin.addressbook.sample.service.PersonService;

@SuppressWarnings("serial")
public class PersonReferenceContainer implements Container,
		Container.ItemSetChangeNotifier {

	/**
	 * Natural property order for Person bean. Used in tables and forms.
	 */
	public static final Object[] NATURAL_COL_ORDER = new String[] {
			"firstName", "lastName", "email", "phoneNumber", "streetAddress",
			"postalCode", "city" };
	protected static final Collection<Object> NATURAL_COL_ORDER_COLL = Collections
			.unmodifiableList(Arrays.asList(NATURAL_COL_ORDER));
	/**
	 * "Human readable" captions for properties in same order as in
	 * NATURAL_COL_ORDER.
	 */
	public static final String[] COL_HEADERS_ENGLISH = new String[] {
			"First name", "Last name", "Email", "Phone number",
			"Street Address", "Postal Code", "City" };
	private PersonService personService;
	protected List<PersonReference> personReferences;
	protected Map<Object, PersonReference> idIndex;
	/**
	 * Default query meta data.
	 */
	public static QueryMetaData defaultQueryMetaData = new QueryMetaData(
			new String[] { "firstName" }, new boolean[] { true });
	protected QueryMetaData queryMetaData = defaultQueryMetaData;
	protected ArrayList<ItemSetChangeListener> listeners = new ArrayList<ItemSetChangeListener>();

	public PersonReferenceContainer(PersonService personService) {
		this.personService = personService;
	}

	public void refresh() {
		refresh(queryMetaData);
	}

	public void refresh(QueryMetaData queryMetaData) {
		this.queryMetaData = queryMetaData;
		personReferences = getReferenceList(personService.findPersonEntries(queryMetaData));
		idIndex = new HashMap<Object, PersonReference>(personReferences.size());
		for (PersonReference pf : personReferences) {
			idIndex.put(pf.getPersonId(), pf);
		}
		notifyListeners();
	}

	private List<PersonReference> getReferenceList(List<Person> people) {
		List<PersonReference> referenceList = new ArrayList<PersonReference>(
				people.size());
		HashMap<String, Object> valueMap;
		Field[] fields = new Field[NATURAL_COL_ORDER.length];
		for (int i = 0; i < NATURAL_COL_ORDER.length; i++) {
			fields[i] = ReflectionUtils.findField(Person.class, (String)NATURAL_COL_ORDER[i]);
			fields[i].setAccessible(true);
		}
		for (Person person : people) {
			valueMap = new HashMap<String, Object>();
			for (int i = 0; i < NATURAL_COL_ORDER.length; i++) {
				try {
					valueMap.put((String)NATURAL_COL_ORDER[i], fields[i].get(person));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			referenceList.add(new PersonReference((Long) person.getId(), valueMap));
		}
		return referenceList;
	}
	
	public QueryMetaData getQueryMetaData() {
		return queryMetaData;
	}

	public void close() {
		if (personReferences != null) {
			personReferences.clear();
			personReferences = null;
		}
	}

	public boolean isOpen() {
		return personReferences != null;
	}

	public int size() {
		return personReferences == null ? 0 : personReferences.size();
	}

	public Item getItem(Object itemId) {
		return idIndex.get(itemId);
	}

	public Collection<?> getContainerPropertyIds() {
		return NATURAL_COL_ORDER_COLL;
	}

	public Collection<?> getItemIds() {
		return Collections.unmodifiableSet(idIndex.keySet());
	}

	public List<PersonReference> getItems() {
		return Collections.unmodifiableList(personReferences);
	}

	public Property getContainerProperty(Object itemId, Object propertyId) {
		Item item = idIndex.get(itemId);
		if (item != null) {
			return item.getItemProperty(propertyId);
		}
		return null;
	}

	public Class<?> getType(Object propertyId) {
		try {
			// TODO Optimize, please!
			PropertyDescriptor pd = new PropertyDescriptor((String) propertyId, Person.class);
			return pd.getPropertyType();
		} catch (Exception e) {
			return null;
		}
	}

	public boolean containsId(Object itemId) {
		return idIndex.containsKey(itemId);
	}

	public Item addItem(Object itemId) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This container is read-only.");
	}

	public Object addItem() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This container is read-only.");
	}

	public boolean removeItem(Object itemId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This container is read-only.");
	}

	public boolean addContainerProperty(Object propertyId, Class<?> type,
			Object defaultValue) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This container is read-only.");
	}

	public boolean removeContainerProperty(Object propertyId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This container is read-only.");
	}

	public boolean removeAllItems() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This container is read-only.");
	}

	public synchronized void addListener(ItemSetChangeListener listener) {
		listeners.add(listener);
	}

	public synchronized void removeListener(ItemSetChangeListener listener) {
		listeners.remove(listener);
	}

	@SuppressWarnings("unchecked")
	protected void notifyListeners() {
		ArrayList<ItemSetChangeListener> cl = (ArrayList<ItemSetChangeListener>) listeners
				.clone();
		ItemSetChangeEvent event = new ItemSetChangeEvent() {

			public Container getContainer() {
				return PersonReferenceContainer.this;
			}
		};

		for (ItemSetChangeListener listener : cl) {
			listener.containerItemSetChange(event);
		}
	}
}
