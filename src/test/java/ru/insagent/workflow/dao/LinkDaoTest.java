package ru.insagent.workflow.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.insagent.model.Roles;
import ru.insagent.workflow.model.Link;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(LinkDao.class)
public class LinkDaoTest {
    @Autowired
    private LinkDao linkDao;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void listByUser() {
        Roles roles = new Roles();
        roles.add("user");

        List<Link> actual = linkDao.listByRoles(roles);
        assertThat(actual).isEmpty();
    }
}