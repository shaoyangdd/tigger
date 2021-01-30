package org.tigger.persistence.file;

import java.util.Map;

@FunctionalInterface
public interface FindConditionMapParam {

    boolean find(String line, Map<String, Object> paramMap);

}
