package com.vobject.vaadin.addressbook.sample.ui;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vobject.vaadin.addressbook.sample.VaadinAddressBookApplication;
import com.vobject.vaadin.addressbook.sample.domain.Person;

public class PersonForm extends Form implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6270056628447919538L;
	private Button save = new Button("Save", (ClickListener) this);
	private Button cancel = new Button("Cancel", (ClickListener) this);
	private Button edit = new Button("Edit", (ClickListener) this);
	private VaadinAddressBookApplication app;
	private boolean newContactMode = false;
	private Person newPerson = null;
	private final ComboBox cities = new ComboBox("City");

	public PersonForm(final VaadinAddressBookApplication app) {
		this.app = app;

		/* Allow the user to enter new cities */
		cities.setNewItemsAllowed(true);
		/* We do not want to use null values */

		cities.setNullSelectionAllowed(false);
		/* Add an empty city used for selecting no city */
		cities.addItem("");

		/* Populate cities select using the cities in the data container */
		PersonReferenceContainer ds = app.getDataSource();
		for (PersonReference pf : ds.getItems()) {
		    String city = (String) pf.getItemProperty("city").getValue();
		    cities.addItem(city);
		}

		/*
		 * Field factory for overriding how the component for city selection is
		 * created
		 */
		setFormFieldFactory(new DefaultFieldFactory() {
			@Override
			public Field createField(Item item, Object propertyId,
					Component uiContext) {
				if (propertyId.equals("city")) {
					return cities;
				}
				Field field = super.createField(item, propertyId, uiContext);
				if (propertyId.equals("postalCode")) {
					TextField tf = (TextField) field;
					/*
					 * We do not want to display "null" to the user when the
					 * field is empty
					 */
					tf.setNullRepresentation("");

					/* Add a validator for postalCode and make it required */
					tf.addValidator(new RegexpValidator("[1-9][0-9]{4}",
							"Postal code must be a five digit number and cannot start with a zero."));
					tf.setRequired(true);
				}

				return field;
			}
		});
		
		// Enable buffering so that commit() must be called for the form
		// before input is written to the data. (Form input is not written
		// immediately through to the underlying object.)
		setWriteThrough(false);

		HorizontalLayout footer = new HorizontalLayout();
		footer.setSpacing(true);
		footer.addComponent(save);
		footer.addComponent(cancel);
		footer.addComponent(edit);
		footer.setVisible(false);

		setFooter(footer);
	}

	@Override
	public void setItemDataSource(Item newDataSource) {
		newContactMode = false;
		if (newDataSource != null) {
			List<Object> orderedProperties = Arrays
					.asList(PersonContainer.NATURAL_COL_ORDER);
			super.setItemDataSource(newDataSource, orderedProperties);

			setReadOnly(true);
			getFooter().setVisible(true);
		} else {
			super.setItemDataSource(null);
			getFooter().setVisible(false);
		}
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		save.setVisible(!readOnly);
		cancel.setVisible(!readOnly);
		edit.setVisible(readOnly);
	}

	public void buttonClick(ClickEvent event) {
		Button source = event.getButton();

		if (source == save) {
			/* If the given input is not valid there is no point in continuing */
			if (!isValid()) {
				return;
			}

			commit();
			app.getPersonService().savePerson(newPerson);
			setItemDataSource(new BeanItem(newPerson));
			app.getDataSource().refresh();
			newContactMode = false;
			setReadOnly(true);

		} else if (source == cancel) {
			if (newContactMode) {
				newContactMode = false;
				setItemDataSource(null);
			} else {
				discard();
			}
			setReadOnly(true);
		} else if (source == edit) {
			setReadOnly(false);
		}
	}

	public void addContact() {
		// Create a temporary item for the form
		newPerson = new Person();
		setItemDataSource(new BeanItem(newPerson));
		newContactMode = true;
		setReadOnly(false);
	}
	
	public void editContact(Person person) {
		newPerson = person;
		setItemDataSource(new BeanItem(person));
		newContactMode = false;
		setReadOnly(true);
	}
	
}