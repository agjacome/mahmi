package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.Peptide.peptide;
import static jersey.repackaged.com.google.common.collect.Sets.newLinkedHashSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Set;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;
import fj.control.db.DB;
import fj.data.Option;

public final class MySQLPeptidesDAO extends MySQLAbstractDAO<Peptide> implements PeptidesDAO {

    private MySQLPeptidesDAO(final ConnectionPool pool) {
        super(pool);
    }

    public static PeptidesDAO mysqlPeptidesDAO(final ConnectionPool pool) {
        return new MySQLPeptidesDAO(pool);
    }


    @Override
    public Option<Peptide> get(final Identifier id) throws DAOException {
        val sql = sql("SELECT * FROM peptides WHERE peptide_id = ? LIMIT 1", id)
                 .bind(query).bind(get);

        return read(sql).toOption();
    }

    @Override
    public Option<Peptide> getBySequence(final AminoAcidSequence sequence) throws DAOException {
        val sha = sequence.toSHA1().asHexString();
        val sql = sql("SELECT * FROM peptides WHERE peptide_hash = ? LIMIT 1", sha)
                 .bind(query).bind(get);

        return read(sql).toOption();
    }

    @Override
    public Set<Peptide> getAll(final int start, final int count) throws DAOException {
        val sql = sql("SELECT * FROM peptides LIMIT ? OFFSET ?", count, start)
                 .bind(query).bind(get);

        return newLinkedHashSet(read(sql).toCollection());
    }

    @Override
    public Peptide insert(final Peptide peptide) throws DAOException {
        val sql = getOrPrepareInsert(peptide);

        return write(sql);
    }

    @Override
    public Set<Peptide> insertAll(final Set<Peptide> peptides) throws DAOException {
        val sqlBuffer = new LinkedList<DB<Peptide>>();
        for (val peptide : peptides) {
            sqlBuffer.add(getOrPrepareInsert(peptide));
        }

        return newLinkedHashSet(write(sequence(sqlBuffer)));
    }

    @Override
    public void delete(final Identifier id) throws DAOException {
        val sql = sql("DELETE FROM peptides WHERE peptide_id = ?", id).bind(update);

        write(sql);
    }

    @Override
    public void update(final Peptide peptide) throws DAOException {
        val id  = peptide.getId();
        val seq = peptide.getSequence().toString();
        val sha = peptide.getSequence().toSHA1().asHexString();

        val sql = sql("UPDATE peptides SET peptide_hash = ?, peptide_sequence = ? WHERE peptide_id = ?", sha, seq)
                 .bind(identifier(3, id))
                 .bind(update);

        write(sql);
    }


    @Override
    protected Peptide createEntity(final ResultSet results) throws SQLException {
        val id  = parseInt(results, "peptide_id");
        val seq = parseAAS(results, "peptide_sequence");

        return peptide(id, seq);
    }


    private DB<Peptide> getOrPrepareInsert(final Peptide peptide) throws DAOException {
        val seq = peptide.getSequence();
        val sha = seq.toSHA1().asHexString();

        val maybeInserted = getBySequence(seq).map(DB::unit);
        val prepareInsert = sql(
            "INSERT INTO peptides (peptide_hash, peptide_sequence) VALUES (?, ?)",
            sha, seq.toString()
        ).bind(update).bind(getKey).map(peptide::withId);

        return maybeInserted.orSome(prepareInsert);
    }

}
