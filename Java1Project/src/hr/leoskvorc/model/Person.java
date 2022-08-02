/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.leoskvorc.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author lskvo
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Person implements Comparable<Person> {
    
    @XmlAttribute
    private int id;
    @XmlElement(name = "personname")
    private String name;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public Person(int id, String name) {
        this(name);
        this.id = id;
    }

    public Person(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            return id == ((Person) obj).id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 13;
        hash = 71 * hash + id;
        return id;
    }

    @Override
    public int compareTo(Person o) {
        return Integer.valueOf(id).compareTo(o.id);
    }
        
}
