/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.leoskvorc.parsers.rss;

import hr.leoskvorc.factory.ParserFactory;
import hr.leoskvorc.factory.UrlConnectionFactory;
import hr.leoskvorc.model.Movie;
import hr.leoskvorc.model.Person;
import hr.leoskvorc.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author lskvo
 */
public class MovieParser {

    private static final String RSS_URL = "https://www.blitz-cinestar.hr/rss.aspx?najava=1";
    private static final String EXT = ".jpg";
    private static final String DIR = "assets";
    private static final String DEL = ", ";

    public MovieParser() {
    }

    public static List<Movie> parse() throws IOException, XMLStreamException {
        List<Movie> movies = new ArrayList<>();
        HttpURLConnection con = UrlConnectionFactory.getHttpUrlConnection(RSS_URL);

        try (InputStream is = con.getInputStream()) {
            XMLEventReader reader = ParserFactory.createStaxParser(is);

            Optional<TagType> tagType = Optional.empty();
            Movie movie = null;
            StartElement startElement = null;
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        tagType = TagType.from(qName);
                        
                        if (tagType.isPresent() && tagType.get().equals(TagType.ITEM)) {
                            movie = new Movie();
                            movies.add(movie);
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        if (tagType.isPresent() && movie != null) {
                            Characters characters = event.asCharacters();
                            String data = characters.getData().trim();
                            switch (tagType.get()) {
                                case TITLE:
                                    if (!data.isEmpty()) {
                                        movie.setTitle(data);
                                    }
                                    break;
                                case DESCRIPTION:
                                    if (!data.isEmpty()) {
                                        movie.setDescription(data.substring(data.lastIndexOf("\">") + 2, data.lastIndexOf("<br />")).trim());
                                    }
                                    break;
                                case ORIGINALNAME:
                                    if (!data.isEmpty()) {
                                        movie.setOriginalName(data);
                                    }
                                    break;
                                case LINK:
                                    if (!data.isEmpty()) {
                                        movie.setLink(data);
                                    }
                                    break;
                                case PRODUCER:
                                    if (!data.isEmpty()) {
                                        movie.setDirector(new Person(data));
                                    }
                                    break;
                                case ACTERS:
                                    if (!data.isEmpty()) {
                                        handleActers(movie, data);
                                    }
                                    break;
                                case LENGTH:
                                    if (!data.isEmpty()) {
                                        movie.setLength(Integer.parseInt(data));
                                    }
                                    break;
                                case GENRE:
                                    if (!data.isEmpty()) {
                                        movie.setGenre(data);
                                    }
                                    break;
                                case PICTUREPATH:
                                    if (startElement != null && movie.getPicturePath() == null) {
                                        handlePicture(movie, data);
                                    }
                                    break;
                                case INCINEMAFROM:
                                    if (!data.isEmpty()) {
                                        movie.setInCinemaFrom(data);
                                    }
                                    break;
                                case PUBLISHED:
                                    if (!data.isEmpty()) {
                                        LocalDateTime inCinemaFrom = LocalDateTime.parse(data, DateTimeFormatter.RFC_1123_DATE_TIME);
                                        movie.setPublished(inCinemaFrom);
                                    }
                                    break;
                            }
                        }
                }
            }
        }

        return movies;
    }

    private static void handleActers(Movie movie, String data) {
        Set<Person> actors = new TreeSet<>();
        String[] actorsList = data.split(DEL);
        for (String actorList : actorsList) {
            actors.add(new Person(actorList));
        }
        movie.setActors(actors);
    }

    private static void handlePicture(Movie movie, String pictureUrl) {
        try {
            String ext = pictureUrl.substring(pictureUrl.lastIndexOf("."));
            if (ext.length() > 4) {
                ext = EXT;
            }
            String pictureName = UUID.randomUUID() + ext;
            String localPicturePath = DIR + File.separator + pictureName;

            FileUtils.copyFromUrl(pictureUrl, localPicturePath);
            movie.setPicturePath(localPicturePath);
        } catch (IOException ex) {
            Logger.getLogger(MovieParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private enum TagType {

        ITEM("item"),
        TITLE("title"),
        DESCRIPTION("description"),
        ORIGINALNAME("orignaziv"),
        LINK("link"),
        PRODUCER("redatelj"),
        ACTERS("glumci"),
        LENGTH("trajanje"),
        GENRE("zanr"),
        PICTUREPATH("plakat"),
        INCINEMAFROM("pocetak"),
        PUBLISHED("pubDate");

        private final String name;

        private TagType(String name) {
            this.name = name;
        }

        private static Optional<TagType> from(String name) {
            for (TagType value : values()) {
                if (value.name.equals(name)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }
    }
}
