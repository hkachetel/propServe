/**
 * 
 */
package com.altavia.props;

import junit.framework.Assert;

import org.junit.Test;


/**
 * @author hkachetel
 *
 */
public class PropsUtilsTest {
	private PropsAcces propsu = null;
	private String propsFileName = "edity.properties";
	
	@Test
	public void loadTest(){
		try {
			propsu = PropsAcces.getPropsAcces(propsFileName);
		} catch (Exception e) {
			Assert.assertEquals(false, true);
		}
		Assert.assertNotNull(propsu);
		Assert.assertNotNull(propsu.getProperties());
		Assert.assertEquals("ceciest1test", propsu.getProperty("edity.test.value"));		
	}
	
	@Test
	public void modifyTest() throws InterruptedException{
		try {
			propsu = PropsAcces.getPropsAcces(propsFileName);
		} catch (Exception e) {
			Assert.assertEquals(false, true);
		}
		propsu.setProperty("edity.test.value", "modify", true);
		Assert.assertEquals("modify", propsu.getProperty("edity.test.value"));
		
		//on remet la valeur initiale
		propsu.setProperty("edity.test.value", "ceciest1test", true);
	}
	
	@Test
	public void addTest() throws InterruptedException{
		try {
			propsu = PropsAcces.getPropsAcces(propsFileName);
		} catch (Exception e) {
			Assert.assertEquals(false, true);
		}
		propsu.addProperty("edity.new.value", "new", true);
		Assert.assertEquals("new", propsu.getProperty("edity.new.value"));
		
		//on supprime la property
		propsu.removeProperty("edity.new.value", true);
	}
	
}
