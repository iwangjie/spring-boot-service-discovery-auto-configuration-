package com.github.iwangjie.discovery.addition;

import org.junit.Assert;
import org.junit.Test;

public class EnableServiceDiscoveryTest {

    @Test
    public void testGetApplicationStartClass() throws ClassNotFoundException {
        main(new String[]{});
    }

    public static void main(String[] args) {
        EnableServiceDiscovery serviceDiscovery = new EnableServiceDiscovery();
        Class aClass = null;
        try {
            aClass = serviceDiscovery.getApplicationStartClass();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(aClass);
        Assert.assertEquals(aClass, EnableServiceDiscoveryTest.class);
    }
}