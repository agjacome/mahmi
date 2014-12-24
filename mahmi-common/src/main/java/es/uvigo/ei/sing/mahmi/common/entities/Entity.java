package es.uvigo.ei.sing.mahmi.common.entities;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;

public interface Entity<A extends Entity<?>> {

    public Identifier getId();

    public A setId(final Identifier id);

}
