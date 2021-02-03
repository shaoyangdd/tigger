package org.tiger.file;

import org.junit.Before;
import org.junit.Test;
import org.tiger.common.ioc.BeanFactory;
import org.tiger.common.ioc.EnableIoc;
import org.tiger.persistence.file.id.IdGenerator;

@EnableIoc(scanPackages = "org.tiger")
public class IdGenTest {

    private IdGenerator idGenerator;

    @Before
    public void before() {
        BeanFactory.autowireBean();
        idGenerator = BeanFactory.getBean(IdGenerator.class);
    }

    @Test
    public void gen() {
        System.out.println(idGenerator.getNextSeq());
        System.out.println(idGenerator.getNextSeq());
        System.out.println(idGenerator.getNextSeq());
        System.out.println(idGenerator.getNextSeq());
        System.out.println(idGenerator.getNextSeq());
    }

}
