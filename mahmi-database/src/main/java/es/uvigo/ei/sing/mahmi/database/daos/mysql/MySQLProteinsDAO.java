package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.val;
import lombok.experimental.ExtensionMethod;

import fj.control.db.DB;
import fj.data.Option;
import fj.data.Set;

import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.ProteinsDAO;

import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.*;

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
    public Option<Protein> getBySequence(
        final AminoAcidSequence sequence
    ) throws DAOException {
        val sql = prepareSelect(protein(sequence)).bind(query).bind(get);
        return read(sql).toOption();
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
    public Set<Protein> search(
            final MetaGenome metagenome,
            final AminoAcidSequence sequence,
            final int start,
            final int count
        ) throws DAOException {
        val sql = sql(
            "SELECT protein_id, protein_sequence " +
            "FROM proteins NATURAL JOIN metagenome_proteins NATURAL JOIN projects " +
            "WHERE (? = 0 OR metagenome_id = ?) AND " +
            "(? = 0 OR project_id = ?) AND " +
            "(? = '' OR protein_sequence = ?) AND " +
            "(? = '' OR project_name = ?) AND " +
            "(? = '' OR project_repository = ?) " +
            "ORDER BY protein_id LIMIT ? OFFSET ?",
            metagenome.getId(),  metagenome.getId(),
            metagenome.getProject().getId(), metagenome.getProject().getId()
        ).bind(string(5, sequence.asString())).bind(string(6, sequence.asString()))
        .bind(string(7, metagenome.getProject().getName())).bind(string(8, metagenome.getProject().getName()))
        .bind(string(9, metagenome.getProject().getRepository())).bind(string(10, metagenome.getProject().getRepository()))
        .bind(integer(11, count)).bind(integer(12, start));

        val statement = sql.bind(query).bind(get);
        return read(statement).toSet(ordering);
    }

    @Override
    protected Protein parse(final ResultSet results) throws SQLException {
        val id  = parseIdentifier(results, "protein_id");
        val seq = parseAASequence(results, "protein_sequence");

        return protein(id, seq);
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final Protein protein) {
        val sha = protein.getSHA1().asHexString();

        return sql(
            "SELECT protein_id, protein_sequence FROM proteins WHERE protein_hash = ? LIMIT 1",
            sha
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
        val sha = protein.getSHA1().asHexString();
        val seq = protein.getSequence().asString();

        return sql(
            "INSERT INTO proteins (protein_hash, protein_sequence) VALUES (?, ?)",
            sha, seq
        );
    }

    @Override
    protected DB<PreparedStatement> prepareDelete(final Identifier id) {
        return sql("DELETE FROM proteins WHERE protein_id = ?", id);
    }

    @Override
    protected DB<PreparedStatement> prepareUpdate(final Protein protein) {
      val id  = protein.getId();
      val seq = protein.getSequence().asString();
      val sha = protein.getSHA1().asHexString();

      return sql(
          "UPDATE proteins SET protein_hash = ?, protein_sequence = ? WHERE protein_id = ?",
          sha, seq
      ).bind(identifier(3, id));
    }

}
