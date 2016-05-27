package com.kbmstudio.opencms.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class PersistenceDao {
//	Ejemplo de operacion con transaccion
	public void transaction(Object o){
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try{
			EntityTransaction t = em.getTransaction();
			try{
				t.begin();
				em.persist(o);
				t.commit();
			} finally{
				if(t.isActive()) t.rollback();
			}
		}finally{
			em.close();
		}
		
	}
//	Ejemplo de operacion sin transaccion
	public Object notransaction(Object o, Long id){
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		Object obj = null;
		try{
			obj = em.find(o.getClass(), id);
		}finally{
			em.close();
		}
		return obj;
	}
}
