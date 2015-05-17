package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Compound;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.CompoundSequence;

import static es.uvigo.ei.sing.mahmi.common.utils.exceptions.PendingImplementationException.notYetImplemented;

public final class FastaParser<A extends CompoundSequence<? extends Compound>> {

    // FIXME: code removed because deletion of lombok and functionaljava, easier
    // to reimplement from scratch than adapt all that previous bullshit

    // TODO: implement by using jparsec
    public FastaParser() {
        throw notYetImplemented;
    }

}
