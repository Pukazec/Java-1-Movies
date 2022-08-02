/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.leoskvorc.dal.sql;

import hr.leoskvorc.dal.UserRepository;
import hr.leoskvorc.model.User;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

/**
 *
 * @author lskvo
 */
public class SqlUserRepository implements UserRepository{
    
    private static final String ID_USER = "IdUser";
    private static final String USERNAME = "UserName";
    private static final String PASSWORD = "Password";
    private static final String ROLE = "Role";
    
    private static final String CREATE_USER = "{ CALL createUser (?,?,?) }";
    private static final String SELECT_USER = "{ CALL selectUser (?,?) }";
    private static final String SELECT_USERS = "{ CALL selectUsers }";
    
    @Override
    public void createUser(User user) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_USER)) {

            stmt.setString("@" + USERNAME, user.getUserName());
            stmt.setString("@" + PASSWORD, user.getPassword());
            stmt.setObject("@" + ROLE, user.getRole().toString());

            stmt.executeUpdate();
        }

    }

    @Override
    public Optional<User> selectUser(String userName, String Password) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_USER)) {

            stmt.setString("@" + USERNAME, userName);
            stmt.setString("@" + PASSWORD, Password);
            
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt(ID_USER),
                            rs.getString(USERNAME),
                            rs.getString(PASSWORD),
                            User.Role.valueOf(rs.getString(ROLE))));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> selectUsers() throws Exception {
        List<User> users = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_USERS);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt(ID_USER),
                        rs.getString(USERNAME),
                        rs.getString(PASSWORD),
                        User.Role.valueOf(rs.getString(ROLE))));
            }
        }
        return users;
    }

}
