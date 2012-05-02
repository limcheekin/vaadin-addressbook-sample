package com.vobject.vaadin.addressbook.sample.ui;

import java.io.Serializable;

public class QueryMetaData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4718339474625539643L;
	private boolean[] ascending;
	private String[] orderBy;
	private String searchTerm;
	private String propertyName;

	public QueryMetaData(String propertyName, String searchTerm,
			String[] orderBy, boolean[] ascending) {
		this.propertyName = propertyName;
		this.searchTerm = searchTerm;
		this.ascending = ascending;
		this.orderBy = orderBy;
	}

	public QueryMetaData(String[] orderBy, boolean[] ascending) {
		this(null, null, orderBy, ascending);
	}

	public boolean[] getAscending() {
		return ascending;
	}

	public String[] getOrderBy() {
		return orderBy;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public String getPropertyName() {
		return propertyName;
	}
}