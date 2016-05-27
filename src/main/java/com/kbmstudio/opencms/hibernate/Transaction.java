package com.kbmstudio.opencms.hibernate;

public interface Transaction {
	public void begin();
	public void commit();
	public void rollback();
}
