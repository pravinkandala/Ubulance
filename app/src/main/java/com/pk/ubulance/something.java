package com.pk.ubulance;

/**
 * Created by Pravin on 10/8/16.
 * Project: Ubulance
 */
class A {
    public String sayHello() {
        return "Hello from A";
    }
}

class B extends A {
    @Override
    public String sayHello() {
        return "Hello from B";
    }
}
public class something {
    public static void main(String... args) {
        new A().sayHello();
        new B().sayHello();

        A a = new B();
        a.sayHello();

        A a1 = new A() {
            @Override
            public String sayHello() {
                return "Hello from Anonymous Class";
            }
        };

        a1.sayHello();
    }
}
