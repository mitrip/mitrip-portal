package com.kbmstudio.opencms.hibernate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.logging.Log;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;



public class HibernateUtil {
	private static final Log LOG = CmsLog.getLog(HibernateUtil.class);
	public static final String CONFIGURATION_FOLDER = "classes/com/saga/opencms/hibernate/configuration/";
	private static final HibernateUtil singleton = new HibernateUtil();
	

	protected SessionFactory sessionFactory;
	protected AnnotationConfiguration acfg;
	
	private HibernateUtil(){}
	
	public static HibernateUtil getInstance(){
		return singleton;
	}

	public SessionFactory getSessionFactory(){
		if(null == sessionFactory)
			createSessionFactory(CONFIGURATION_FOLDER,null);
		return sessionFactory;
	}
	
	/**
	 * Crea el Sesion factory
	 * @param configuracionProperties : Indica donde esta el archivo de configuraci�n en caso de no usar el de por defecto
	 * @param configuration_folfer: Indica la ruta donde se encuentran las clases anotadas
	 * @return
	 */
	public SessionFactory getSessionFactory(String configuration_folfer, String configuracionProperties){
		if(null == sessionFactory)
			createSessionFactory(configuration_folfer,configuracionProperties);
		return sessionFactory;
	}
	
    private void createSessionFactory(String configuration_folfer, String configuracionProperties) {
		initConfiguration(getAnnotatedClassesList(configuration_folfer));
		
		try{
		
			if(configuracionProperties!=null){
				String file = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf(configuracionProperties);
				acfg.addFile(new File(file));
				this.sessionFactory = acfg.configure(new File(file)).buildSessionFactory();
			}else{
				this.sessionFactory = acfg.buildSessionFactory();
			}
					
		}catch (HibernateException e) {
			LOG.error("ERROR CREANDO EL SESION FACTORY. Mensaje:"+e.getMessage());
			e.printStackTrace();
		}
	}

	private void initConfiguration(List<String> listClasses){
		this.acfg = new AnnotationConfiguration();
		LOG.info("LOADING ANNOTATED ENTITIES");
        for(String c: listClasses){
        	LOG.info("LOADING ANNOTATED ENTITY: "+c);
        	try {
				acfg.addAnnotatedClass(Class.forName(c));
			} catch (MappingException e) {
				LOG.error("ERROR LOADING HIBERNATE ANNOTATED CLASS", e);
			} catch (ClassNotFoundException e) {
				LOG.error("ERROR LOADING HIBERNATE ANNOTATED CLASS", e);
			}
        	LOG.info("ANNOTATED ENTITY "+c+" LOADED");
        }
	}
	
	private List<String> getAnnotatedClassesList(String configuration_folfer){
		String path = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf(configuration_folfer);
		List<String> classes = new ArrayList<String>();
		File folder;
		BufferedReader breader = null;
		try {
			folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			try {
				for(int i = 0; i < listOfFiles.length; i++){
					if(listOfFiles[i].isFile()){
						breader = new BufferedReader(new FileReader(listOfFiles[i]));
						for(String line = breader.readLine(); line != null && !"".equals(line);line = breader.readLine()){
							classes.add(line);
						}
					}
				}
			} finally{
				if(breader!=null)
					breader.close();
			}
		} catch (FileNotFoundException e) {
			LOG.error("FILE NOT FOUND", e);
		} catch (IOException e) {
			LOG.error("ERROR I/O", e);
		}
		return classes;
	}
    
}
