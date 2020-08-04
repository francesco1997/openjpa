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
public class CopyCustomTest {

    private ProxyManagerImpl proxyManager;
    private Object object;
    private boolean resultNull;

    public CopyCustomTest(TestInput testInput) {
        this.object = testInput.getObject();
        this.resultNull = testInput.isResultNull();
    }

    @Parameterized.Parameters
    public static Collection<TestInput> getParameters(){
        Random r = new Random();
        List<TestInput> testInputs = new ArrayList<>();

        testInputs.add(new TestInput(null, true));
        testInputs.add(new TestInput(new NonBeanClass(r.nextInt(), r.nextInt()), true));

        BeanClass beanClass = new BeanClass();
        beanClass.setValue(r.nextInt());
        testInputs.add(new TestInput(beanClass, false));

        Map<Integer, Integer> map = new HashMap<>();
        map.put(r.nextInt(), r.nextInt());
        map.put(r.nextInt(), r.nextInt());
        map.put(r.nextInt(), r.nextInt());

        testInputs.add(new TestInput(map, false));

        testInputs.add(new TestInput(new Date(), false));

        testInputs.add(new TestInput(new GregorianCalendar(), false));

        Proxy proxy = new ProxyManagerImpl().newDateProxy(Date.class);
        testInputs.add(new TestInput(proxy, false));

        List<Integer> list = new ArrayList<>();

        list.add(r.nextInt());
        list.add(r.nextInt());
        list.add(r.nextInt());

        testInputs.add(new TestInput(list, false));



        return testInputs;

    }

    private static class TestInput {
        private Object object;
        private boolean resultNull;

        public TestInput(Object object, boolean resultNull) {
            this.object = object;
            this.resultNull = resultNull;
        }

        public Object getObject() {
            return object;
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
    public void copyCustomTest() {
        Object result = this.proxyManager.copyCustom(this.object);

        if (this.resultNull) {
            Assert.assertNull(result);
        } else {
            Assert.assertEquals(this.object, result);
        }

    }

}
