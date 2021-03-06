@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(type = Identifier.class        , value = IdentifierAdapter.class),
    @XmlJavaTypeAdapter(type = AminoAcid.class         , value = AminoAcidAdapter.class),
    @XmlJavaTypeAdapter(type = Nucleobase.class        , value = NucleobaseAdapter.class),
    @XmlJavaTypeAdapter(type = AminoAcidSequence.class , value = AminoAcidSequenceAdapter.class),
    @XmlJavaTypeAdapter(type = NucleobaseSequence.class       , value = NucleobaseSequenceAdapter.class),
})
package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.AminoAcid;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.Nucleobase;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;
import es.uvigo.ei.sing.mahmi.common.serializers.jaxb.AminoAcidAdapter;
import es.uvigo.ei.sing.mahmi.common.serializers.jaxb.AminoAcidSequenceAdapter;
import es.uvigo.ei.sing.mahmi.common.serializers.jaxb.IdentifierAdapter;
import es.uvigo.ei.sing.mahmi.common.serializers.jaxb.NucleobaseAdapter;
import es.uvigo.ei.sing.mahmi.common.serializers.jaxb.NucleobaseSequenceAdapter;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;

