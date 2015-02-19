package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.User;

public interface UsersDAO extends DAO<User>{    
    public boolean login(
            final User user
    ) throws DAOException;
    
    public boolean exists(
            final User user
    ) throws DAOException;
    
}