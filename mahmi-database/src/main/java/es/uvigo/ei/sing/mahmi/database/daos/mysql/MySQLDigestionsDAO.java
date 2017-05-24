package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.Digestion.digestion;
import static es.uvigo.ei.sing.mahmi.common.entities.Enzyme.enzyme;
import static es.uvigo.ei.sing.mahmi.common.entities.Peptide.peptide;
import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.identifier;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.longInt;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseAASequence;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseIdentifier;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseLong;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseString;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.prepare;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DigestionsDAO;
import fj.control.db.DB;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(IterableExtensionMethods.class)
public final class MySQLDigestionsDAO extends MySQLAbstractDAO<Digestion> implements DigestionsDAO {

    private static final String TABLES = "digestions NATURAL JOIN peptides NATURAL JOIN proteins NATURAL JOIN enzymes";

    private MySQLDigestionsDAO(final ConnectionPool connectionPool) {
        super(connectionPool);
    }

    public static DigestionsDAO mysqlDigestionsDAO(final ConnectionPool pool) {
        return new MySQLDigestionsDAO(pool);
    }

    @Override
    protected Digestion parse(final ResultSet results) throws SQLException {
    	return digestion(parseIdentifier(results, "digestion_id"),
        				 parseProtein(results),
        				 parsePeptide(results),
        				 parseEnzyme(results),
        				 parseLong(results, "counter"));
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final Digestion digestion) {
        return sql(
            "SELECT * FROM " + TABLES + " WHERE protein_id = ? AND peptide_id = ? AND enzyme_id = ? LIMIT 1",
            digestion.getProtein().getId(), digestion.getPeptide().getId(), digestion.getEnzyme().getId()
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
        return prepare("SELECT COUNT(digestion_id) FROM digestions");
    }

    @Override
    protected DB<PreparedStatement> prepareInsert(final Digestion digestion) {
        return sql(
            "INSERT INTO digestions (protein_id, peptide_id, enzyme_id, counter) VALUES (?, ?, ?, ?)",
            digestion.getProtein().getId(), digestion.getPeptide().getId(), digestion.getEnzyme().getId()
        ).bind(longInt(4, digestion.getCounter()));
    }

    @Override
    protected DB<PreparedStatement> prepareDelete(final Identifier id) {
        return sql("DELETE FROM digestions WHERE digestion_id = ?", id);
    }

    @Override
    protected DB<PreparedStatement> prepareUpdate(final Digestion digestion) {
        return prepare(
            "UPDATE digestions SET counter = ? WHERE digestion_id = ?"
        ).bind(longInt(1, digestion.getCounter())).bind(identifier(2, digestion.getId()));
    }


    private Enzyme parseEnzyme(final ResultSet results) throws SQLException {
        return enzyme(parseIdentifier(results, "enzyme_id"),
        			  parseString(results, "enzyme_name"));
    }

    private Peptide parsePeptide(final ResultSet results) throws SQLException {
        return peptide(parseIdentifier(results, "peptide_id"),
        			   parseAASequence(results, "peptide_sequence"));
    }

    private Protein parseProtein(final ResultSet results) throws SQLException {
        return protein(parseIdentifier(results, "protein_id"),
        			   parseAASequence(results, "protein_sequence"),
        			   parseString(results, "protein_name"));
    }
}
