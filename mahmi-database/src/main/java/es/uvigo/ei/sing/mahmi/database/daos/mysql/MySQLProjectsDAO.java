package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.identifier;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseIdentifier;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseString;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.prepare;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.ProjectsDAO;
import fj.control.db.DB;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(IterableExtensionMethods.class)
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
    protected Project parse(final ResultSet results) throws SQLException {
    	return project(parseIdentifier(results, "project_id"),
			           parseString(results, "project_name"),
			           parseString(results, "project_repository"));
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final Project project) {
        return sql(
            "SELECT * FROM projects WHERE project_name = ? AND project_repository = ? LIMIT 1",
            project.getName(), project.getRepository()
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
        return sql(
            "INSERT INTO projects (project_name, project_repository) VALUES (?, ?)",
            project.getName(), project.getRepository()
        );
    }

    @Override
    protected DB<PreparedStatement> prepareDelete(final Identifier id) {
        return sql("DELETE FROM projects WHERE project_id = ?", id);
    }

    @Override
    protected DB<PreparedStatement> prepareUpdate(final Project project) {
        return sql(
            "UPDATE projects SET project_name = ?, project_repository = ? WHRE project_id = ?",
            project.getName(), project.getRepository()
        ).bind(identifier(3, project.getId()));
    }

}
