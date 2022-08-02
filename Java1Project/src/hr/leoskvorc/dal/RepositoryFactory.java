/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.leoskvorc.dal;

import hr.leoskvorc.dal.sql.SqlMovieRepository;
import hr.leoskvorc.dal.sql.SqlUserRepository;

/**
 *
 * @author lskvo
 */
public class RepositoryFactory {
    
    private RepositoryFactory() {
    }
    
    public static MovieRepository getMovieRepository() throws Exception {
        return new SqlMovieRepository();
    }
    
    public static UserRepository getUserRepository() throws Exception{
        return new SqlUserRepository();
    }
    
}
