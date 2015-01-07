package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.Digestion.digestion;
import static es.uvigo.ei.sing.mahmi.common.entities.Enzyme.enzyme;
import static es.uvigo.ei.sing.mahmi.common.entities.Peptide.peptide;
import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.DigestionsDAO;
import fj.control.db.DB;
import fj.data.Option;

public final class MySQLDigestionsDAO extends MySQLAbstractDAO<Digestion> implements DigestionsDAO {

    private static final String TABLES = "digestions NATURAL JOIN peptides NATURAL JOIN proteins NATURAL JOIN enzymes";

    private MySQLDigestionsDAO(final ConnectionPool connectionPool) {
        super(connectionPool);
    }

    public static DigestionsDAO mysqlDigestionsDAO(final ConnectionPool pool) {
        return new MySQLDigestionsDAO(pool);
    }


    @Override
    public Option<Digestion> get(
        final Enzyme enzyme, final Protein protein, final Peptide peptide
    ) throws DAOException {
        val digestion = digestion(protein, peptide, enzyme);
        val statement = prepareSelect(digestion).bind(query).bind(get);

        return read(statement).toOption();
    }


    @Override
    public Collection<Digestion> getByEnzyme(
        final Enzyme enzyme, final int start, final int count
    ) throws DAOException {
        return getByForeignIdentifier(
            "enzyme_id", enzyme.getId(), start, count
        );
    }

    @Override
    public Collection<Digestion> getByProtein(
        final Protein protein, final int start, final int count
    ) throws DAOException {
        return getByForeignIdentifier(
            "protein_id", protein.getId(), start, count
        );
    }

    @Override
    public Collection<Digestion> getByPeptide(
        final Peptide peptide, final int start, final int count
    ) throws DAOException {
        return getByForeignIdentifier(
            "peptide_id", peptide.getId(), start, count
        );
    }


    @Override
    protected Digestion parse(final ResultSet results) throws SQLException {
        val id      = parseIdentifier(results, "digestion_id");
        val protein = parseProtein(results);
        val peptide = parsePeptide(results);
        val enzyme  = parseEnzyme(results);
        val counter = parseLong(results, "counter");

        return digestion(id, protein, peptide, enzyme, counter);
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final Digestion digestion) {
        val proteinId = digestion.getProtein().getId();
        val peptideId = digestion.getPeptide().getId();
        val enzymeId  = digestion.getEnzyme().getId();

        return sql(
            "SELECT * FROM " + TABLES + " WHERE protein_id = ? AND peptide_id = ? AND enzyme_id = ? LIMIT 1",
            proteinId, peptideId, enzymeId
        );
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final Identifier id) {
        return sql(
            "SELECT * FROM " + TABLES + " WHERE digestion_id = ? LIMIT 1", id
        );
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final int limit, final int offset) {
        return sql(
            "SELECT * FROM " + TABLES + " ORDER BY digestion_id LIMIT ? OFFSET ?",
            limit, offset
        );
    }
        
    @Override
    public DB<PreparedStatement> prepareCount() {
    	return sql(
                "SELECT COUNT(*) AS count FROM digestions LIMIT ",1);
    }

    @Override
    protected DB<PreparedStatement> prepareInsert(final Digestion digestion) {
        val protein = digestion.getProtein().getId();
        val peptide = digestion.getPeptide().getId();
        val enzyme  = digestion.getEnzyme().getId();
        val counter = digestion.getCounter();

        return sql(
            "INSERT INTO digestions (protein_id, peptide_id, enzyme_id, counter) VALUES (?, ?, ?, ?)",
            protein, peptide, enzyme
        ).bind(longInt(4, counter));
    }

    @Override
    protected DB<PreparedStatement> prepareDelete(final Identifier id) {
        return sql("DELETE FROM digestions WHERE digestion_id = ?", id);
    }

    @Override
    protected DB<PreparedStatement> prepareUpdate(final Digestion entity) {
        val id      = entity.getId();
        val counter = entity.getCounter();

        return prepare(
            "UPDATE digestions SET counter = ? WHERE digestion_id = ?"
        ).bind(longInt(1, counter)).bind(identifier(2, id));
    }


    private Enzyme parseEnzyme(final ResultSet results) throws SQLException {
        return enzyme(
            parseIdentifier(results, "enzyme_id"),
            parseString(results, "enzyme_name")
        );
    }

    private Peptide parsePeptide(final ResultSet results) throws SQLException {
        return peptide(
            parseIdentifier(results, "peptide_id"),
            parseAASequence(results, "peptide_sequence")
        );
    }

    private Protein parseProtein(final ResultSet results) throws SQLException {
        return protein(
            parseIdentifier(results, "protein_id"),
            parseAASequence(results, "protein_sequence")
        );
    }

    private Collection<Digestion> getByForeignIdentifier(
        final String     columnName,
        final Identifier id,
        final int        start,
        final int        count
    ) throws DAOException {
        val sql = sql(
            "SELECT * FROM " + TABLES + " WHERE " + columnName + " = ? ORDER BY digestion_id LIMIT ? OFFSET ?",
            id
        ).bind(integer(2, count)).bind(integer(3, start));

        val statement = sql.bind(query).bind(get);

        return read(statement).toCollection();
    }

}
