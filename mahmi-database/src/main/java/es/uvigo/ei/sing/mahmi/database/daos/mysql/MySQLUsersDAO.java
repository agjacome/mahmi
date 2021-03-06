package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.val;
import lombok.experimental.ExtensionMethod;

import fj.control.db.DB;
import fj.data.Option;

import org.jasypt.util.password.StrongPasswordEncryptor;

import es.uvigo.ei.sing.mahmi.common.entities.User;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.UsersDAO;

import static es.uvigo.ei.sing.mahmi.common.entities.User.user;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.*;

@ExtensionMethod(IterableExtensionMethods.class)
public class MySQLUsersDAO extends MySQLAbstractDAO<User> implements UsersDAO{
    
    private MySQLUsersDAO(final ConnectionPool connectionPool) {
        super(connectionPool);
    }

    public static UsersDAO mysqlUsersDAO(
        final ConnectionPool connectionPool
    ) {
        return new MySQLUsersDAO(connectionPool);
    }
    
    @Override
    protected DB<PreparedStatement> prepareUpdate(final User user) {
        throw new UnsupportedOperationException("Not valid operation");     
    }   
  
  
    @Override
    protected User parse(ResultSet resultSet) throws SQLException {
        val id           = parseIdentifier(resultSet, "user_id");
        val name         = parseString(resultSet,"user_name");
        val organization = parseString(resultSet,"user_organization");
        val username     = parseString(resultSet,"user_username");

        return user(id, name, organization, username, "");
    }
    
    private User parseWithPass (ResultSet resultSet) throws SQLException {
        val id           = parseIdentifier(resultSet, "user_id");
        val name         = parseString(resultSet,"user_name");
        val organization = parseString(resultSet,"user_organization");
        val username     = parseString(resultSet,"user_username");
        val pass         = parseString(resultSet,"user_pass");

        return user(id, name, organization, username, pass);
    }
    
    @Override
    protected DB<PreparedStatement> prepareSelect(User user) {
        return sql(
                "SELECT * FROM users WHERE user_username=? LIMIT 1",
                user.getUserName()
            );
    }
    
    @Override
    protected DB<PreparedStatement> prepareCount() {
        throw new UnsupportedOperationException("Not valid operation");
    }
    
    @Override
    protected DB<PreparedStatement> prepareSelect(Identifier id) {
        return sql(
                "SELECT * FROM users WHERE user_id=? LIMIT 1",
                id
            );
    }
    
    @Override
    protected DB<PreparedStatement> prepareSelect(int limit, int offset) {
        return sql(
                "SELECT * FROM users LIMIT ? OFFSET ?",
            limit, offset
            );
    }
    
    @Override
    protected DB<PreparedStatement> prepareInsert(User user) {
        final StrongPasswordEncryptor spe = new StrongPasswordEncryptor();
        val encryptedPassword = spe.encryptPassword(user.getPassword());
        
        return sql(
                "INSERT INTO users (user_name, user_organization, user_username, user_pass) VALUES (?, ?, ?, ?)",
                user.getName(),user.getOrganization(),user.getUserName(),encryptedPassword
            );
    }
    
    @Override
    protected DB<PreparedStatement> prepareDelete(Identifier id) {
        throw new UnsupportedOperationException("Not valid operation");
    }
    
    private String getPassword(User user){
        val sql = prepareSelect(user).bind(query).bind(getWith(this::parseWithPass));
        Option<User> result = read(sql).toOption();
        return result.some().getPassword();
    }
    
    @Override 
    public boolean exists (User user) throws DAOException {
        val sql = prepareSelect(user).bind(query).bind(get);
        final Option<User> result = read(sql).toOption();
        return result.isSome();      
    }

    @Override
    public boolean login(User user) throws DAOException {
        final StrongPasswordEncryptor spe = new StrongPasswordEncryptor();
        return spe.checkPassword(user.getPassword(), getPassword(user));
    }

    @Override
    public boolean register(User user) throws DAOException {
        try{
            write(getOrInsert(user));      
        }catch(Exception e){
            return false;
        }
        return true;
    }
   
}
