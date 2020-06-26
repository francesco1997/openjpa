package org.apache.openjpa.kernel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestDelegatingBroker {


    @Test
    public void test(){
        DelegatingBroker delegatingBroker = new DelegatingBroker(new BrokerImpl());
        delegatingBroker.getAllowReferenceToSiblingContext();
        delegatingBroker.getAutoClear();
    }
}
