package org.apache.openjpa.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
CacheMap maintains
a fixed number of cache entries, and an
optional soft reference map for entries
that are moved out of the LRU space. So,
for applications that have a monotonically
increasing number of distinct queries, this
option can be used to ensure that a fixed
amount of memory is used by the cache.
*/

@RunWith(Parameterized.class)
public class PutCacheMapTest {

    private Object key;
    private Object value;
    private Object previousValue;
    private CacheMap cacheMap;
    private boolean hasPreviousValue;


    public PutCacheMapTest(TestInput testInput) {
        this.key = testInput.getKey();
        this.value = testInput.getValue();
        this.hasPreviousValue = testInput.isAlreadyExist();
        if (this.hasPreviousValue) {
            this.previousValue = new Object();
        } else {
            this.previousValue = null;
        }
    }


    @Parameterized.Parameters
    public static Collection<TestInput> getParameters(){
        List<TestInput> testInputs = new ArrayList<>();

        testInputs.add(new TestInput(null, null, false));
        testInputs.add(new TestInput(new Object(), null, false));
        testInputs.add(new TestInput(new Object(), new Object(), false));
        testInputs.add(new TestInput(new Object(), new Object(), true));
        testInputs.add(new TestInput(new Object(), null, true));
        return testInputs;

    }

    private static class TestInput {
        private Object key;
        private Object value;
        private boolean alreadyExist;

        public TestInput(Object key, Object value, boolean alreadyExist) {
            this.key = key;
            this.value = value;
            this.alreadyExist = alreadyExist;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public boolean isAlreadyExist() {
            return alreadyExist;
        }
    }

    @Before
    public void setUp(){
        this.cacheMap = new CacheMap(true);
        if (this.hasPreviousValue) {
            this.cacheMap.put(this.key, this.previousValue);
        }
    }

    @Test
    public void putTest() {
        Object previousValue = this.cacheMap.put(this.key, this.value);

        if (this.hasPreviousValue) {
            Assert.assertEquals(this.previousValue, previousValue);
        } else {
            Assert.assertNull(previousValue);
        }

        Object getValue = this.cacheMap.get(this.key);

        Assert.assertEquals(this.value, getValue);

    }

}
