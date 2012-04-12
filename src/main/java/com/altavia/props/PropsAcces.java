/**
 * 
 */
package com.altavia.props;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hkachetel objet permettant d'utiliser PropsUtils
 */
public class PropsAcces {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	PropsUtils propsutls;
	private String propsFileName;

	private PropsAcces(String resourceName) {
		this.setPropsFileName(resourceName);
	}

	/**
	 * unique classe pouvant créer un objet PropsAcces défini pour chaque ressource
	 * 
	 * @author hkachetel
	 * 
	 */
	private static class PropsHolder {
		private static Map<String, PropsAcces> propsAcces = new ConcurrentHashMap<String, PropsAcces>();

		public static PropsAcces getPropsAcces(String resourceName) {
			PropsAcces propsa = propsAcces.get(resourceName);
			if (propsa == null) {
				propsa = new PropsAcces(resourceName);
				propsa.load();
				propsAcces.put(resourceName, propsa);
			}
			return propsa;
		}
	}

	/**
	 * 
	 * @param resourceName
	 * @return PropsAcces to resourceName
	 */
	public static PropsAcces getPropsAcces(String resourceName) {
		return PropsHolder.getPropsAcces(resourceName);
	}

	private void load() {
		log.info("Debut chargement des properties : {}", getPropsFileName());
		propsutls = new PropsUtils();
		propsutls.init(getPropsFileName());
	}

	private void setPropsFileName(String propsFileName) {
		this.propsFileName = propsFileName;
	}

	private String getPropsFileName() {
		return this.propsFileName;
	}

	public PropsUtils getPropsutls() {
		if (propsutls == null) {
			this.load();
		}
		return propsutls;
	}

	public Properties getProperties() {
		return getPropsutls().getProperties();
	}

	public Object getProperty(String propertyName) {
		return getPropsutls().getProperty(propertyName);
	}

	public Object getProperty(String propertyName, String defaultValue) {
		return getPropsutls().getProperty(propertyName, defaultValue);
	}

	public String getStringProperty(String key, String defaultValue) {
		return (String) getProperty(key, "" + defaultValue);
	}

	public int getIntProperty(String key, int defaultValue) {
		String stringVal = getStringProperty(key, "" + defaultValue);
		try {
			return Integer.parseInt(stringVal);
		} catch (NumberFormatException e) {
			log.error("La valeur " + stringVal + " pour la property " + key + " n'est pas valide. Vérifier le fichier " + getPropsFileName()
					+ " dans le classpath");
		}
		return defaultValue;
	}

	public boolean getBooleanProperty(String key, boolean defaultValue) {
		String stringVal = getStringProperty(key, "" + defaultValue);
		try {
			return Boolean.parseBoolean(stringVal);
		} catch (NumberFormatException e) {
			log.error("La valeur " + stringVal + " pour la property " + key + " n'est pas valide. Vérifier le fichier " + getPropsFileName()
					+ " dans le classpath");
		}
		return defaultValue;
	}

	public void addProperty(String propertyName, Object value, boolean saveIntoPropsFile) {
		getPropsutls().addProperty(propertyName, value, saveIntoPropsFile);
	}

	public final void removeProperty(String propertyName, boolean saveIntoPropsFile) {
		getPropsutls().removeProperty(propertyName, saveIntoPropsFile);
	}

	public final void setProperty(String propertyName, Object value, boolean saveIntoPropsFile) {
		getPropsutls().setProperty(propertyName, value, saveIntoPropsFile);
	}
}
