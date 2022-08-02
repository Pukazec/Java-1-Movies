/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.leoskvorc.dal;

import hr.leoskvorc.model.Movie;
import hr.leoskvorc.model.Person;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author lskvo
 */
public interface MovieRepository {
    
    int createMovie(Movie movie) throws Exception;
    void createMovies(List<Movie> movies) throws Exception;
    void updateMovie(int id, Movie data) throws Exception;
    void deleteMovie(int id) throws Exception;
    Optional<Movie> selectMovie(int id) throws Exception;
    List<Movie> selectMovies() throws Exception;
    void deleteMovies() throws Exception;
    
    int createPerson(Person person) throws Exception;
    void updatePerson(int id, Person data) throws Exception;
    void deletePerson(int id) throws Exception;
    Optional<Person> selectPerson(int id) throws Exception;
    List<Person> selectPersons() throws Exception;
    
    int createActors(Person person, Movie movie) throws Exception;
    void deleteActor(int id) throws Exception;
    Set<Person> selectActors(int id) throws Exception;
    
}
