package com.kbmstudio.opencms.hibernate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;
import org.opencms.main.CmsLog;

/**
 * Clase p�blica que implementa la interfaz HibernateDao.
 * @author sraposo
 * @version 1.0 (Rev 1.0)
 */
public class  HibernateDaoImpl implements HibernateDao<Object, Long> {
	private static final Log LOG = CmsLog.getLog(HibernateDaoImpl.class);
	SessionFactory sessionFactory;
	public Transaction t;
	public Session session;
	
	public HibernateDaoImpl() {
		sessionFactory = HibernateUtil.getInstance().getSessionFactory();
		
	}
	
	public HibernateDaoImpl(SessionFactory s) {
		sessionFactory = s;
		
	}
	
	/**
	 * Devuelve el objeto de la clase con el ide correspondiente.
	 */
	public Object find(Class entity, Long id) {
		
		boolean open= isOpen();
		
		Object o = null;
		
		if(!open){
			startSessionQuery();
		}
		
		o = session.get(entity, id.longValue());
		
		if(!open){
			endSessionQuery();
		}
		
		return o;
	}




	/**
	 * Devuelve una lista de objetos de una clase determinada
	 * (non-Javadoc)
	 * @see com.saga.opencms.hibernate.HibernateDao#findAll(java.lang.Object)
	 */
	public List<Object> findAll(Class entity) {
		
		Order o=addOrder(entity.getName());
		
		return findAll(entity, o);
	}
	
	
	/**
	 * Devuelve una lista de objetos de una clase determinada
	 * (non-Javadoc)
	 * @see com.saga.opencms.hibernate.HibernateDao#findAll(java.lang.Object)
	 */
	public List<Object> findAll(Class entity,Order order) {
		
		boolean open= isOpen();
		
		if(!open){
			startSessionQuery();
		}
		
		List l = new ArrayList();
		Criteria createCriteria = session.createCriteria(entity);
		
		if(order!=null){
			createCriteria.addOrder(order);
		}
		
		l = createCriteria.list();

		if(!open){
			endSessionQuery();
		}
		
		return l;
	}	
	

	
	/**
	 * 	Metodo que guarda un objeto
	 */
	
	public Object persist(Object entity) {
		Object o = null;
		
		
		boolean open= isOpen();
		
		if(!open){
			startSession();
		}
				
		o = session.save(entity);
			
		if(!open){
			endSession();
		}	
		
		return o;
	}

	public List persistAll(List lis){
		
		List<Object>  l = new ArrayList<Object>();
		
		boolean open= isOpen();
		
		if(!open){
			startSession();
		}
		
		for(Object o:lis){
			l.add(persist(o));
		}
		
		if(!open){
			endSession();
		}
		
		return l;		
		

		
	}
	
	/**
	 * Metodo que elimina un objeto
	 * (non-Javadoc)
	 * @see com.saga.opencms.hibernate.HibernateDao#remove(java.lang.Object)
	 */
	
	public void remove(Object entity) {
		
		boolean open= isOpen();
		
		if(!open){
			startSession();
		}
		
		session.delete(entity);

		if(!open){
			endSession();
		}
		
	}

	/**
	 * Metodo que actualiza un objeto
	 */
	
	public void update(Object entity) {
		
		boolean open= isOpen();
		
		if(!open){
			startSession();
		}
		
		session.update(entity);
		
		if(!open){
			endSession();
		}
	}

	public void updateOrPersist(Object entity){
		 if(isNull(entity)){
	    	  persist(entity);
	      }else{
	    	  update(entity);
	      }
	}
	

	
	/**
	 * Metodo de no EXISTE.
	 * @param tipo, es el tipo de la clase q se va a listar
	 * @param en,  es el otro tipo de la clase que no debe contener como atributo a tipo
	 * @param relacion, nombre del atributo en la clase 'en' de la clase 'tipo'
	 * @param criterios sobre la clase en
	 * @return
	 */
	  public List noExiste(Class tipo,Class en, String relacion,List<Criterion> criterios ){
		
		boolean open= isOpen();
			
		if(!open){
			startSessionQuery();
		}		  
		  
		Criteria criteria = session.createCriteria(tipo,"f");

		DetachedCriteria sub =  DetachedCriteria.forClass(en,"i");
		for(Criterion r: criterios){
    		sub.add(r);
    	}
		sub.setProjection(Projections.property(relacion+".id"));

		criteria.add(Subqueries.propertyNotIn("f.id", sub));
		List l=criteria.list();
		
		if(!open){
			endSessionQuery();
		}
		
		return l;
	}	
	
	
	/**
	 * Metodo de  EXISTE.
	 * @param tipo, es el tipo de la clase q se va a listar
	 * @param en,  es el otro tipo de la clase que  debe contener como atributo a tipo
	 * @param relacion, nombre del atributo en la clase 'en' de la clase 'tipo'
	 * @param criterios sobre la clase en
	 * @return
	 */
	public List existe(Class tipo,Class en, String relacion,List<Criterion> criterios ){

		boolean open= isOpen();
		
		if(!open){
			startSessionQuery();
		}
		
		Criteria criteria = session.createCriteria(tipo,"f");

		DetachedCriteria sub =  DetachedCriteria.forClass(en,"i");
		for(Criterion r: criterios){
    		sub.add(r);
    	}
		sub.setProjection(Projections.property(relacion+".id"));

		criteria.add(Subqueries.propertyIn("f.id", sub));

		List l=criteria.list();
		
		if(!open){
			endSessionQuery();
		}
		
		return l;
	}	
	
	/**
	 *  Cargar en sesion el objeto dado
	 * */
	public void cargaSession(Object empresa) {
		 Session session = sessionFactory.openSession();
		 if(!session.contains(empresa))
			{				
				session.lock(empresa, LockMode.READ);				
			}
	 }
	
	/**
	 * Metodo que devuelve una lista de objetos de una clase a partir de unas condiciones
	 * @param obj
	 * @param l
	 * @return
	 */
	public List getIf(Class obj,List <Criterion> l){
		
		Order o=addOrder(obj.getName());
		    	
		return getIf(obj, l,o);
    }
	/**
	 * Metodo que devuelve una lista de objetos de una clase a partir de unas condiciones
	 * con un tipo de ordenacion
	 * @param obj
	 * @param l
	 * @return
	 */
	public List getIf(Class obj,List <Criterion> l,Order order){
		
		boolean open= isOpen();
		
		if(!open){
			startSessionQuery();
		}
		
		Criteria criteria = session.createCriteria(obj);
    	for(Criterion r: l){
    		criteria.add(r);
    	}
    	
    	if(order!=null){
    		criteria.addOrder(order);
    	}
    	
    	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
    	List res=criteria.list();
    	
    	if(!open){
			endSessionQuery();
		}
    	
		return res;
    }
	
	/**
	 * Metodo que devuelve un objeto de una clase a partir de unas condiciones
	 * @param obj
	 * @param l
	 * @return
	 */
    public Object getOneIf(Class obj,List <Criterion> l){	
		
		List lista=getIf(obj,l);
		
		if(lista.isEmpty()){
    		return null;
    	}else{
    		return lista.get(0);
    	}
		
	}
	

	/**
	 * Metodo que devuelve un objeto de una clase a partir de unas condiciones
	 * con un tipo de ordenacion
	 * @param obj
	 * @param l
	 * @return
	 */
	public Object getOneIf(Class obj,List <Criterion> l,Order order){	
		
		List lista=getIf(obj,l,order);
		
		if(lista.isEmpty()){
			return null;
		}else{
			return lista.get(0);
		}
		
	}
	
	public void flush(){
		if(session!=null && isOpen()){
			session.flush();
		}
		
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void clear(){
		if(session!=null && isOpen()){
			session.clear();
		}
	}
	
	public void startSession(){
		session = getSessionFactory().openSession();
		t = new TransactionImpl(session);
		t.begin();
	}

	public void startSessionQuery(){
		session = getSessionFactory().openSession();		
	}
	
	public void endSession(){		
		t.commit();
		session.close();
	}
	
	public void endSessionQuery(){		
		session.close();
	}	
	
	public void rollback(){
		try{
			if(t!=null){
				t.rollback();
			}
			if(session!=null){
				session.close();
			}
		}catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	private boolean isOpen() {
		
		boolean b= false;
		
		if(session!=null && session.isOpen()){
			b=true;
		}		
		
		return b;
	}
	
	/**
	 * Metodo auxiliar. A�ade el criterio de ordenacion especificado en el metodo getOrder() de la clase,
	 * en el caso de que este metodo exista. El metodo devolver� un string con el campo a ordenar
	 * @param criteria
	 * @param clases
	 */
	private Order addOrder(String clases) {

		Order o=null;
		
    	try{
	    	Class cls = Class.forName(clases);
			Method meth = cls.getMethod("getOrder");
			String s=(String)meth.invoke(clases, null);
			if(s!=null){
				 o=Order.asc(s);
			}
    	}catch (Exception e) {
    		LOG.debug("No se ha encontrado metodo de ordenaci�n");
    	}

    	return o;
	}
	
	
	/**
	 * Metodo auxiliar, comprueba si es null el id del objeto
	 * */
	  boolean isNull(Object o) {

		  boolean b=false;
		  try{
		  Class cls = Class.forName(o.getClass().getName());
		  Method meth = cls.getMethod("getId");
		  Integer i=(Integer)meth.invoke(o, null);
		  b=(i==null);
		  }catch (Exception e) {
			b=false;
		  }
		return b;
	}	
	
	
}

