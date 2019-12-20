package org.miaohong.fishrpc.example.proto;


public class Person {

    int age;
    String name;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return name + " : " + age;
    }
}
