package es.uvigo.ei.sing.mahmi.database.daos;

import fj.data.Set;

import es.uvigo.ei.sing.mahmi.common.entities.Project;

public interface ProjectsDAO extends DAO<Project> {

    public Set<Project> search(
        final Project project, final int start, final int count
    ) throws DAOException;

}
