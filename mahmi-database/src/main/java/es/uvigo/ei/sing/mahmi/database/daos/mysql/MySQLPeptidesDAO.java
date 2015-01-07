package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.Peptide.peptide;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;
import fj.control.db.DB;
import fj.data.Option;

public final class MySQLPeptidesDAO extends MySQLAbstractDAO<Peptide> implements PeptidesDAO {

    private MySQLPeptidesDAO(final ConnectionPool connectionPool) {
        super(connectionPool);
    }

    public static PeptidesDAO mysqlPeptidesDAO(
        final ConnectionPool connectionPool
    ) {
        return new MySQLPeptidesDAO(connectionPool);
    }


    @Override
    public Option<Peptide> getBySequence(
        final AminoAcidSequence sequence
    ) throws DAOException {
        val sql = prepareSelect(peptide(sequence)).bind(query).bind(get);
        return read(sql).toOption();
    }

    @Override
    public Collection<Peptide> getByProtein(
        final Protein protein, final int start, final int count
    ) throws DAOException {
        val sql = sql(
            "SELECT peptide_id, peptide_sequence "   +
            "FROM peptides NATURAL JOIN digestions " +
            "WHERE protein_id = ?"                   +
            "ORDER BY peptide_id LIMIT ? OFFSET ?",
            protein.getId()
        ).bind(integer(2, count)).bind(integer(3, start));

        val statement = sql.bind(query).bind(get);
        return read(statement).toCollection();
    }

    @Override
    protected Peptide parse(final ResultSet results) throws SQLException {
        val id  = parseIdentifier(results, "peptide_id");
        val seq = parseAASequence(results, "peptide_sequence");

        return peptide(id, seq);
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final Peptide peptide) {
        val sha = peptide.getSHA1().asHexString();
        return sql(
            "SELECT peptide_id, peptide_sequence FROM peptides WHERE peptide_hash = ? LIMIT 1",
            sha
        );
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final Identifier id) {
        return sql(
            "SELECT peptide_id, peptide_sequence FROM peptides WHERE peptide_id = ? LIMIT 1",
            id
        );
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(
        final int limit, final int offset
    ) {
        return sql(
            "SELECT peptide_id, peptide_sequence FROM peptides ORDER BY peptide_id LIMIT ? OFFSET ?",
            limit, offset
        );
    }
    
    @Override
    public DB<PreparedStatement> prepareCount() {
    	return sql(
                "SELECT COUNT(*) AS count FROM peptides LIMIT ",1);
    }

    @Override
    protected DB<PreparedStatement> prepareInsert(final Peptide peptide) {
        val seq = peptide.getSequence().toString();
        val sha = peptide.getSHA1().asHexString();

        return sql(
            "INSERT INTO peptides (peptide_hash, peptide_sequence) VALUES (?, ?)",
            sha, seq
        );
    }

    @Override
    protected DB<PreparedStatement> prepareDelete(final Identifier id) {
        return sql("DELETE FROM peptides WHERE peptide_id = ?", id);
    }

    @Override
    protected DB<PreparedStatement> prepareUpdate(final Peptide peptide) {
      val id  = peptide.getId();
      val seq = peptide.getSequence().toString();
      val sha = peptide.getSHA1().asHexString();

      return sql(
          "UPDATE peptides SET peptide_hash = ?, peptide_sequence = ? WHERE peptide_id = ?",
          sha, seq
      ).bind(identifier(3, id));
    }

}
