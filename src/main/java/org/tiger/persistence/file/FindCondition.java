package org.tiger.persistence.file;

@FunctionalInterface
public interface FindCondition {

    boolean find(String line, Record record);

}
