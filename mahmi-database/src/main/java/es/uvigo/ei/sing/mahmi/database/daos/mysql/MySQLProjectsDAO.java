package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.ProjectsDAO;
import fj.control.db.DB;

public final class MySQLProjectsDAO extends MySQLAbstractDAO<Project> implements ProjectsDAO {

    private MySQLProjectsDAO(final ConnectionPool connectionPool) {
        super(connectionPool);
    }

    public static ProjectsDAO mysqlProjectsDAO(
        final ConnectionPool connectionPool
    ) {
        return new MySQLProjectsDAO(connectionPool);
    }


    @Override
    public Collection<Project> getByName(
        final String name, final int start, final int count
    ) throws DAOException {
        val sql = sql(
            "SELECT * FROM projects WHERE project_name = ? ORDER BY project_id LIMIT ? OFFSET ?",
            name
        ).bind(integer(2, count)).bind(integer(3, start));

        val statement = sql.bind(query).bind(get);
        return read(statement).toCollection();
    }

    @Override
    public Collection<Project> getByRepository(
        final String repo, final int start, final int count
    ) throws DAOException {
        val sql = sql(
            "SELECT * FROM projects WHERE project_repository = ? ORDER BY project_id LIMIT ? OFFSET ?",
            repo
        ).bind(integer(2, count)).bind(integer(3, start));

        val statement = sql.bind(query).bind(get);
        return read(statement).toCollection();
    }

    @Override
    protected Project parse(final ResultSet results) throws SQLException {
        val id   = parseIdentifier(results, "project_id");
        val name = parseString(results, "project_name");
        val repo = parseString(results, "project_repository");

        return project(id, name, repo);
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final Project project) {
        val name = project.getName();
        val repo = project.getRepository();

        return sql(
            "SELECT * FROM projects WHERE project_name = ? AND project_repository = ? LIMIT 1",
            name, repo
        );
    }

    @Override
    public DB<PreparedStatement> prepareCount() {
    	return prepare("SELECT COUNT(project_id) FROM projects");
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final Identifier id) {
        return sql("SELECT * FROM projects WHERE project_id = ? LIMIT 1", id);
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(
        final int limit, final int offset
    ) {
        return sql(
            "SELECT * FROM projects ORDER BY project_id LIMIT ? OFFSET ?",
            limit, offset
        );
    }

    @Override
    protected DB<PreparedStatement> prepareInsert(final Project project) {
        val name = project.getName();
        val repo = project.getRepository();

        return sql(
            "INSERT INTO projects (project_name, project_repository) VALUES (?, ?)",
            name, repo
        );
    }

    @Override
    protected DB<PreparedStatement> prepareDelete(final Identifier id) {
        return sql("DELETE FROM projects WHERE project_id = ?", id);
    }

    @Override
    protected DB<PreparedStatement> prepareUpdate(final Project project) {
        val id   = project.getId();
        val name = project.getName();
        val repo = project.getRepository();

        return sql(
            "UPDATE projects SET project_name = ?, project_repository = ? WHRE project_id = ?",
            name, repo
        ).bind(identifier(3, id));
    }

}
