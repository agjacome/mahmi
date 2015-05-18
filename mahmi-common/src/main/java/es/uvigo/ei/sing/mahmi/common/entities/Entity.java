package es.uvigo.ei.sing.mahmi.common.entities;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.Tuple;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Collections.sort;
import static java.util.Collections.unmodifiableList;

import static es.uvigo.ei.sing.mahmi.common.utils.Contracts.requireNonNull;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.ArrayUtils.asList;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableUtils.zip;

public abstract class Entity {

    private static final ConcurrentMap<Class<?>, Field[ ]> entityFieldMap = new ConcurrentHashMap<>();

    private static Field[ ] getEntityFields(final Class<?> type) {
        Field[ ] fields = entityFieldMap.get(type);
        if (fields == null) {
            fields = introspectEntityFields(type);
            entityFieldMap.put(type, fields);
        }

        return fields;
    }

    private static Field[ ] introspectEntityFields(final Class<?> type) {
        if (type == Entity.class) return new Field[0];

        final List<Field> fieldList = new ArrayList<>();
        fieldList.addAll(asList(introspectEntityFields(type.getSuperclass())));

        final List<Field> myFields = fieldList.subList(fieldList.size(), fieldList.size());
        for (final Field field : type.getDeclaredFields()) {
            if (!isStatic(field.getModifiers()) && isFinal(field.getModifiers()))
                myFields.add(field);
        }

        sort(myFields, (f1, f2) -> f1.getName().compareTo(f2.getName()));

        final Field[ ] fields = fieldList.toArray(new Field[fieldList.size()]);
        AccessibleObject.setAccessible(fields, true);

        return fields;
    }

    private static List<Object> toValueList(final Object obj, final Field[ ] fields) {
        final List<Object> list = new ArrayList<>();

        try {
            for (final Field field : fields) list.add(field.get(obj));
        } catch (final IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }

        return list;
    }

    // ---------------------------

    protected final Identifier id;

    private volatile List<Object> fieldValues = null;

    protected Entity(final Identifier id) {
        this.id = requireNonNull(id, "Entity ID cannot be NULL");;
    }

    public Identifier getId() {
        return id;
    }

    private List<Object> valueList() {
        if (fieldValues == null)
            fieldValues = unmodifiableList(toValueList(this, getEntityFields(getClass())));

        return fieldValues;
    }

    @Override
    public int hashCode() {
        return valueList().hashCode();
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (that == null) return false;

        return getClass() == that.getClass()
            && valueList().equals(((Entity) that).valueList());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append("(").append("id = ").append(id);

        final Iterable<Tuple<Field, Object>> fieldValues = zip(
            asList(getEntityFields(getClass())), valueList()
        );

        for (final Tuple<Field, Object> fieldValue : fieldValues) {
            sb.append(", ")
              .append(fieldValue.left.getName())
              .append(" = ")
              .append(fieldValue.right.toString());
        }

        sb.append(")");
        return sb.toString();
    }

}
