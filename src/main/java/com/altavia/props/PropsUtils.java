/**
 * 
 */
package com.altavia.props;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hkachetel
 *
 */
public class PropsUtils {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private String 		resourceName 	= null;
	private Properties 	properties 		= new Properties();
	private long 		lastModified	= 0;
	private final 		Timer 	timer 	= new Timer(true);
	private File 		propertiesFile;
	private int 		timerPeriod 	= 3600;//ms
	
	public PropsUtils() {}
	
	/**
	 * initialisation avec le nom du fichier properties
	 * @param resourceName
	 */
	public void init(String resourceName){
		this.resourceName = "/"+resourceName;
		URL url = this.getClass().getResource(this.resourceName);
		if(url != null){
			propertiesFile = new File(url.getFile());
		}		
		timer.schedule(new TimerTask() {
		      public void run() {
		        checkForPropertyChanges();
		      }
		    }, timerPeriod, timerPeriod);
		loadProperties();
	}	
	
	/**
	 * chargement des properties
	 */
	private void loadProperties() {
		try {
	    	 InputStream in = this.getClass().getResourceAsStream(this.resourceName);
	        if (in != null) {
	            properties.load(in);
	        }	        
	    } catch (IOException e) {
	    	log.error("Erreur chargement des properties : {}", e);
	    }
	}
	
	/**
	 * 
	 * @return properties
	 */
	public Properties getProperties() {
		return properties;
	}
	
	/**
	 * verifie si le fichier a été modifié, si oui, on le recharge
	 * cette methode est appelée chaque timerPeriod (ms) par le timer
	 */
	private void checkForPropertyChanges() {
		if (lastModified != propertiesFile.lastModified()) {
			lastModified = propertiesFile.lastModified();
			loadProperties();	      
		}
	}
	
	/**
	 * retourne la valeur d'une property
	 * @param propertyName
	 * @return property value
	 */
	public final Object getProperty(String propertyName) {
		synchronized (properties) {
			return properties.get(propertyName);
		}
	}
	
	/**
	 * retourne la valeur d'une property, sinon la valeur par defaut
	 * @param propertyName
	 * @param defaultValue valeur a retourner si la property est pas trouvee
	 * @return property value
	 */
	public final Object getProperty(String propertyName, String defaultValue) {
		synchronized (properties) {
			return properties.getProperty(propertyName, defaultValue);
		}
	}
	
	/**
	 * ajoute une property
	 * @param propertyName
	 * @param value
	 */
	public final void addProperty(String propertyName, Object value, boolean saveIntoPropsFile) {
		synchronized (properties) {
			if (value != null && !properties.containsKey(propertyName)) {
				properties.put(propertyName, value);
				if(saveIntoPropsFile){
					saveProperties();
				}
			}
		}
	}
	
	/**
	 * supprime une property
	 * @param propertyName
	 * @param value
	 */
	public final void removeProperty(String propertyName, boolean saveIntoPropsFile) {
		synchronized (properties) {
			if (properties.containsKey(propertyName)) {
				properties.remove(propertyName);
				if(saveIntoPropsFile){
					saveProperties();
				}
			}
		}
	}
	
	/**
	 * met a jour une property
	 * @param propertyName
	 * @param value
	 */
	public final void setProperty(String propertyName, Object value, boolean saveIntoPropsFile) {
		synchronized (properties) {
			Object old = properties.get(propertyName);
			if ((value != null && !value.equals(old))
					|| value == null && old != null) {
				if(value == null){
					value = new Object();
				}
				properties.put(propertyName, value);
				if(saveIntoPropsFile){
					saveProperties();
				}
			}
		}
	}
	
	/**
	 * sauvegarde des properties dans le fichier d'origine
	 */
	public void saveProperties() {
		OutputStream outPropFile = null;
		try {
			outPropFile = new FileOutputStream(propertiesFile.getAbsolutePath());
			properties.store(outPropFile, "Properties File to the Test Application");
			lastModified = propertiesFile.lastModified();
		} catch (IOException ioe) {
			log.error("Erreur de sauvegarde des properties : {}", ioe);
		} finally {
			try {
				if(outPropFile != null){
					outPropFile.close();
				}
			} catch (IOException e) {
				log.error("Erreur de fermeture du flux {}", e);
			}
		}
	}
	
	public void forceReload(){
		this.loadProperties();
	}
}
