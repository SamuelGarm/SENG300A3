package org.lsmr.selfcheckout.devices.tests;

import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.ElectronicScaleObserver;

public class ESOStub implements ElectronicScaleObserver {

    public boolean over = false;
    public boolean change = false;
    public boolean outOfOver = false;
    
    public void weightChanged(ElectronicScale scale, double weightInGrams){
        this.change = true;
    }

    public void overload(ElectronicScale scale){
        this.over = true;
    }

    public void outOfOverload(ElectronicScale scale){
    	outOfOver = true;
    }

    public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
        ;
        
    }


    public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
        ;
        
    }

}
