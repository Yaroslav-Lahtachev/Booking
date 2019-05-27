package com.pingvin.autoservice.test;

import com.pingvin.autoservice.dao.UserDAO;
import com.pingvin.autoservice.entity.User;
import com.pingvin.autoservice.form.UsersForm;
import com.pingvin.autoservice.model.UsersInfo;
import com.pingvin.autoservice.pagination.PaginationResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDAOTest {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private UserDAO userDAO;

    @Test
    @Transactional
    @Rollback(true)
    public void findByIdUserWhenIdNotExists() {
        User userActual = userDAO.findByIdUser(1);
        Assert.assertNull(userActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findByIdUserWhenIdExists() {
        Session session = this.sessionFactory.getCurrentSession();
        User userExpected = new User("test1","test","test","test");
        session.persist(userExpected);
        session.flush();
        User userActual = userDAO.findByIdUser(userExpected.getIdUser());
        Assert.assertEquals(userExpected, userActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findByLoginWhenLoginNotExists() {
        User userActual = userDAO.findByLogin("test");
        Assert.assertNull(userActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findByLoginWhenLoginExists() {
        Session session = this.sessionFactory.getCurrentSession();
        User userExpected = new User("test2","test","test","test");
        session.persist(userExpected);
        session.flush();
        User userActual = userDAO.findByLogin("test2");
        Assert.assertEquals(userExpected, userActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void listUsersInfoWhenUsersExists() {
        Session session = this.sessionFactory.getCurrentSession();
        User user1 = new User("test3","test","test","test");
        session.persist(user1);
        User user2 = new User("test4","test","test","test");
        session.persist(user2);
        User user3 = new User("test5","test","test","test");
        session.persist(user3);
        User user4 = new User("test6","test","test","test");
        session.persist(user4);
        session.flush();

        PaginationResult<UsersInfo> pagination = userDAO.listUsersInfo(1,5,10);
        Assert.assertEquals(4, pagination.getList().size());

        PaginationResult<UsersInfo> pagination2 = userDAO.listUsersInfo(2,3,10);
        Assert.assertEquals(1, pagination2.getList().size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void listUsersInfoWhenUsersNotExists() {
        PaginationResult<UsersInfo> pagination = userDAO.listUsersInfo(2,1,10);
        Assert.assertEquals(0,pagination.getList().size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void addNewUserWhenUserNotExists() {
        UsersForm userForm = new UsersForm();
        userForm.setLogin("test7");
        userForm.setPassword("test");
        userForm.setEmail("test");
        userDAO.addNewUser(userForm);

        Session session = this.sessionFactory.getCurrentSession();
        User user = session.get(User.class, userDAO.getUsersCount());
        Assert.assertNotNull(user);
    }

    @After
    public void afterEveryTest() {
        Session session = this.sessionFactory.getCurrentSession();
        session.createQuery("DELETE FROM " + User.class.getName()).executeUpdate();
        session.getTransaction().commit();
    }

    @Test
    @Transactional
    @Rollback(true)
    public void getUsersCountWhenUsersNotExists() {
        Assert.assertEquals(0, userDAO.listUsersInfo(1,10,10).getList().size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void getUsersCountWhenUsersExists() {
        Session session = this.sessionFactory.getCurrentSession();
        User user1 = new User("test8","test","test","test");
        session.persist(user1);
        User user2 = new User("test9","test","test","test");
        session.persist(user2);
        User user3 = new User("test10","test","test","test");
        session.persist(user3);
        User user4 = new User("test11","test","test","test");
        session.persist(user4);
        session.flush();

        Assert.assertEquals(4, userDAO.listUsersInfo(1,10,10).getList().size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void removeUserWhenUserExists() {
        Session session = this.sessionFactory.getCurrentSession();
        User user1 = new User("test12","test","test","test");
        session.persist(user1);

        userDAO.removeUser(user1.getIdUser());
        Assert.assertNull(session.get(User.class, user1.getIdUser()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void changeUserRole() {
        Session session = this.sessionFactory.getCurrentSession();
        User user1 = new User("test13","test","test","test");
        session.persist(user1);

        userDAO.changeUserRole(user1.getIdUser(),"test2");
        Assert.assertEquals(session.get(User.class, user1.getIdUser()).getRole(), "test2");
    }
}