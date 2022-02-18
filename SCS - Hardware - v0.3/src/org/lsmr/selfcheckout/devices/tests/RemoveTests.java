package org.lsmr.selfcheckout.devices.tests;

import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.*;

import static org.junit.Assert.*;

import org.junit.*;


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
		Item myItem = new ItemStub(10);
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
		Item myItem = new ItemStub(10);
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
	
	
	@Test
	public void leaveOverloadState1Item()
	{
		myScale = new ElectronicScale(10,1);
		ESOStub myObserver = new ESOStub();
		myScale.attach(myObserver);
		myScale.endConfigurationPhase();
		
		Item myItem = new ItemStub(15);
		try {
			myScale.add(myItem);
		} catch (Exception e1) {
			fail("Failed to add item to ElectronicScale to proceed with test");
		}
		//system should not be in overloaded state
		myScale.remove(myItem);
		assertTrue("The observer was not notified of the outOfOverlead message", myObserver.outOfOver);
	}
	
	@Test
	public void leaveOverloadStateAddMultipleRemove1()
	{
		myScale = new ElectronicScale(10,1);
		ESOStub myObserver = new ESOStub();
		myScale.attach(myObserver);
		myScale.endConfigurationPhase();
		
		Item myItem = new ItemStub(3);
		Item myItem2 = new ItemStub(3);
		Item myItem3 = new ItemStub(15);
		try {
			myScale.add(myItem);
			myScale.add(myItem2);
			myScale.add(myItem3);
		} catch (Exception e1) {
			fail("Failed to add items to ElectronicScale to proceed with test");
		}
		//system should not be in overloaded state
		myScale.remove(myItem3);
		assertTrue("The observer was not notified of the outOfOverlead message", myObserver.outOfOver);
	}
	
	@Test
	public void leaveOverloadStateAddMultipleRemoveMultiple()
	{
		myScale = new ElectronicScale(10,1);
		ESOStub myObserver = new ESOStub();
		myScale.attach(myObserver);
		myScale.endConfigurationPhase();
		
		Item myItem = new ItemStub(3);
		Item myItem2 = new ItemStub(3);
		Item myItem3 = new ItemStub(19);
		Item myItem4 = new ItemStub(34);
		
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
		assertTrue("The observer was not notified of the outOfOverlead message" ,myObserver.outOfOver);
	}
	
	@Test 
	public void weightChanged()
	{
		myScale = new ElectronicScale(10,1);
		ESOStub myObserver = new ESOStub();
		myScale.attach(myObserver);
		myScale.endConfigurationPhase();
		
		Item myItem = new ItemStub(7);
		try {
			myScale.add(myItem);
		} catch (Exception e1) {
			fail("Failed to add item to ElectronicScale to proceed with test");
		}
		//system should not be in overloaded state
		myScale.remove(myItem);
		assertTrue("The observer was not notified of the changeInWeight message", myObserver.change);
	}
}
