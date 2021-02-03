package org.tiger.common.parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 参数读取器，在BEAN加载之前，所以不能使用注入方法
 *
 * @author 康绍飞
 * @date 2021-02-03
 */
public class ParameterReaders {

    private List<ParameterReader> parameterReaderList = new ArrayList<>();

    public ParameterReaders() {
        super();
        parameterReaderList.add(new CallNetApiParameterReader());
        parameterReaderList.add(new DatabaseParameterReader());
        parameterReaderList.add(new EnvironmentParameterReader());
        parameterReaderList.add(new FileConfigParameterReader());
    }

    public void loadParameters() {
        Map<String, String> parameterMap = Parameters.getParameter();
        for (ParameterReader parameterReader : parameterReaderList) {
            parameterMap.putAll(parameterReader.read());
        }
    }
}
