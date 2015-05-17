package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleotideSequence;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaParser;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaPrinter;

public class NucleotideFastaAdapter extends XmlAdapter<String, Fasta<NucleotideSequence>> {

    @Override
    public Fasta<NucleotideSequence> unmarshal(final String str) throws Exception {
        return FastaParser.forNucleotideSequences().parseString(str);
    }

    @Override
    public String marshal(final Fasta<NucleotideSequence> fasta) throws Exception {
        return FastaPrinter.forNucleotideSequences().printString(fasta);
    }

}
