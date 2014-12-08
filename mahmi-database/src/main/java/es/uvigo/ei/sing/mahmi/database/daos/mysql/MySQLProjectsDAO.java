package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;
import static jersey.repackaged.com.google.common.collect.Sets.newLinkedHashSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Set;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.ProjectsDAO;
import fj.control.db.DB;
import fj.data.Option;

public final class MySQLProjectsDAO extends MySQLAbstractDAO<Project> implements ProjectsDAO {

    private MySQLProjectsDAO(final ConnectionPool pool) {
        super(pool);
    }

    public static MySQLProjectsDAO mysqlProjectsDAO(final ConnectionPool pool) {
        return new MySQLProjectsDAO(pool);
    }


    @Override
    public Option<Project> get(final Identifier id) throws DAOException {
        val sql = sql("SELECT * FROM projects WHERE project_id = ? LIMIT 1", id)
                 .bind(query).bind(get);

        return read(sql).toOption();
    }

    @Override
    public Set<Project> getAll(final int start, final int count) throws DAOException {
        val sql = sql("SELECT * FROM projects LIMIT ? OFFSET ?", count, start)
                 .bind(query).bind(get);

        return newLinkedHashSet(read(sql).toCollection());
    }

    @Override
    public Project insert(final Project project) throws DAOException {
        val sql = prepareInsert(project);

        return write(sql);
    }

    @Override
    public Set<Project> insertAll(final Set<Project> projects) throws DAOException {
        val sqlBuffer = new LinkedList<DB<Project>>();
        for (val project : projects) {
            sqlBuffer.add(prepareInsert(project));
        }

        return newLinkedHashSet(write(sequence(sqlBuffer)));
    }

    @Override
    public void delete(final Identifier id) throws DAOException {
        val sql = sql("DELETE FROM projects WHERE project_id = ?", id).bind(update);

        write(sql);
    }

    @Override
    public void update(final Project project) throws DAOException {
        val name = project.getName();
        val repo = project.getRepository();

        val sql = sql("UPDATE projects SET project_name = ?, project_repository = ? WHERE project_id = ?", name, repo)
                 .bind(identifier(3, project.getId()))
                 .bind(update);

        write(sql);
    }

    @Override
    protected Project createEntity(final ResultSet results) throws SQLException {
        val id   = parseInt(results, "project_id");
        val name = parseString(results, "project_name");
        val repo = parseString(results, "project_repository");

        return project(id, name, repo);
    }


    private DB<Project> prepareInsert(final Project project) throws DAOException {
        val name = project.getName();
        val repo = project.getRepository();

        return sql("INSERT INTO projects (project_name, project_repository) VALUES (?, ?)", name, repo)
               .bind(update).bind(getKey).map(project::withId);
    }

}
