package org.apache.openjpa.util;

import org.apache.openjpa.util.support.BeanClass;
import org.apache.openjpa.util.support.NonBeanClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;


@RunWith(Parameterized.class)
public class CopyCustomTest {

    private ProxyManagerImpl proxyManager;
    private Object object;
    private Comparator<Object> comparator;

    public CopyCustomTest(TestInput testInput) {
        this.object = testInput.getObject();
        this.comparator = testInput.getComparator();
    }

    @Parameterized.Parameters
    public static Collection<TestInput> getParameters(){
        List<TestInput> testInputs = new ArrayList<>();

        testInputs.add(new TestInput(null, null));
        testInputs.add(new TestInput(new NonBeanClass(25, 36), null));

        BeanClass beanClass = new BeanClass();
        beanClass.setValue(36);
        testInputs.add(new TestInput(beanClass, (o1, o2) -> {
            BeanClass b1 = (BeanClass) o1;
            BeanClass b2 = (BeanClass) o2;

            if (b1.getValue().equals(b2.getValue())) {
                return 0;
            } else {
                return -1;
            }
        }));
        return testInputs;

    }

    private static class TestInput {
        Object object;
        Comparator<Object> comparator;

        public TestInput(Object object, Comparator<Object> comparator) {
            this.object = object;
            this.comparator = comparator;
        }

        public Object getObject() {
            return object;
        }

        public Comparator<Object> getComparator() {
            return comparator;
        }
    }

    @Before
    public void setUp(){
        this.proxyManager = new ProxyManagerImpl();
    }

    @Test
    public void copyCustomTest() {
        Object result = this.proxyManager.copyCustom(this.object);

        if (comparator == null) {
            Assert.assertNull(result);
        } else {
            Assert.assertEquals(0, this.comparator.compare(this.object, result));
        }

    }

}
