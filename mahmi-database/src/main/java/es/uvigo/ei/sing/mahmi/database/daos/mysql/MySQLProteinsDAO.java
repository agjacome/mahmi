package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;
import static jersey.repackaged.com.google.common.collect.Sets.newLinkedHashSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Set;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.ProteinsDAO;
import fj.control.db.DB;
import fj.data.List;
import fj.data.Option;

public final class MySQLProteinsDAO extends MySQLAbstractDAO<Protein> implements ProteinsDAO {

    private MySQLProteinsDAO(final ConnectionPool pool) {
        super(pool);
    }

    public static ProteinsDAO mysqlProteinsDAO(final ConnectionPool pool) {
        return new MySQLProteinsDAO(pool);
    }


    @Override
    public Option<Protein> get(final Identifier id) throws DAOException {
        val sql = sql("SELECT * FROM proteins WHERE protein_id = ? LIMIT 1", id)
                 .bind(query).bind(get);

        return read(sql).toOption();
    }

    @Override
    public Option<Protein> getBySequence(final AminoAcidSequence sequence) throws DAOException {
        val sha = sequence.toSHA1().asHexString();
        val sql = sql("SELECT * FROM proteins WHERE protein_hash = ? LIMIT 1", sha)
                 .bind(query).bind(get);

        return read(sql).toOption();
    }

    @Override
    public Set<Protein> getAll(final int start, final int count) throws DAOException {
        val sql = sql("SELECT * FROM proteins LIMIT ? OFFSET ?", count, start)
                 .bind(query).bind(get);

        return newLinkedHashSet(read(sql).toCollection());
    }

    @Override
    public Protein insert(final Protein protein) throws DAOException {
        val sql = getOrPrepareInsert(protein);

        return write(sql);
    }

    @Override
    public Set<Protein> insertAll(final Set<Protein> proteins) throws DAOException {
        val sqlBuffer = new LinkedList<DB<Protein>>();
        for (val protein : proteins) {
            sqlBuffer.add(getOrPrepareInsert(protein));
        }

        return newLinkedHashSet(write(sequence(sqlBuffer)));
    }

    @Override
    public void delete(final Identifier id) throws DAOException {
        val sql = sql("DELETE FROM proteins WHERE protein_id = ?", id).bind(update);

        write(sql);
    }

    @Override
    public void update(final Protein protein) throws DAOException {
        val id  = protein.getId();
        val seq = protein.getSequence().toString();
        val sha = protein.getSequence().toSHA1().asHexString();

        val sql = sql("UPDATE proteins SET protein_hash = ?, protein_sequence = ? WHERE protein_id = ?", sha, seq)
                 .bind(identifier(3, id))
                 .bind(update);

        write(sql);
    }


    @Override
    protected Protein createEntity(final ResultSet results) throws SQLException {
        val id  = parseInt(results, "protein_id");
        val seq = parseAAS(results, "protein_sequence");

        return protein(id, seq);
    }


    private DB<Protein> getOrPrepareInsert(final Protein protein) throws DAOException {
        val seq = protein.getSequence();
        val sha = seq.toSHA1().asHexString();

        val exists = sql(
            "SELECT * FROM proteins WHERE protein_hash = ? LIMIT 1", sha
        ).bind(query).bind(get).map(List::toOption);

        val insert = sql(
            "INSERT INTO proteins (protein_hash, protein_sequence) VALUES (?, ?)",
            sha, seq.toString()
        ).bind(update).bind(getKey).map(protein::withId);

        return exists.bind(opt -> opt.map(DB::unit).orSome(insert));
    }

}
