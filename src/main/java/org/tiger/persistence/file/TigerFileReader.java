package org.tiger.persistence.file;

import org.tiger.common.ioc.InjectByType;
import org.tiger.common.ioc.SingletonBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件读取
 *
 * @author 康绍飞
 * @date 2021-01-30
 */
@SingletonBean
public class TigerFileReader {

    @InjectByType
    private FilePathResolver filePathResolver;

    /**
     * 读取第一行
     *
     * @param file 文件
     * @return 第一行
     */
    public String readOneLine(File file) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            return bufferedReader.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读文件并且查找一行
     *
     * @param record        要查找的行
     * @param findCondition 匹配条件
     * @return 满足条件的行
     */
    public String readAndFind(Record record, FindCondition findCondition) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePathResolver.getFile(record)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (findCondition.find(line)) {
                    return line;
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读文件并且查找一行
     *
     * @param record        要查找的行条件
     * @param findCondition 匹配条件
     * @return
     */
    public List<String> readAndFindList(Record record, FindConditionMapParam findCondition) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePathResolver.getFile(record)));
            String line;
            List<String> lines = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                if (findCondition.find(line, record.searchParam())) {
                    lines.add(line);
                }
            }
            return lines;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
