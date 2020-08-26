package org.apache.openjpa.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

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
public class PinCacheMapTest {

    private Object key;
    private Object value;
    private CacheMap cacheMap;
    private boolean hasPreviousValue;
    private boolean pinned;
    private boolean expectedResult;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    public PinCacheMapTest(TestInput testInput) {
        this.key = testInput.getKey();
        this.hasPreviousValue = testInput.isAlreadyExist();
        this.pinned = testInput.isPinned();
        this.expectedResult = testInput.isExpectedResult();
        if (this.hasPreviousValue) {
            this.value = testInput.getValue();
        } else {
            this.value = null;
        }
    }


    @Parameterized.Parameters
    public static Collection<TestInput> getParameters(){
        List<TestInput> testInputs = new ArrayList<>();

        testInputs.add(new TestInput(null, null, false,false, false));
        testInputs.add(new TestInput(new Object(), new Object(), true,false, true));
        testInputs.add(new TestInput(new Object(), null, false,true, false));
        testInputs.add(new TestInput(new Object(), new Object(), true,true, true));

        return testInputs;

    }

    private static class TestInput {
        private Object key;
        private Object value;
        private boolean alreadyExist;
        private boolean pinned;
        private boolean expectedResult;

        public TestInput(Object key, Object value, boolean alreadyExist, boolean pinned, boolean expectedResult) {
            this.key = key;
            this.value = value;
            this.alreadyExist = alreadyExist;
            this.pinned = pinned;
            this.expectedResult = expectedResult;
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

        public boolean isPinned() {
            return pinned;
        }

        public boolean isExpectedResult() {
            return expectedResult;
        }
    }

    @Before
    public void setUp(){
        this.cacheMap = new CacheMap(true);
        if (this.hasPreviousValue) {
            this.cacheMap.put(this.key, this.value);
        }

        if (this.pinned) {
            this.cacheMap.pin(this.key);
        }

        this.cacheMap = spy(this.cacheMap);
    }

    @Test
    public void pinTest() {
        boolean result = this.cacheMap.pin(this.key);
        verify(this.cacheMap).writeLock();
        verify(this.cacheMap).writeUnlock();

        Assert.assertEquals(this.expectedResult, result);

    }

}
