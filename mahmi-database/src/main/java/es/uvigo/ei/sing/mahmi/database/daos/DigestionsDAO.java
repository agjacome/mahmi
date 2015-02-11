package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import fj.data.Option;
import fj.data.Set;

public interface DigestionsDAO extends DAO<Digestion> {

    public Option<Digestion> get(
        final Enzyme enzyme, final Protein protein, final Peptide peptide
    ) throws DAOException;

    public Set<Digestion> getByEnzyme(
        final Enzyme enzyme, final int start, final int count
    ) throws DAOException;

    public Set<Digestion> getByProtein(
        final Protein protein, final int start, final int count
    ) throws DAOException;

    public Set<Digestion> getByPeptide(
        final Peptide peptide, final int start, final int count
    ) throws DAOException;

    public Set<Digestion> search(
        final Protein    protein,
        final MetaGenome metagenome,
        final Peptide    peptide,
        final Enzyme     enzyme,
        final int        start,
        final int        count
    ) throws DAOException;

}
