package org.lsmr.selfcheckout.devices.tests;

import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.*;

import static org.junit.Assert.*;

import org.junit.*;


class stubItem extends Item {
	stubItem() {
		super(10);
	}
	stubItem(double weight) {
		super(weight);
	}
}

public class RemoveTests {
	ElectronicScale myScale;
	
	@Before
	public void setUp()
	{
		myScale = new ElectronicScale(10,10);
	}
	

	@Test
	public void ErrorPhase() {
		myScale.forceErrorPhase();	
		try {
			myScale.remove(null);
			fail("Expected SimulationException to be thrown in ERROR phase, no ExceptionThrown");
		}
		catch(SimulationException e) {
			assertTrue("expected simulationException to be thrown", e instanceof SimulationException);
		}
		catch(Exception e) {
			fail("Expected simulationExpection to be thrown, instead " + e + " was thrown");
		}
	}
	
	@Test
	public void ConfigurationPhase() {
		try {
			myScale.remove(null);
			fail("Expected SimulationException to be thrown in ERROR phase, no ExceptionThrown");
		}
		catch(SimulationException e) {
			assertTrue("expected simulationException to be thrown", e instanceof SimulationException);
		}
		catch(Exception e) {
			fail("Expected simulationExpection to be thrown, instead " + e + " was thrown");
		}
	}
	
	@Test
	public void EmptyScaleNullItem() {
		myScale.endConfigurationPhase();
		try {
			myScale.remove(null);
			fail("Expected SimulationException to be thrown in ERROR phase, no ExceptionThrown");
		}
		catch(SimulationException e) {
			assertTrue("expected simulationException to be thrown", e instanceof SimulationException);
		}
		catch(Exception e) {
			fail("Expected simulationExpection to be thrown, instead " + e + " was thrown");
		}
	}
	
	@Test
	public void EmptyScaleValidItem()
	{
		myScale.endConfigurationPhase();
		Item myItem = new stubItem();
		try {
			myScale.remove(myItem);
			fail("Expected SimulationException to be thrown in ERROR phase, no ExceptionThrown");
		}
		catch(SimulationException e) {
			assertTrue("expected simulationException to be thrown", e instanceof SimulationException);
		}
		catch(Exception e) {
			fail("Expected simulationExpection to be thrown, instead " + e + " was thrown");
		}
	}
	
	@Test
	public void nonEmptyScaleValidItem()
	{
		myScale.endConfigurationPhase();
		Item myItem = new stubItem();
		try {
			myScale.add(myItem);
		} catch (Exception e1) {
			fail("Failed to add item to ElectronicScale to proceed with test");
		}
		
		try {
			myScale.remove(myItem);
		}
		catch(Exception e) {
			fail("Expected simulationExpection to be thrown, instead " + e + " was thrown");
		}
	}
	
class stubObserver implements ElectronicScaleObserver {
	public boolean outOfOverloadNotified = false;
	public boolean weightChangedNotified = false;
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {}
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {}
	public void weightChanged(ElectronicScale scale, double weightInGrams) {
		weightChangedNotified = true;
	}
	public void overload(ElectronicScale scale) {}
	public void outOfOverload(ElectronicScale scale) {
		outOfOverloadNotified = true;
	}
	
}
	
	@Test
	public void leaveOverloadState1Item()
	{
		myScale = new ElectronicScale(10,1);
		stubObserver myObserver = new stubObserver();
		myScale.attach(myObserver);
		myScale.endConfigurationPhase();
		
		stubItem myItem = new stubItem(15);
		try {
			myScale.add(myItem);
		} catch (Exception e1) {
			fail("Failed to add item to ElectronicScale to proceed with test");
		}
		//system should not be in overloaded state
		myScale.remove(myItem);
		assertTrue("The observer was not notified of the outOfOverlead message", myObserver.outOfOverloadNotified);
	}
	
	@Test
	public void leaveOverloadStateAddMultipleRemove1()
	{
		myScale = new ElectronicScale(10,1);
		stubObserver myObserver = new stubObserver();
		myScale.attach(myObserver);
		myScale.endConfigurationPhase();
		
		stubItem myItem = new stubItem(3);
		stubItem myItem2 = new stubItem(3);
		stubItem myItem3 = new stubItem(15);
		try {
			myScale.add(myItem);
			myScale.add(myItem2);
			myScale.add(myItem3);
		} catch (Exception e1) {
			fail("Failed to add items to ElectronicScale to proceed with test");
		}
		//system should not be in overloaded state
		myScale.remove(myItem3);
		assertTrue("The observer was not notified of the outOfOverlead message", myObserver.outOfOverloadNotified);
	}
	
	@Test
	public void leaveOverloadStateAddMultipleRemoveMultiple()
	{
		myScale = new ElectronicScale(10,1);
		stubObserver myObserver = new stubObserver();
		myScale.attach(myObserver);
		myScale.endConfigurationPhase();
		
		stubItem myItem = new stubItem(3);
		stubItem myItem2 = new stubItem(3);
		stubItem myItem3 = new stubItem(19);
		stubItem myItem4 = new stubItem(34);
		
		try {
			myScale.add(myItem);
			myScale.add(myItem2);
			myScale.add(myItem3);
			myScale.add(myItem4);
		} catch (Exception e1) {
			fail("Failed to add items to ElectronicScale to proceed with test");
		}
		//system should not be in overloaded state
		myScale.remove(myItem3);
		myScale.remove(myItem4);
		assertTrue("The observer was not notified of the outOfOverlead message" ,myObserver.outOfOverloadNotified);
	}
	
	@Test 
	public void weightChanged()
	{
		myScale = new ElectronicScale(10,1);
		stubObserver myObserver = new stubObserver();
		myScale.attach(myObserver);
		myScale.endConfigurationPhase();
		
		stubItem myItem = new stubItem(7);
		try {
			myScale.add(myItem);
		} catch (Exception e1) {
			fail("Failed to add item to ElectronicScale to proceed with test");
		}
		//system should not be in overloaded state
		myScale.remove(myItem);
		assertTrue("The observer was not notified of the changeInWeight message", myObserver.weightChangedNotified);
	}
}
