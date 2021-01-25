package org.tigger.persistence.file.field;

public interface FieldToStringPolicy<T> {

    String toString(T t);

}
