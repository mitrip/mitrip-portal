package com.kbmstudio.opencms.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * Intefaz que nos ofrece una serie de utilidades para el acceso y modificaci�n de los datos.
 * @author Saga Soluciones Tecnol�gicas
 * @version 1.0 (Rev 1.0)
 * @param <T>
 * @param <ID>
 */
public interface HibernateDao<T,ID extends Serializable> {
	/**
	 * Persiste la lista de objetos enviada
	 * @param l
	 * @return
	 */
	public List persistAll(List l);
	/**
	 * Persiste el objeto enviado por par�metro y lo devuelve. En el caso de no existir 
	 * lo crea, y si ya existe lo actualiza.
	 * @param entity
	 * @return
	 */
	public T persist(T entity);
	/**
	 * Actualiza un objeto en BBDD siempre y cuando ya haya sido creado con anterioridad.
	 * @param entity
	 */
	public void update(T entity);
	/**
	 * Devuelve una instancia de la clase enviada por par�metro con el identificador recibido.
	 * Ej: find(Usuario.class,1) -> devuelve el usuario con identificador 1.
	 * @param entity
	 * @param id
	 * @return
	 */
	public T find(Class entity, ID id);
	/**
	 * Devuelve la lista de todos los registros almacenandos correspondiente a la clase enviada.
	 * @param entity
	 * @return
	 */
	public List<T> findAll(Class entity);
	/**
	 * Borra de la BBDD el objeto enviado
	 * @param entity
	 */
	public void remove(T entity);
	/**
	 * Actualiza o persiste un objeto en BBDD seg�n corresponda
	 * @param entity
	 */
	public void updateOrPersist(T entity);
	/**
	 * Carga en session el objeto enviado por par�metro
	 * @param empresa
	 */
	public void cargaSession(T empresa);
	/**
	 * Realiza una sincronizaci�n de los objetos almacenados en memoria y los almacenados en BBDD
	 */
	public void flush();
	/**
	 * Metodo equivalente el NOT EXISTS de SQL
	 * @param tipo, es el tipo de la clase q se va a listar
	 * @param en,  es el otro tipo de la clase que no debe contener como atributo a tipo
	 * @param relacion, nombre del atributo en la clase 'en' de la clase 'tipo'
	 * @param criterios sobre la clase en
	 * @return
	 */
	public List noExiste(Class tipo, Class en, String relacion, List<Criterion> criterios);
	/**
	 * Metodo equivalente al EXISTS de SQL
	 * @param tipo, es el tipo de la clase q se va a listar
	 * @param en,  es el otro tipo de la clase que  debe contener como atributo a tipo
	 * @param relacion, nombre del atributo en la clase 'en' de la clase 'tipo'
	 * @param criterios sobre la clase en
	 * @return
	 */
	public List existe(Class tipo, Class en, String relacion, List<Criterion> criterios);
	/**
	 * Metodo que devuelve una lista de objetos de una clase a partir de unas condiciones
	 * @param obj
	 * @param l
	 * @return
	 */
	public List getIf(Class obj, List <Criterion> l);
	/**
	 * Metodo que devuelve una lista de objetos de una clase a partir de unas condiciones
	 * con un tipo de ordenacion
	 * @param obj
	 * @param l
	 * @return
	 */
	public List getIf(Class obj, List <Criterion> l, Order order);
	/**
	 * Metodo que devuelve un objeto de una clase a partir de unas condiciones
	 * @param obj
	 * @param l
	 * @return
	 */
	public Object getOneIf(Class obj, List <Criterion> l);
	/**
	 * Metodo que devuelve un objeto de una clase a partir de unas condiciones
	 * con un tipo de ordenacion
	 * @param obj
	 * @param l
	 * @return
	 */
	public Object getOneIf(Class obj, List <Criterion> l, Order order);
	/**
	 * Abre una session y lanza una transacci�n asociada
	 */
	public void startSession();
	/**
	 * Abre una nueva session de consulta por lo que no crea una transacci�n asociada
	 */
	public void startSessionQuery();
	/**
	 * Finaliza una session haciendo comit previamente
	 */
	public void endSession();
	/**
	 * Finaliza una session sin hacer comit ya que se trataba de una sesion de consulta no de modificaci�n
	 */
	public void endSessionQuery();
	/**
	 * Realizar un rollback de la transaccion actual
	 */
	public void rollback();
	/**
	 * Devuelve una lista de objetos de una clase determinada
	 * @see com.saga.opencms.hibernate.HibernateDao#findAll(java.lang.Object)
	 */
	public List<Object> findAll(Class entity, Order order);

}
