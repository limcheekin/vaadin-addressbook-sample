package com.vobject.vaadin.addressbook.sample.ui;

import com.vaadin.ui.VerticalSplitPanel;

public class ListView extends VerticalSplitPanel {
     /**
	 * 
	 */
	private static final long serialVersionUID = -663702919966140166L;

	public ListView(PersonList personList, PersonForm personForm) {
		setFirstComponent(personList);
		setSecondComponent(personForm);
		setSplitPosition(40);
	}
}