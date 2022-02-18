package org.lsmr.selfcheckout.devices.tests;

import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SimulationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class AddTests {
    
    private ElectronicScale testScale;
    private ItemStub testItem;
    private ESOStub testESOStub;

    @Before
    public void setup(){
        this.testScale = new ElectronicScale(10, 1);
        this.testESOStub = new ESOStub();               //stub for testing calls of notifyOverload() and notifyWeightChanged()
        this.testScale.attach(testESOStub);
    }

    @Test(expected = SimulationException.class)
    public void sameItem(){                             //adding the same item twice should throw Exception
        this.testItem = new ItemStub(1);
        this.testScale.endConfigurationPhase();
        this.testScale.add(testItem);
        this.testScale.add(testItem);
    }

    @Test
    public void overWeight1(){                          //adding item over weight limit should call notifyOverload()
        this.testItem = new ItemStub(100);
        this.testScale.endConfigurationPhase();
        this.testScale.add(testItem);
        Assert.assertTrue("adding overweight item didn't call notifyOverload()", testESOStub.over == true);
    }

    @Test
    public void overWeight2(){                          //item added equal to limit, so notifyOverload() NOT called
        this.testItem = new ItemStub(10);
        this.testScale.endConfigurationPhase();
        this.testScale.add(testItem);
        Assert.assertTrue("adding item with weight = limit called notifyOverload()", testESOStub.over == false);
    }

    @Test
    public void overWeight3(){                          //item added less than limit, so notifyOverload() NOT called
        this.testItem = new ItemStub(1);
        this.testScale.endConfigurationPhase();
        this.testScale.add(testItem);
        Assert.assertTrue("adding itemweight below limit called notifyOverload()", testESOStub.over == false);
    }

    @Test
    public void sensTest1(){                            //item added less than sensitivity, so notifyWeightChanged() NOT called
        this.testItem = new ItemStub(0.5);
        this.testScale.endConfigurationPhase();
        this.testScale.add(testItem);
        Assert.assertTrue("adding item below sensitivity called notifyWeightChanged()", testESOStub.change == false);
    }

    @Test
    public void sensTest2(){                            //item added equal to sensitivity, so notifyWeightChanged() NOT called
        this.testItem = new ItemStub(1);
        this.testScale.endConfigurationPhase();
        this.testScale.add(testItem);
        Assert.assertTrue("adding item with weight = sensitivity called notifyWeightChanged()", testESOStub.change == false);
    }

    @Test
    public void sensTest3(){                            //item added greater than sensitivity, so notifyWeightChanged() is called
        this.testItem = new ItemStub(2);
        this.testScale.endConfigurationPhase();
        this.testScale.add(testItem);
        Assert.assertTrue("item with weight above sensitivity didn't call notifyWeightChanged", testESOStub.change == true);
    }

    @Test(expected = SimulationException.class)
    public void phaseTest1(){                           //still in CONFIGURATION so throw error
        this.testItem = new ItemStub(2);
        this.testScale.add(testItem);
    }

    @Test(expected = SimulationException.class)
    public void phaseTest2(){                           //in ERROR so throw error
        this.testItem = new ItemStub(2);
        this.testScale.forceErrorPhase();
        this.testScale.add(testItem);
    }
       
}
