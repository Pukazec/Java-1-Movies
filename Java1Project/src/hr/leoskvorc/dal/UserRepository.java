/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.leoskvorc.dal;

import hr.leoskvorc.model.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author lskvo
 */
public interface UserRepository {
    
    void createUser(User user) throws Exception;
    Optional<User> selectUser(String userName, String Password) throws Exception;
    List<User> selectUsers() throws Exception;
    
}
