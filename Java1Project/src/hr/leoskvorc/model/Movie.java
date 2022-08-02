/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.leoskvorc.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author lskvo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"title", "originalName", "length", "link", "director", "actors", "genre", "description", "picturePath", "inCinemaFrom", "published"})
public class Movie {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    @XmlAttribute
    private int id;
    private String title;
    private String description;
    private String originalName;
    private String link;
    @XmlElement(name = "director")
    private Person director;
    @XmlElementWrapper
    @XmlElement(name = "actor")
    private Set<Person> actors;
    private int length;
    private String genre;
    private String picturePath;
    private String inCinemaFrom;
    @XmlJavaTypeAdapter(PublishedAdapter.class)
    @XmlElement(name = "published")
    private LocalDateTime published;

    public Movie() {
    }

    public Movie(String title, String description, String originalName, String link, Person producer, int length, String genre, String picturePath, String inCinemaFrom, LocalDateTime published) {
        this.title = title;
        this.description = description;
        this.originalName = originalName;
        this.link = link;
        this.director = producer;
        this.length = length;
        this.genre = genre;
        this.picturePath = picturePath;
        this.inCinemaFrom = inCinemaFrom;
        this.published = published;
    }
    
    public Movie(int id, String title, String description, String originalName, String link, Person producer, Set<Person> actor, int length, String genre, String picturePath, String inCinemaFrom, LocalDateTime published) {
        this(title, description, originalName, link, producer, length, genre, picturePath, inCinemaFrom, published);
        this.id = id;
        this.actors = actor;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Person getDirector() {
        return director;
    }

    public void setDirector(Person director) {
        this.director = director;
    }

    public Set<Person> getActors() {
        return actors;
    }

    public void setActors(Set<Person> actors) {
        this.actors = actors;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getInCinemaFrom() {
        return inCinemaFrom;
    }

    public void setInCinemaFrom(String inCinemaFrom) {
        this.inCinemaFrom = inCinemaFrom;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDateTime getPublished() {
        return published;
    }

    public void setPublished(LocalDateTime published) {
        this.published = published;
    }

    @Override
    public String toString() {
        return id + ": " + title;
    }  
        
}
