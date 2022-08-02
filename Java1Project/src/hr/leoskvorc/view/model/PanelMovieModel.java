/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.leoskvorc.view.model;

import hr.leoskvorc.dal.MovieRepository;
import hr.leoskvorc.model.Movie;
import java.util.List;

/**
 *
 * @author lskvo
 */
public abstract class PanelMovieModel extends javax.swing.JPanel {

    private MovieRepository repository;
    
    public void setRepository(MovieRepository repo) {
        repository = repo;
    }
        
    public List<Movie> getMovies() throws Exception {
        return repository.selectMovies();
    }
}
