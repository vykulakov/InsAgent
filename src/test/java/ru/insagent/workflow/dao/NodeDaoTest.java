package ru.insagent.workflow.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(NodeDao.class)
public class NodeDaoTest {
    @Autowired
    private NodeDao nodeDao;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void listByIds() {
        nodeDao.listByIds(null);
        nodeDao.listByIds(Collections.emptyList());
        nodeDao.listByIds(Arrays.asList(1, 2, 3));
    }
}