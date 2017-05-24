package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.count;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.identifier;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.integer;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseAASequence;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseIdentifier;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseString;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.prepare;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.query;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.sql;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.string;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.ProteinsDAO;
import fj.control.db.DB;
import fj.data.Set;
import lombok.val;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(IterableExtensionMethods.class)
public final class MySQLProteinsDAO extends MySQLAbstractDAO<Protein> implements ProteinsDAO {

    private MySQLProteinsDAO(final ConnectionPool connectionPool) {
        super(connectionPool);
    }

    public static ProteinsDAO mysqlProteinsDAO(
        final ConnectionPool connectionPool
    ) {
        return new MySQLProteinsDAO(connectionPool);
    }

    @Override
    public long countByMetaGenome(final MetaGenome mg) {
        val sql = sql(
            "SELECT COUNT(protein_id) FROM metagenome_proteins WHERE metagenome_id = ?",
            mg.getId()
        );

        val statement = sql.bind(query).bind(count);
        return read(statement);
    }

    @Override
    public Set<Protein> getByMetaGenome(
        final MetaGenome mg, final int start, final int count
    ) throws DAOException {
        val sql = sql(
            "SELECT protein_id, protein_sequence "            +
            "FROM proteins NATURAL JOIN metagenome_proteins " +
            "WHERE metagenome_id = ? "                        +
            "ORDER BY protein_id LIMIT ? OFFSET ?",
            mg.getId()
        ).bind(integer(2, count)).bind(integer(3, start));

        val statement = sql.bind(query).bind(get);
        return read(statement).toSet(ordering);
    }

    @Override
    protected Protein parse(final ResultSet results) throws SQLException {
    	return protein(parseIdentifier(results, "protein_id"),
			           parseAASequence(results, "protein_sequence"),
			           parseString(results, "protein_name"));
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final Protein protein) {
        return sql(
            "SELECT protein_id, protein_sequence FROM proteins WHERE protein_hash = ? LIMIT 1",
            protein.getSHA1().asHexString()
        );
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final Identifier id) {
        return sql(
            "SELECT protein_id, protein_sequence FROM proteins WHERE protein_id = ? LIMIT 1",
            id
        );
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(
        final int limit, final int offset
    ) {
        return sql(
            "SELECT protein_id, protein_sequence FROM proteins ORDER BY protein_id LIMIT ? OFFSET ?",
            limit, offset
        );
    }

    @Override
    public DB<PreparedStatement> prepareCount() {
        return prepare("SELECT COUNT(protein_hash) FROM proteins");
    }

    @Override
    protected DB<PreparedStatement> prepareInsert(final Protein protein) {
        return sql(
            "INSERT INTO proteins (protein_hash, protein_sequence) VALUES (?, ?)",
            protein.getSHA1().asHexString(), protein.getSequence().asString()
        );
    }

    @Override
    protected DB<PreparedStatement> prepareDelete(final Identifier id) {
        return sql("DELETE FROM proteins WHERE protein_id = ?", id);
    }

    @Override
    protected DB<PreparedStatement> prepareUpdate(final Protein protein) {
      return sql(
          "UPDATE proteins SET protein_hash = ?, protein_sequence = ?, protein_name = ? WHERE protein_id = ?",
          protein.getSHA1().asHexString(), protein.getSequence().asString()
      ).bind(string(3, protein.getName())).bind(identifier(4, protein.getId()));
    }
    
    @Override
    public Set<Protein> getByPeptide(final Peptide peptide){
    	val sql = sql(
            "SELECT protein_id, protein_sequence, protein_name FROM digestions NATURAL JOIN "
            + "proteins WHERE peptide_id = ?",
            peptide.getId()
        );
        val statement = sql.bind(query).bind(get);
        return read(statement).toSet(ordering);
	}

}
