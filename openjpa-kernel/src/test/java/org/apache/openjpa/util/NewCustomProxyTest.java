package org.apache.openjpa.util;

import org.apache.openjpa.util.support.BeanClass;
import org.apache.openjpa.util.support.NonBeanClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class NewCustomProxyTest {

    private ProxyManagerImpl proxyManager;
    private Object object;
    private boolean autoOff;
    private boolean resultNull;

    public NewCustomProxyTest(TestInput testInput) {
        this.object = testInput.getObject();
        this.autoOff = testInput.isAutoOff();
        this.resultNull = testInput.isResultNull();
    }

    @Parameterized.Parameters
    public static Collection<TestInput> getParameters(){
        Random r = new Random();
        List<TestInput> testInputs = new ArrayList<>();

        testInputs.add(new TestInput(null, true, true));
        testInputs.add(new TestInput(new NonBeanClass(r.nextInt(), r.nextInt()), false,true));

        BeanClass beanClass = new BeanClass();
        beanClass.setValue(r.nextInt());
        testInputs.add(new TestInput(beanClass, true,false));

        return testInputs;

    }

    private static class TestInput {
        private Object object;
        private boolean autoOff;
        private boolean resultNull;

        public TestInput(Object object, boolean autoOff, boolean resultNull) {
            this.object = object;
            this.autoOff = autoOff;
            this.resultNull = resultNull;


        }

        public Object getObject() {
            return object;
        }

        public boolean isAutoOff() {
            return autoOff;
        }

        public boolean isResultNull() {
            return resultNull;
        }
    }

    @Before
    public void setUp(){
        this.proxyManager = new ProxyManagerImpl();
    }

    @Test
    public void newCustomProxyTest() {
        Object result = this.proxyManager.newCustomProxy(this.object, this.autoOff);

        if (this.resultNull) {
            Assert.assertNull(result);
        } else {
            Assert.assertEquals(this.object, result);
        }

    }

}
