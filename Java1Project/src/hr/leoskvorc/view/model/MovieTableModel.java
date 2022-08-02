/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.leoskvorc.view.model;

import hr.leoskvorc.model.Movie;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author lskvo
 */
public class MovieTableModel extends AbstractTableModel{

    private static final String[] COLUMN_NAMES = {"Id", "Title", "Original title", "Link", "Description", "Producer", "Actors", "Length", "Genre", "Picture path", "In cinema from", "Published" };
    
    private List<Movie> movies;

    public MovieTableModel(List<Movie> movies) {
        this.movies = movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
    
    @Override
    public int getRowCount() {
        return movies.size();
    }

    @Override
    public int getColumnCount() {
        return Movie.class.getDeclaredFields().length - 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0:
                return movies.get(rowIndex).getId();
            case 1:
                return movies.get(rowIndex).getTitle();
            case 2:
                return movies.get(rowIndex).getOriginalName();
            case 3:
                return movies.get(rowIndex).getLink();
            case 4:
                return movies.get(rowIndex).getDescription();
            case 5:
                return movies.get(rowIndex).getDirector();
            case 6:
                return movies.get(rowIndex).getActors();
            case 7:
                return movies.get(rowIndex).getLength();
            case 8:
                return movies.get(rowIndex).getGenre();
            case 9:
                return movies.get(rowIndex).getPicturePath();
            case 10:
                return movies.get(rowIndex).getInCinemaFrom();
            case 11:
                return movies.get(rowIndex).getPublished();
            default:
                throw new RuntimeException("No such column");
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case 0:
                return Integer.class;
        }
        return super.getColumnClass(columnIndex);
    }
    
}
