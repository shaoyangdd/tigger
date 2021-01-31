package org.tiger.common.datastruct;

/**
 * 组装bean参数
 *
 * @author 康绍飞
 * @date 2021-01-28
 */
public class AutowireBeanParameter {

    /**
     * 要扫的包
     */
    private String packageName;

    /**
     * 排除的类名,包含包名，如： org.tigger.common.datastruct.AutowireBeanParameter
     */
    private String[] excludeClassName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String[] getExcludeClassName() {
        return excludeClassName;
    }

    public void setExcludeClassName(String[] excludeClassName) {
        this.excludeClassName = excludeClassName;
    }
}
