package com.vobject.vaadin.addressbook.sample.ui;

import com.vaadin.data.Property;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vobject.vaadin.addressbook.sample.VaadinAddressBookApplication;
import com.vobject.vaadin.addressbook.sample.domain.Person;

public class PersonList extends Table {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5269799790226554688L;

	public PersonList(final VaadinAddressBookApplication app) {
		setColumnCollapsingAllowed(true);
		setColumnReorderingAllowed(true);
		setSizeFull();
		// customize email column to have mailto: links using column generator
		addGeneratedColumn("email", new ColumnGenerator() {
			public Component generateCell(Table source, Object itemId, Object columnId) {
				String email = (String) getContainerProperty(itemId, "email").getValue();
				Link l = new Link();
				l.setResource(new ExternalResource("mailto:" + email));
				l.setCaption(email);
				return l;
			}
		});
		setContainerDataSource(app.getDataSource());
		setVisibleColumns(PersonContainer.NATURAL_COL_ORDER);
		setColumnHeaders(PersonContainer.COL_HEADERS_ENGLISH);
		/*
		 * Make table selectable, react immediatedly to user events, and pass
		 * events to the controller (our main application)
		 */
		setSelectable(true);
		setImmediate(true);
		addListener((Property.ValueChangeListener) app);
		/* We don't want to allow users to de-select a row */
		setNullSelectionAllowed(false);
	}
}