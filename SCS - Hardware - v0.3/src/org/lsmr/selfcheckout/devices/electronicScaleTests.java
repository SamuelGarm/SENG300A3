package org.lsmr.selfcheckout.devices;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static junit.framework.TestCase.fail;

import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class electronicScaleTests {
    private final int limit;
    private final int sens;
    private final boolean expected;
    
    public electronicScaleTests(int limit, int sens, boolean expected){
        this.limit = limit;
        this.sens = sens;
        this.expected = expected;
    }

    @Test
    public void testInitialization(){
        try {
            ElectronicScale testE = new ElectronicScale(limit, sens);
            Assert.assertTrue("weight limit not set", testE.getWeightLimit() == limit);
            Assert.assertTrue("sensitivity not set", testE.getSensitivity() == sens);
        }
        catch (Exception e) {
            if (expected == false){
                Assert.assertTrue("min weightLimit or sensitivity not caught", e instanceof SimulationException);
            }
            else
                fail();

        }
    }

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][]{{0, 2, false},
                                            {-1, 2, false},
                                            {1, 2, true},
                                            {2, 0, false},
                                            {2, -1, false},
                }
        );
    }

}
