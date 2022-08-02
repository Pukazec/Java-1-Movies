/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.leoskvorc.dal.sql;

import hr.leoskvorc.model.Movie;
import hr.leoskvorc.model.Person;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import hr.leoskvorc.dal.MovieRepository;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author lskvo
 */
public class SqlMovieRepository implements MovieRepository {

    private static final String ID_MOVIE = "IdMovie";
    private static final String TITLE = "Title";
    private static final String DESCRIPTION = "Description";
    private static final String ORIGINALNAME = "OriginalName";
    private static final String LINK = "Link";
    private static final String PERSON_ID = "DirectorId";
    private static final String LENGTH = "Length";
    private static final String GENRE = "Genre";
    private static final String PICTUREPATH = "PicturePath";
    private static final String INCINEMAFROM = "InCinemaFrom";
    private static final String PUBLISHED = "Published";
    private static final String DIRECTOR = "Director";

    private static final String ID_PERSON = "IdPerson";
    private static final String NAME = "Name";

    private static final String ID_ACTOR = "IdActor";

    private static final String CREATE_MOVIE = "{ CALL createMovie (?,?,?,?,?,?,?,?,?,?,?) }";
    private static final String UPDATE_MOVIE = "{ CALL updateMovie (?,?,?,?,?,?,?,?,?,?,?) }";
    private static final String DELETE_MOVIE = "{ CALL deleteMovie (?) }";
    private static final String SELECT_MOVIE = "{ CALL selectMovie (?) }";
    private static final String SELECT_MOVIES = "{ CALL selectMovies }";
    private static final String DELETE_MOVIES = "{ CALL deleteMovies }";

    private static final String CREATE_PERSON = "{ CALL createPerson (?,?) }";
    private static final String UPDATE_PERSON = "{ CALL updatePerson (?,?) }";
    private static final String DELETE_PERSON = "{ CALL deletePerson (?) }";
    private static final String SELECT_PERSON = "{ CALL selectPerson (?) }";
    private static final String SELECT_PERSONS = "{ CALL selectPersons }";

    private static final String CREATE_ACTOR = "{ CALL createActor (?,?,?) }";
    private static final String UPDATE_ACTOR = "{ CALL updateActor (?,?,?) }";
    private static final String DELETE_ACTOR = "{ CALL deleteActor (?) }";
    private static final String SELECT_ACTORS = "{ CALL selectActors (?) }";

    @Override
    public int createMovie(Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        int directorId = createPerson(movie.getDirector());

        int movieId;

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE)) {

            stmt.setString("@" + TITLE, movie.getTitle());
            stmt.setString("@" + DESCRIPTION, movie.getDescription());
            stmt.setString("@" + ORIGINALNAME, movie.getOriginalName());
            stmt.setString("@" + LINK, movie.getLink());
            stmt.setInt("@" + DIRECTOR, directorId);
            stmt.setInt("@" + LENGTH, movie.getLength());
            stmt.setString("@" + GENRE, movie.getGenre());
            stmt.setString("@" + PICTUREPATH, movie.getPicturePath());
            stmt.setString("@" + INCINEMAFROM, movie.getInCinemaFrom());
            stmt.setString("@" + PUBLISHED, movie.getPublished().format(Movie.DATE_FORMATTER));
            stmt.registerOutParameter("@" + ID_MOVIE, Types.INTEGER);

            stmt.executeUpdate();
            movieId = stmt.getInt("@" + ID_MOVIE);
        }
        
        movie.setId(movieId);

        if (movie.getActors() != null) {
            for (Person actor : movie.getActors()) {
                createActors(actor, movie);
            }
        }

        return movieId;
    }

    @Override
    public void createMovies(List<Movie> movies) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        for (Movie movie : movies) {
            int personId = createPerson(movie.getDirector());
            movie.getDirector().setId(personId);
        }

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE)) {

            for (Movie movie : movies) {
                stmt.setString("@" + TITLE, movie.getTitle());
                stmt.setString("@" + DESCRIPTION, movie.getDescription());
                stmt.setString("@" + ORIGINALNAME, movie.getOriginalName());
                stmt.setString("@" + LINK, movie.getLink());
                stmt.setInt("@" + DIRECTOR, movie.getDirector().getId());
                stmt.setInt("@" + LENGTH, movie.getLength());
                stmt.setString("@" + GENRE, movie.getGenre());
                stmt.setString("@" + PICTUREPATH, movie.getPicturePath());
                stmt.setString("@" + INCINEMAFROM, movie.getInCinemaFrom());
                stmt.setString("@" + PUBLISHED, movie.getPublished().format(Movie.DATE_FORMATTER));
                stmt.registerOutParameter("@" + ID_MOVIE, Types.INTEGER);

                stmt.executeUpdate();
                movie.setId(stmt.getInt("@" + ID_MOVIE));
            }
        }

        for (Movie movie : movies) {
            if (movie.getActors() != null) {
                for (Person actor : movie.getActors()) {
                    actor.setId(createPerson(actor));
                    createActors(actor, movie);
                }
            }
        }
    }

    @Override
    public void updateMovie(int id, Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        updatePerson(movie.getDirector().getId(), movie.getDirector());
        deleteActor(movie.getId());
        if (movie.getActors() != null) {
            for (Person actor : movie.getActors()) {
                createActors(actor, movie);
            }
        }

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_MOVIE)) {

                stmt.setInt("@" + ID_MOVIE, id);
                stmt.setString("@" + TITLE, movie.getTitle());
                stmt.setString("@" + DESCRIPTION, movie.getDescription());
                stmt.setString("@" + ORIGINALNAME, movie.getOriginalName());
                stmt.setString("@" + LINK, movie.getLink());
                stmt.setInt("@" + DIRECTOR, movie.getDirector().getId());
                stmt.setInt("@" + LENGTH, movie.getLength());
                stmt.setString("@" + GENRE, movie.getGenre());
                stmt.setString("@" + PICTUREPATH, movie.getPicturePath());
                stmt.setString("@" + INCINEMAFROM, movie.getInCinemaFrom());
                stmt.setString("@" + PUBLISHED, movie.getPublished().format(Movie.DATE_FORMATTER));

                stmt.executeUpdate();
        }
    }
    
    @Override
    public void deleteMovies() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_MOVIES)) {
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_MOVIE)) {

            stmt.setInt("@" + ID_MOVIE, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Movie> selectMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIE)) {

            stmt.setInt("@" + ID_MOVIE, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Movie(
                            rs.getInt(ID_MOVIE),
                            rs.getString(TITLE),
                            rs.getString(DESCRIPTION),
                            rs.getString(ORIGINALNAME),
                            rs.getString(LINK),
                            new Person(rs.getInt(PERSON_ID),
                                    rs.getString(NAME)),
                            selectActors(rs.getInt(ID_MOVIE)),
                            rs.getInt(LENGTH),
                            rs.getString(GENRE),
                            rs.getString(PICTUREPATH),
                            rs.getString(INCINEMAFROM),
                            LocalDateTime.parse(rs.getString(PUBLISHED), Movie.DATE_FORMATTER)));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Movie> selectMovies() throws Exception {
        List<Movie> movies = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIES);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt(ID_MOVIE),
                        rs.getString(TITLE),
                        rs.getString(DESCRIPTION),
                        rs.getString(ORIGINALNAME),
                        rs.getString(LINK),
                        new Person(rs.getInt(PERSON_ID),
                                rs.getString(NAME)),
                        null,
                        rs.getInt(LENGTH),
                        rs.getString(GENRE),
                        rs.getString(PICTUREPATH),
                        rs.getString(INCINEMAFROM),
                        LocalDateTime.parse(rs.getString(PUBLISHED), Movie.DATE_FORMATTER));
                movie.setActors(selectActors(movie.getId()));

                movies.add(movie);
            }
        }

        return movies;
    }

    @Override
    public int createPerson(Person person) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_PERSON)) {

            stmt.setString("@" + NAME, person.getName());
            stmt.registerOutParameter("@" + ID_PERSON, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt("@" + ID_PERSON);
        }
    }

    @Override
    public void updatePerson(int id, Person person) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_PERSON)) {

            stmt.setInt("@" + ID_PERSON, id);
            stmt.setString("@" + NAME, person.getName());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deletePerson(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_PERSON)) {

            stmt.setInt("@" + ID_PERSON, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Person> selectPerson(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_PERSON)) {

            stmt.setInt("@" + ID_PERSON, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Person(
                            rs.getInt(ID_PERSON),
                            rs.getString(NAME)));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Person> selectPersons() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        List<Person> people = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_PERSONS);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                people.add(new Person(
                        rs.getInt(ID_PERSON),
                        rs.getString(NAME)));
            }
        }
        return people;
    }

    @Override
    public int createActors(Person person, Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ACTOR)) {

            stmt.setInt("@" + ID_PERSON, person.getId());
            stmt.setInt("@" + ID_MOVIE, movie.getId());
            stmt.registerOutParameter("@" + ID_ACTOR, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(ID_ACTOR);
        }
    }

    @Override
    public void deleteActor(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_ACTOR)) {

            stmt.setInt("@" + ID_MOVIE, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Set<Person> selectActors(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        Set<Person> actors = new TreeSet<>();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ACTORS)) {

            stmt.setInt("@" + ID_MOVIE, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    actors.add(new Person(
                            rs.getInt(ID_PERSON),
                            rs.getString(NAME)
                    ));
                }
            }
        }

        return actors;
    }

}
