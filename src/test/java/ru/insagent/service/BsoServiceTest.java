package ru.insagent.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.insagent.dao.UnitDao;
import ru.insagent.document.dao.BsoNormalDao;
import ru.insagent.workflow.dao.ActionDao;
import ru.insagent.workflow.dao.LinkDao;
import ru.insagent.workflow.dao.NodeDao;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({BsoService.class, UnitDao.class, NodeDao.class, LinkDao.class, ActionDao.class, BsoNormalDao.class})
public class BsoServiceTest {
    @Autowired
    private BsoService bsoService;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void nodes() {
        bsoService.nodes(null);
    }
}