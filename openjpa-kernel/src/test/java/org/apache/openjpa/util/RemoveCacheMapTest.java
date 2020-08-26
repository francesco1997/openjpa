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

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

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
public class RemoveCacheMapTest {

    private Object key;
    private Object value;
    private CacheMap cacheMap;
    private boolean hasPreviousValue;
    private boolean pinned;
    private Integer cachedMaxMapSize;
    private Integer numObjectToInsert;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    public RemoveCacheMapTest(TestInput testInput) {
        this.key = testInput.getKey();
        this.value = testInput.getValue();
        this.hasPreviousValue = testInput.isAlreadyExist();
        this.pinned = testInput.isPinned();
        this.cachedMaxMapSize = testInput.getCacheMaxMapSize();
        this.numObjectToInsert = testInput.getNumObjectToInsert();
        if (this.hasPreviousValue) {
            this.value = new Object();
        } else {
            this.value = null;
        }
    }


    @Parameterized.Parameters
    public static Collection<TestInput> getParameters(){
        List<TestInput> testInputs = new ArrayList<>();

        testInputs.add(new TestInput(null, null, false, false, 1, 0));
        testInputs.add(new TestInput(new Object(), null, false, true, 1, 0));
        testInputs.add(new TestInput(new Object(), new Object(), true, true, 0, 0));
        testInputs.add(new TestInput(new Object(), new Object(), true, false, 1,1));
        testInputs.add(new TestInput(new Object(), null, true, false, 2, 1));
        return testInputs;

    }

    private static class TestInput {
        private Object key;
        private Object value;
        private boolean alreadyExist;
        private boolean pinned;
        private Integer cacheMaxMapSize;
        private Integer numObjectToInsert;

        public TestInput(Object key, Object value, boolean alreadyExist, boolean pinned, Integer cacheMaxMapSize, Integer numObjectToInsert) {
            this.key = key;
            this.value = value;
            this.alreadyExist = alreadyExist;
            this.pinned = pinned;
            this.cacheMaxMapSize = cacheMaxMapSize;
            this.numObjectToInsert = numObjectToInsert;
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

        public Integer getCacheMaxMapSize() {
            return cacheMaxMapSize;
        }

        public Integer getNumObjectToInsert() {
            return numObjectToInsert;
        }
    }

    @Before
    public void setUp() {
        this.cacheMap = new CacheMap(true, this.cachedMaxMapSize, this.cachedMaxMapSize + 1, 1L, 1);
        if (this.pinned) {
            this.cacheMap.pin(this.key);
        }

        if (this.hasPreviousValue) {
            this.cacheMap.put(this.key, this.value);
        }
        for (int i = 0; i < this.numObjectToInsert; i++) {
            this.cacheMap.put(new Object(), new Object());
        }
        this.cacheMap = spy(this.cacheMap);

    }

    @Test
    public void removeTest() {
        Object deletedValue = this.cacheMap.remove(this.key);
        verify(this.cacheMap).writeLock();
        verify(this.cacheMap).writeUnlock();

        if (this.hasPreviousValue) {
            Assert.assertEquals(this.value, deletedValue);
        } else {
            Assert.assertNull(deletedValue);
        }

        Object value = this.cacheMap.get(this.key);

        Assert.assertNull(value);

    }

}
