package org.apache.openjpa.util.support;

public class NonBeanClass {
    Integer value;
    Integer otherValue;

    public NonBeanClass(Integer value, Integer otherValue) {
        this.value = value;
        this.otherValue = otherValue;
    }

    public void doSomething(Integer value) {
        if (value > 47) {
            this.value = this.value * 2;
        } else {
            this.value  = value * 2;
        }
    }
}
