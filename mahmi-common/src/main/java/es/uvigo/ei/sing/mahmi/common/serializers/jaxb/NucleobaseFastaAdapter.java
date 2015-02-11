package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaReader;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaWriter;

public class NucleobaseFastaAdapter extends XmlAdapter<String, Fasta<NucleobaseSequence>> {

    @Override
    public Fasta<NucleobaseSequence> unmarshal(final String str) throws Exception {
        return FastaReader.forNucleobase().fromReader(
            new BufferedReader(new StringReader(str))
        );
    }

    @Override
    public String marshal(final Fasta<NucleobaseSequence> fasta) throws Exception {
        try (final StringWriter writer = new StringWriter()) {
            FastaWriter.forNucleobase().toWriter(fasta, writer);
            return writer.toString();
        }
    }

}
