package com.wolf.reflect;

import java.io.Serializable;

public class Person implements Serializable {
    private Long id;
    public String name;

    public Person() {
        System.out.println("无参构造！");
    }

    public Person(Long id) {
        this.id = id;
        System.out.println("一个参数id的构造函数！");
    }

    private Person(String name) {
        this.name = name;
        System.out.println("一个参数name的私有构造函数！");
    }

    public Person(Long id, String name) {
        this.id = id;
        this.name = name;
        System.out.println("两个参数的构造函数！");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    private String getSomeThing() {
        return "get some thing ... ";
    }
}
