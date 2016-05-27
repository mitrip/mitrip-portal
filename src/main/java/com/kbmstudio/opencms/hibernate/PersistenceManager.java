package com.kbmstudio.opencms.hibernate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import org.apache.commons.logging.Log;
import org.hibernate.MappingException;
import org.hibernate.ejb.Ejb3Configuration;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;

public class PersistenceManager {
	private static final Log LOG = CmsLog.getLog(PersistenceManager.class);
	private static final PersistenceManager singleton = new PersistenceManager();
	private static final String CONFIGURATION_FOLDER = "classes/com/saga/opencms/hibernate/configuration/";
	protected EntityManagerFactory emf; 
	protected Ejb3Configuration ejb;
	
	private PersistenceManager(){}
	
	public static PersistenceManager getInstance(){
		return singleton;
	}
	
	public EntityManagerFactory getEntityManagerFactory(){
		if(emf == null)
			createEntityManagerFactory();
		return emf;
	}
	
	protected void createEntityManagerFactory() {
		addAnnotatedClassesToConfiguration(getAnnotatedClassesList());
        this.emf = ejb.buildEntityManagerFactory();
        LOG.info("PERSISTENCE STARTED AT "+ new Date());
	}
	
	public void closeEntityManagerFactory() {
	    if (emf != null) {
	      emf.close();
	      emf = null;
	      LOG.info("PERSISTENCE FINISHED AT " + new Date());
	    }
	}
	
	protected List<String> getAnnotatedClassesList(){
		String path = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf(CONFIGURATION_FOLDER);
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
				breader.close();
			}
		} catch (FileNotFoundException e) {
			LOG.error("FILE NOT FOUND", e);
		} catch (IOException e) {
			LOG.error("ERROR I/O", e);
		}
		return classes;
	}
	
	protected void addAnnotatedClassesToConfiguration(List<String> classes){
		ejb = new Ejb3Configuration();
		LOG.info("LOADING ANNOTATED ENTITIES");
        for(String c: classes){
        	LOG.info("LOADING ANNOTATED ENTITY... : "+c);
        	try {
				ejb.addAnnotatedClass(Class.forName(c));
			} catch (MappingException e) {
				LOG.error("ERROR LOADING HIBERNATE ANNOTATED CLASS", e);
			} catch (ClassNotFoundException e) {
				LOG.error("ERROR LOADING HIBERNATE ANNOTATED CLASS", e);
			}
        	LOG.info("ANNOTATED ENTITY "+c+" LOADED!!");
        }
	}
}
