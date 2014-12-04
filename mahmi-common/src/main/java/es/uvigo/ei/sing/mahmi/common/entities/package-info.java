@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(type = Identifier.class        , value = IdentifierAdapter.class),
    @XmlJavaTypeAdapter(type = AminoAcid.class         , value = AminoAcidAdapter.class),
    @XmlJavaTypeAdapter(type = Nucleobase.class        , value = NucleobaseAdapter.class),
    @XmlJavaTypeAdapter(type = AminoAcidSequence.class , value = AminoAcidSequenceAdapter.class),
    @XmlJavaTypeAdapter(type = DNASequence.class       , value = DNASequenceAdapter.class),
    @XmlJavaTypeAdapter(type = GenomeFasta.class       , value = GenomeFastaAdapter.class),
    @XmlJavaTypeAdapter(type = ProteinFasta.class      , value = ProteinFastaAdapter.class),
})
package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.AminoAcid;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.Nucleobase;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.GenomeFasta;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.ProteinFasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.DNASequence;
import es.uvigo.ei.sing.mahmi.common.serializers.jaxb.IdentifierAdapter;
import es.uvigo.ei.sing.mahmi.common.serializers.jaxb.entities.compounds.AminoAcidAdapter;
import es.uvigo.ei.sing.mahmi.common.serializers.jaxb.entities.compounds.NucleobaseAdapter;
import es.uvigo.ei.sing.mahmi.common.serializers.jaxb.entities.fasta.GenomeFastaAdapter;
import es.uvigo.ei.sing.mahmi.common.serializers.jaxb.entities.fasta.ProteinFastaAdapter;
import es.uvigo.ei.sing.mahmi.common.serializers.jaxb.entities.sequences.AminoAcidSequenceAdapter;
import es.uvigo.ei.sing.mahmi.common.serializers.jaxb.entities.sequences.DNASequenceAdapter;

