package es.uvigo.ei.sing.mahmi.controller.controllers;

import java.util.Set;

import javax.ws.rs.core.GenericType;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.utils.wrappers.CutProteinsWrapper;
import es.uvigo.ei.sing.mahmi.controller.Configuration;

public final class CutterServiceController extends RemoteServiceAbstractController {

    private CutterServiceController(final Configuration config) {
        super(config);
    }

    public static CutterServiceController cutterServiceController(final Configuration config) {
        return new CutterServiceController(config);
    }

    @SuppressWarnings("unchecked")
    public Set<Digestion> cut(final CutProteinsWrapper toDigest) {
        val type = new GenericType<Set<Digestion>>() { };
        return (Set<Digestion>) post(getTargetFor("cutter"), toDigest, type.getRawType());
    }

}
