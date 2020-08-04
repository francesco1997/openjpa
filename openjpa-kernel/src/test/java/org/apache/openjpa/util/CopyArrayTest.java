package org.apache.openjpa.util;

import org.apache.openjpa.util.support.NonBeanClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;


@RunWith(Parameterized.class)
public class CopyArrayTest {

    private ProxyManagerImpl proxyManager;
    private Object object;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    public CopyArrayTest(TestInput testInput) {
        this.object = testInput.getObject();
        if (testInput.getExpectedException() != null) {
            this.expectedException.expect(testInput.getExpectedException());
        }
    }

    @Parameterized.Parameters
    public static Collection<TestInput> getParameters(){
        Random r = new Random();
        List<TestInput> testInputs = new ArrayList<>();

        testInputs.add(new TestInput(null, null));
        testInputs.add(new TestInput(new NonBeanClass(r.nextInt(), r.nextInt()), UnsupportedException.class));

        Integer[] list = new Integer[]{r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt()};

        testInputs.add(new TestInput(list, null));



        return testInputs;

    }

    private static class TestInput {
        private Object object;
        private Class<? extends Exception> expectedException;


        public TestInput(Object object, Class<? extends Exception> expectedException) {
            this.object = object;
            this.expectedException = expectedException;
        }

        public Object getObject() {
            return object;
        }

        public Class<? extends Exception> getExpectedException() {
            return expectedException;
        }
    }

    @Before
    public void setUp(){
        this.proxyManager = new ProxyManagerImpl();
    }

    @Test
    public void copyCustomTest() {
        Object result = this.proxyManager.copyArray(this.object);

        Assert.assertArrayEquals((Object[]) this.object, (Object[]) result);

    }

}
