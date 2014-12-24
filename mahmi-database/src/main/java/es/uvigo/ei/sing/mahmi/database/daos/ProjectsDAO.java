package es.uvigo.ei.sing.mahmi.database.daos;

import java.util.Collection;

import es.uvigo.ei.sing.mahmi.common.entities.Project;

public interface ProjectsDAO extends DAO<Project> {

    public Collection<Project> getByName(
        final String name, final int start, final int count
    ) throws DAOException;

    public Collection<Project> getByRepository(
        final String repository, final int start, final int count
    ) throws DAOException;

}
