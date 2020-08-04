package org.apache.openjpa.util;

import org.apache.openjpa.util.support.BeanClass;
import org.apache.openjpa.util.support.FinalClass;
import org.apache.openjpa.util.support.NonBeanClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.sql.Timestamp;
import java.util.*;

@RunWith(Parameterized.class)
public class NewCustomProxyTest {

    private ProxyManagerImpl proxyManager;
    private Object object;
    private boolean autoOff;
    private String unproxyable;
    private boolean resultNull;

    public NewCustomProxyTest(TestInput testInput) {
        this.object = testInput.getObject();
        this.autoOff = testInput.isAutoOff();
        this.unproxyable = testInput.getUnproxyable();
        this.resultNull = testInput.isResultNull();
    }

    @Parameterized.Parameters
    public static Collection<TestInput> getParameters(){
        Random r = new Random();
        List<TestInput> testInputs = new ArrayList<>();

        testInputs.add(new TestInput(null, true, true));
        testInputs.add(new TestInput(new NonBeanClass(r.nextInt(), r.nextInt()), false,true));
        testInputs.add(new TestInput(new FinalClass(), true, true));

        BeanClass beanClass = new BeanClass();
        beanClass.setValue(r.nextInt());
        testInputs.add(new TestInput(beanClass, true,false));

        Map<Integer, Integer> map = new HashMap<>();
        map.put(r.nextInt(), r.nextInt());
        map.put(r.nextInt(), r.nextInt());
        map.put(r.nextInt(), r.nextInt());

        testInputs.add(new TestInput(map, true, false));

        Map<Integer, Integer> sortedMap = new TreeMap<>();
        sortedMap.put(r.nextInt(), r.nextInt());
        sortedMap.put(r.nextInt(), r.nextInt());
        sortedMap.put(r.nextInt(), r.nextInt());

        testInputs.add(new TestInput(sortedMap, true, false));

        Set<Integer> sortedSet = new TreeSet<>();
        sortedSet.add(r.nextInt());
        sortedSet.add(r.nextInt());
        sortedSet.add(r.nextInt());

        testInputs.add(new TestInput(sortedSet, false, false));

        testInputs.add(new TestInput(new Date(), false, false));

        testInputs.add(new TestInput(new GregorianCalendar(), false, false));

        Proxy proxy = new ProxyManagerImpl().newDateProxy(Date.class);
        testInputs.add(new TestInput(proxy, true, false));

        List<Integer> list = new ArrayList<>();

        list.add(r.nextInt());
        list.add(r.nextInt());
        list.add(r.nextInt());

        testInputs.add(new TestInput(list, true, false));
        testInputs.add(new TestInput(new Timestamp(System.currentTimeMillis()), false, false));

        BeanClass unproxyableClass = new BeanClass();
        unproxyableClass.setValue(r.nextInt());
        testInputs.add(new TestInput(unproxyableClass, false, BeanClass.class.getName(),true));


        return testInputs;

    }

    private static class TestInput {
        private Object object;
        private boolean autoOff;
        private String unproxyable;
        private boolean resultNull;

        public TestInput(Object object, boolean autoOff, boolean resultNull) {
            this.object = object;
            this.autoOff = autoOff;
            this.unproxyable = "";
            this.resultNull = resultNull;

        }

        public TestInput(Object object, boolean autoOff, String unproxyable,boolean resultNull) {
            this(object, autoOff, resultNull);
            this.unproxyable = unproxyable;
        }

        public Object getObject() {
            return object;
        }

        public boolean isAutoOff() {
            return autoOff;
        }

        public String getUnproxyable() {
            return unproxyable;
        }

        public boolean isResultNull() {
            return resultNull;
        }
    }

    @Before
    public void setUp(){
        this.proxyManager = new ProxyManagerImpl();
        this.proxyManager.setUnproxyable(this.unproxyable);
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
