package org.tigger.persistence.file;

@FunctionalInterface
public interface FindCondition {

    boolean find(String line, Record record);

}
