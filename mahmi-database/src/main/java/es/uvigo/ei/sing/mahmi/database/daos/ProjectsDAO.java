package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.Project;
import fj.data.Set;

public interface ProjectsDAO extends DAO<Project> {

    public Set<Project> search(
        final Project project, final int start, final int count
    ) throws DAOException;

}
