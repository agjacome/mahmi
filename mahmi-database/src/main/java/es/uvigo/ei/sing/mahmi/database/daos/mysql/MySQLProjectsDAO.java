package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;
import static fj.Unit.unit;
import static fj.data.Stream.iterableStream;

import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.ProjectsDAO;
import fj.F;
import fj.F2;
import fj.Unit;
import fj.control.db.DB;
import fj.data.List;
import fj.data.Option;
import fj.data.Validation;

public final class MySQLProjectsDAO extends MySQLAbstractDAO<Project> implements ProjectsDAO {

    private final F<Integer, DB<Option<Project>>> getProject = id -> sql(
        "SELECT * FROM projects WHERE project_id = ?", id
    ).bind(query).bind(get).map(List::toOption);

    private final F2<Integer, Integer, DB<Iterable<Project>>> getProjects = (count, start) -> sql(
        "SELECT * FROM projects LIMIT ? OFFSET ?", count, start
    ).bind(query).bind(get).map(List::toCollection);

    private final F2<String, String, DB<Integer>> insertProject = (name, repo) -> sql(
        "INSERT INTO projects (project_name, project_repository) VALUES (?, ?)", name, repo
    ).bind(update).bind(getKey);

    private final F<Integer, DB<Unit>> deleteProject = id -> sql(
        "DELETE FROM projects WHERE project_id = ?", id
    ).bind(update).map(r -> unit());

    private final F<Integer, F2<String, String, DB<Unit>>> updateProject = id -> (name, repo) -> sql(
        "UPDATE projects SET project_name = ?, project_repository = ? WHERE project_id = ?", name, repo
    ).bind(integer(3, id)).bind(update).map(r -> unit());


    private MySQLProjectsDAO(final ConnectionPool pool) {
        super(pool, pool.getConnector());
    }

    public static MySQLProjectsDAO mysqlProjectsDAO(final ConnectionPool pool) {
        return new MySQLProjectsDAO(pool);
    }


    @Override
    public Validation<DAOException, Option<Project>> get(final Identifier id) {
        return withIdentifier(getProject, id).bind(this::read);
    }

    @Override
    public Validation<DAOException, Iterable<Project>> getAll(final int start, final int count) {
        return read(withPagination(getProjects, count, start));
    }

    @Override
    public Validation<DAOException, Project> insert(final Project project) {
        return write(withProject(insertProject, project)).map(project::withId);
    }

    @Override
    public Validation<DAOException, Iterable<Project>> insertAll(final Iterable<Project> projects) {
        val inserts = iterableStream(projects).map(
            p -> withProject(insertProject, p).map(p::withId)
        );

        return write(sequence(inserts));
    }

    @Override
    public Validation<DAOException, Unit> delete(final Identifier id) {
        return withIdentifier(deleteProject, id).bind(this::write);
    }

    @Override
    public Validation<DAOException, Unit> update(final Project project) {
        return withIdentifier(updateProject, project.getId()).map(
            projectF -> withProject(projectF, project)
        ).bind(this::write);
    }

    @Override
    protected Project createEntity(final ResultSet results) throws SQLException {
        return project(
            parseInt(results, "project_id"),
            parseString(results, "project_name"),
            parseString(results, "project_repository")
        );
    }

    private <A> A withProject(final F2<String, String, A> f, final Project project) {
        return f.f(project.getName(), project.getRepository());
    }

}
