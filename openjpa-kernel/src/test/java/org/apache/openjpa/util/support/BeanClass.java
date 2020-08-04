package org.apache.openjpa.util.support;

public class BeanClass {
    Integer value;

    public BeanClass() {}


    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            BeanClass bean = (BeanClass) obj;
            return this.value.equals(bean.getValue());
        } catch (Exception e) {
            return false;
        }
    }
}
