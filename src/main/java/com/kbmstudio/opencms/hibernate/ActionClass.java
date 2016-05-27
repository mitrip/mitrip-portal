package com.kbmstudio.opencms.hibernate;

import org.apache.commons.logging.Log;
import org.opencms.configuration.CmsConfigurationManager;
import org.opencms.db.CmsPublishList;
import org.opencms.file.CmsObject;
import org.opencms.main.CmsEvent;
import org.opencms.main.CmsLog;
import org.opencms.module.CmsModule;
import org.opencms.module.I_CmsModuleAction;
import org.opencms.report.I_CmsReport;

public class ActionClass implements I_CmsModuleAction{
	private static final Log LOG = CmsLog.getLog(ActionClass.class);
	public void initialize(CmsObject adminCms,CmsConfigurationManager configurationManager, CmsModule module) {
		
		String configuracion_folder=HibernateUtil.CONFIGURATION_FOLDER;
		
		String configuracion_properties=null;
		
		if(module.getParameters().containsKey("CONFIGURATION_FOLDER")){
			configuracion_folder=module.getParameter("CONFIGURATION_FOLDER");
		}
		
		if(module.getParameters().containsKey("CONFIGURATION_PROPERTIES")){
			configuracion_properties=module.getParameter("CONFIGURATION_PROPERTIES");
		}
		
		HibernateUtil.getInstance().getSessionFactory(configuracion_folder,configuracion_properties);
		
		
//		PersistenceManager.getInstance().getEntityManagerFactory();
		
		
	}

	public void moduleUninstall(CmsModule module) {
		
	}

	public void moduleUpdate(CmsModule module) {
		// TODO Auto-generated method stub
		
	}

	public void publishProject(CmsObject cms, CmsPublishList publishList,
			int publishTag, I_CmsReport report) {
		
	}

	public void shutDown(CmsModule module) {
//		PersistenceManager.getInstance().closeEntityManagerFactory();
		HibernateUtil.getInstance().getSessionFactory().close();
	}

	public void cmsEvent(CmsEvent event) {
		
	}

}
