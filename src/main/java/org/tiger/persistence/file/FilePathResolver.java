package org.tiger.persistence.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.common.ioc.InjectParameter;
import org.tiger.common.ioc.SingletonBean;

import java.io.File;
import java.io.IOException;

/**
 * 文件路径解析
 *
 * @author 康绍飞
 * @date 2021-02-03
 */
@SingletonBean
public class FilePathResolver {

    private Logger logger = LoggerFactory.getLogger(FilePathResolver.class.getSimpleName());

    @InjectParameter
    private String dbFilePath;

    public File getFile(Record record) {
        File file = new File(dbFilePath + record.getClass().getSimpleName() + ".txt");
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (created && logger.isInfoEnabled()) {
                    logger.info("文件:{}创建成功", file.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new RuntimeException("文件创建失败:" + file.getAbsolutePath(), e);
            }
        }
        return file;
    }
}
