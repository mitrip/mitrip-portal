package com.kbmstudio.opencms.hibernate;


import org.hibernate.Session;


public class TransactionImpl implements Transaction {
	private Session session;
	

	public TransactionImpl(Session session) {
		this.session = session;
	}


	public void begin() {
		session.beginTransaction();

	}

	public void commit() {
		session.getTransaction().commit();

	}


	public void rollback() {
		session.getTransaction().rollback();
		
	}
	
	

}
