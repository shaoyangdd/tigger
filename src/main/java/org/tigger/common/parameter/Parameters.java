package org.tigger.common.parameter;

import org.tigger.common.datastruct.AutowireBeanParameter;

public class Parameters {

    private static AutowireBeanParameter autowireBeanParameter;

    public static AutowireBeanParameter getAutowireBeanParameter() {
        return autowireBeanParameter;
    }
}
