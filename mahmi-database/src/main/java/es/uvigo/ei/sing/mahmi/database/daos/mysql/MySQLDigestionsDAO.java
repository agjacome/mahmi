package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.Digestion.digestion;
import static es.uvigo.ei.sing.mahmi.common.entities.Enzyme.enzyme;
import static es.uvigo.ei.sing.mahmi.common.entities.Peptide.peptide;
import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;
import static jersey.repackaged.com.google.common.collect.Sets.newLinkedHashSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.DigestionsDAO;
import fj.control.db.DB;
import fj.data.List;
import fj.data.Option;

public class MySQLDigestionsDAO extends MySQLAbstractDAO<Digestion> implements DigestionsDAO {

    private static final String tables = "digestions NATURAL JOIN peptides NATURAL JOIN proteins NATURAL JOIN enzymes";


    private MySQLDigestionsDAO(final ConnectionPool pool) {
        super(pool);
    }

    public static DigestionsDAO mysqlDigestionsDAO(final ConnectionPool pool) {
        return new MySQLDigestionsDAO(pool);
    }


    @Override
    public Option<Digestion> get(final Identifier id) throws DAOException {
        val sql = sql("SELECT * FROM " + tables + " WHERE digestion_id = ? LIMIT 1", id)
                 .bind(query).bind(get);

        return read(sql).toOption();
    }

    @Override
    public Option<Digestion> get(final Identifier enzyme, final Identifier protein, final Identifier peptide) throws DAOException {
        val sql = sql(
            "SELECT * FROM " + tables + " WHERE enzyme_id = ? AND protein_id = ? AND peptide_id = ? LIMIT 1",
            enzyme, protein, peptide
        ).bind(query).bind(get);

        return read(sql).toOption();
    }

    @Override
    public Set<Digestion> getAll(final int start, final int count) throws DAOException {
        val sql = sql("SELECT * FROM " + tables + " LIMIT ? OFFSET ?", count, start)
                 .bind(query).bind(get);

        return new LinkedHashSet<>(read(sql).toCollection());
    }

    @Override
    public Set<Digestion> getByEnzymeId(final Identifier enzyme, final int start, final int count) throws DAOException {
        val sql = sql("SELECT * FROM " + tables + " WHERE enzyme_id = ? LIMIT ? OFFSET ?", enzyme)
                 .bind(integer(2, count))
                 .bind(integer(3, start))
                 .bind(query).bind(get);

        return new LinkedHashSet<>(read(sql).toCollection());
    }

    @Override
    public Set<Digestion> getByProteinId(final Identifier protein, final int start, final int count) throws DAOException {
        val sql = sql("SELECT * FROM " + tables + " WHERE protein_id = ? LIMIT ? OFFSET ?", protein)
                 .bind(integer(2, count))
                 .bind(integer(3, start))
                 .bind(query).bind(get);

        return new LinkedHashSet<>(read(sql).toCollection());
    }

    @Override
    public Set<Digestion> getByPeptideId(final Identifier peptide, final int start, final int count) throws DAOException {
        val sql = sql("SELECT * FROM " + tables + " WHERE peptide_id = ? LIMIT ? OFFSET ?", peptide)
                 .bind(integer(2, count))
                 .bind(integer(3, start))
                 .bind(query).bind(get);

        return new LinkedHashSet<>(read(sql).toCollection());
    }

    @Override
    public Digestion insert(final Digestion digestion) throws DAOException {
        val sql = getOrPrepareInsert(digestion);

        return write(sql);
    }

    @Override
    public Set<Digestion> insertAll(final Set<Digestion> digestions) throws DAOException {
        val sqlBuffer = new LinkedList<DB<Digestion>>();
        for (val digestion : digestions) {
            sqlBuffer.add(getOrPrepareInsert(digestion));
        }

        return newLinkedHashSet(write(sequence(sqlBuffer)));
    }

    @Override
    public void delete(final Identifier id) throws DAOException {
        val sql = sql("DELETE FROM digestions WHERE digestion_id = ?", id).bind(update);

        write(sql);
    }

    @Override
    public void update(final Digestion digestion) throws DAOException {
        val sql = prepare("UPDATE digestions SET counter = ? WHERE digestion_id = ?")
               .bind(longInt(1, digestion.getCounter()))
               .bind(identifier(2, digestion.getId()))
               .bind(update);

        write(sql);
    }

    @Override
    protected Digestion createEntity(final ResultSet results) throws SQLException {
        val id      = parseInt(results, "digestion_id");
        val protein = protein(parseInt(results, "protein_id"), parseAAS(results, "protein_sequence"));
        val peptide = peptide(parseInt(results, "peptide_id"), parseAAS(results, "peptide_sequence"));
        val enzyme  = enzyme(parseInt(results, "enzyme_id"), parseString(results, "enzyme_name"));
        val counter = parseLong(results, "digestion_counter");

        return digestion(id, protein, peptide, enzyme, counter);
    }


    private DB<Digestion> getOrPrepareInsert(final Digestion digestion) throws DAOException {
        val enzyme  = digestion.getEnzyme().getId();
        val protein = digestion.getProtein().getId();
        val peptide = digestion.getPeptide().getId();

        val exists = sql(
            "SELECT * FROM " + tables + " WHERE enzyme_id = ? AND protein_id = ? AND peptide_id = ? LIMIT 1",
            enzyme, protein, peptide
        ).bind(query).bind(get).map(List::toOption);

        val insert = sql(
            "INSERT INTO digestions (enzyme_id, protein_id, peptide_id, counter) VALUES (?, ?, ?, ?)",
            enzyme, protein, peptide
        ).bind(longInt(4, digestion.getCounter())).bind(update).bind(getKey).map(digestion::withId);

        return exists.bind(opt -> opt.map(DB::unit).orSome(insert));
    }

}
