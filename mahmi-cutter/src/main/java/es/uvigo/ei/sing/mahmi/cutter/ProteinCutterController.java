package es.uvigo.ei.sing.mahmi.cutter;

import java.util.concurrent.CompletableFuture;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;

import fj.F;
import fj.data.HashMap;
import fj.data.Set;
import fj.function.Try0;

import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.TableStat;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.DigestionsDAO;
import es.uvigo.ei.sing.mahmi.database.daos.MetaGenomesDAO;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;
import es.uvigo.ei.sing.mahmi.database.daos.ProteinsDAO;
import es.uvigo.ei.sing.mahmi.database.daos.TableStatsDAO;

import static java.util.concurrent.CompletableFuture.runAsync;

import static es.uvigo.ei.sing.mahmi.common.entities.TableStat.tableStat;

/**
 * {@linkplain ProteinCutterController} is the controller of
 * {@link ProteinCutter}
 * 
 * @author Alberto Gutierrez-Jacome
 * @author Aitor Blanco-Miguez
 *
 */
@Slf4j
@AllArgsConstructor(staticName = "proteinCutterCtrl")
@ExtensionMethod({ IterableExtensionMethods.class, OptionExtensionMethods.class })
public final class ProteinCutterController {

	/**
	 * The protein cutter
	 */
	private final ProteinCutter cutter;

	/**
	 * The {@link MetaGenome} DAO
	 */
	private final MetaGenomesDAO metaGenomesDAO;

	/**
	 * The {@link Protein} DAO
	 */
	private final ProteinsDAO proteinsDAO;

	/**
	 * The {@link Peptide} DAO
	 */
	private final PeptidesDAO peptidesDAO;

	/**
	 * The {@link Digestion} DAO
	 */
	private final DigestionsDAO digestionsDAO;

	/**
	 * The {@link TableStat} DAO
	 */
	private final TableStatsDAO tableStatsDAO;

	/**
	 * Digests the proteins with a set of enzymes and inserts the peptides and
	 * the digestions into the MAHMI database, filtering before by the peptide
	 * size. Moreover updates the peptide count in table stats table on MAHMI
	 * database
	 * 
	 * @param project
	 *            The {@link Project} entity
	 * @param enzymes
	 *            The {@code Set} of {@link Enzyme}s
	 * @param sizeFilter
	 *            The peptide size filter
	 * @return The {@linkplain CompletableFuture} of the file load process
	 * 
	 * @see Project
	 * @see Enzyme
	 */
	public CompletableFuture<Void> cutProjectProteins(	final Project project,
														final Set<Enzyme> enzymes,
														final F<Integer, Boolean> sizeFilter) {
		return runAsync(() -> {
			log.info("Cutting proteins of {} with {}", project, enzymes);

			metaGenomesDAO.forEachMetaGenomeOf(project,
					metagenome -> cutMetaGenomeProteins(metagenome, enzymes, sizeFilter));

			tableStatsDAO.update(tableStat(Identifier.of(4), "", peptidesDAO.count()));

			log.info("Finished cutting proteins of {}", project);
		});
	}

	/**
	 * Digest the proteins of a metagenome with a set of enzymes and insert the
	 * peptides and digestions into the MAHMI database, filtering before by the
	 * peptide size.
	 * 
	 * @param metagenome
	 *            The {@link MetaGenome} entity
	 * @param enzymes
	 *            The {@code Set} of {@link Enzyme}s
	 * @param sizeFilter
	 *            The size filter
	 * 
	 * @see MetaGenome
	 * @see Enzyme
	 */
	private void cutMetaGenomeProteins(	final MetaGenome metagenome,
										final Set<Enzyme> enzymes,
										final F<Integer, Boolean> sizeFilter) {
		log.info("Cutting proteins of {} with {}", metagenome, enzymes);

		proteinsDAO.forEachProteinOf(metagenome,
				proteins -> cutProteins(proteins, enzymes, sizeFilter));

		log.info("Finished cutting proteins of {}", metagenome);
	}

	/**
	 * Digest a set of proteins with a set of enzymes and insert the peptides
	 * and digestions into the MAHMI database, filtering before by the peptide
	 * size.
	 * 
	 * @param proteins
	 *            The {@code Set} of {@link Protein}s
	 * @param enzymes
	 *            The {@code Set} of {@link Enzyme}s
	 * @param sizeFilter
	 *            The size filter
	 * 
	 * @see Protein
	 * @see Enzyme
	 */
	private void cutProteins(	final Set<Protein> proteins,
								final Set<Enzyme> enzymes,
								final F<Integer, Boolean> sizeFilter) {
		log.info("Digesting {} proteins", proteins.size());

		final Set<Digestion> cuts = cutter.cutProteins(proteins, enzymes, sizeFilter);
		insertCuts(cuts);
	}

	/**
	 * Inserts a set of digestions and his related peptides into the MAHMI
	 * database
	 * 
	 * @param digestions
	 *            The {@code Set} of {@link Digestion}s
	 * 
	 * @see Digestion
	 */
	private void insertCuts(final Set<Digestion> digestions) {
		final Set<Peptide> peptides = digestions.map(Peptide.ord, d -> d.getPeptide());

		insertDigestions(digestions, insertPeptides(peptides));
	}

	/**
	 * Insert a set of peptides into the MAHMI database
	 * 
	 * @param peptides
	 *            The {@code Set} of {@link Peptide}s
	 * @return The inserted {@code Set} of peptides with {@link Identifier}
	 * 
	 * @see Peptide
	 */
	private Set<Peptide> insertPeptides(final Set<Peptide> peptides) {
		log.info("Inserting {} peptides in database", peptides.size());

		return databaseAction(() -> peptidesDAO.insertAll(peptides));
	}

	/**
	 * Insert a set of digestions into the MAHMI database
	 * 
	 * @param digestions
	 *            The {@code Set} of {@link Digestion}s
	 * @param peptides
	 *            The {@code Set} of {@link Peptides}s
	 * 
	 * @see Peptide
	 * @see Digestion
	 */
	private void insertDigestions(final Set<Digestion> digestions, final Set<Peptide> peptides) {
		val updated = updateReferences(digestions, peptides);

		log.info("Inserting {} digestions in database", digestions.size());
		databaseAction(() -> digestionsDAO.insertAll(updated));
	}

	/**
	 * Updates the set of digestions adding the identifiers of their peptides
	 * 
	 * @param digestions
	 *            The {@code Set} of {@link Digestion}s with {@link Peptide}s
	 *            without {@link Identifier}s
	 * @param peptides
	 *            The {@code Set} of {@link Peptide}s with {@link Identifier}s
	 * @return The {@code Set} of digestions {@link Peptide}s with
	 *         {@link Identifier}s
	 * 
	 * @see Peptide
	 * @see Digestion
	 */
	private Set<Digestion> updateReferences(final Set<Digestion> digestions,
											final Set<Peptide> peptides) {
		final HashMap<Peptide, Peptide> peptideMap = peptides.toIdentityMap(Peptide.equal,
				Peptide.hash);

		return digestions.map(Digestion.ord,
				d -> d.withPeptide(peptideMap.get(d.getPeptide()).some()));
	}

	/**
	 * Performs a DAO action
	 * 
	 * @param f
	 *            The DAO action
	 * @return The response of the action
	 */
	private <A> A databaseAction(final Try0<A, DAOException> f) {
		try {
			return f.f();
		} catch (final DAOException daoe) {
			throw CutterException.withCause(daoe);
		}
	}

}
