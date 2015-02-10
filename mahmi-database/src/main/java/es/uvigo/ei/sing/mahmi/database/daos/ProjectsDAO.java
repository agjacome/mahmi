package es.uvigo.ei.sing.mahmi.database.daos;

import java.util.Collection;

import es.uvigo.ei.sing.mahmi.common.entities.Project;

public interface ProjectsDAO extends DAO<Project> {

    public Collection<Project> search(
        final Project project, final int start, final int count
    ) throws DAOException;

}
