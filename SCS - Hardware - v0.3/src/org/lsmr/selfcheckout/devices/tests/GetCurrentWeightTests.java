package org.lsmr.selfcheckout.devices.tests;

import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.*;
import static org.junit.Assert.*;

import org.junit.*;

class createItem extends Item {
	createItem() {
		super(10);
	}
	createItem(double weight) {
		super(weight);
	}
}

public class GetCurrentWeightTests {
	ElectronicScale myScale;
	
	@Before
	public void setUp()
	{
		myScale = new ElectronicScale(10, 10);
	}
	
	@Test
	public void PhaseError() {
		myScale.forceErrorPhase();
		try {
			myScale.getCurrentWeight();
			fail("Expected SimulationException to be thrown in Error Phase, No exception thrown");
		}
		catch(SimulationException e) {
			assertTrue("Expected SimulationException to be thrown", e instanceof SimulationException);
		}
		catch (Exception e) {
			fail("Expected SimulationException to be thrown, but " + e + " exception has been thrown");
		}
	}
	
	@Test
	public void PhaseConfiguration() {
		try {
			myScale.getCurrentWeight();
			fail("Expected SimulationException to be thrown in Configuration Phase, No exception thrown");
		}
		catch (SimulationException e) {
			assertTrue("Expected SimulationException to be thrown", e instanceof SimulationException);
		}
		catch (Exception e)	{
			fail("Expected SimulationException to be thrown, but " + e + " exception has been thrown");
		}
	}
	
	@Test
	public void OverloadScale() {
		createItem new_item = new createItem(15);
		myScale.endConfigurationPhase();
		try {
			myScale.add(new_item);
		} catch (Exception e1) {
			fail("Unexpected exception thrown while adding item, can't proceed with test");
		}
		try {
			myScale.getCurrentWeight();
			fail("Expected OverloadException to be thrown but, No exception thrown");
		}
		catch (OverloadException e) {
			assertTrue("Expected OverloadException to be thrown", e instanceof OverloadException);
		}
	}
	
	@Test
	public void weightTest() {
		createItem new_item = new createItem(5);
		myScale.endConfigurationPhase();
		try {
			myScale.add(new_item);
		} catch (Exception e1) {
			fail("Unexpected exception thrown while adding item, can't proceed with test");
		}
		try {
			double actual_result = myScale.getCurrentWeight();
			Assert.assertEquals(5.0, actual_result, 1.0);
		}
		catch (Exception e) {
			fail("Expected no exception, but " + e + " has been thrown");
		}
	}
}
