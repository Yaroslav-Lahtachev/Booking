package com.pingvin.autoservice.test;

import com.pingvin.autoservice.dao.UserDAO;
import com.pingvin.autoservice.entity.User;
import com.pingvin.autoservice.form.UsersForm;
import com.pingvin.autoservice.model.UsersInfo;
import com.pingvin.autoservice.pagination.PaginationResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

    @Test
    @Transactional
    @Rollback(true)
    public void findByIdUserWhenIdNotExists() {
        User userActual = new UserDAO().findByIdUser(9999);
        Assert.assertNull(userActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findByIdUserWhenIdExists() {
        Session session = this.sessionFactory.getCurrentSession();
        User userExpected = new User(9999,"test","test","test","test");
        session.persist(userExpected);
        session.flush();
        User userActual = new UserDAO().findByIdUser(9999);
        Assert.assertEquals(userExpected, userActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findByLoginWhenLoginNotExists() {
        User userActual = new UserDAO().findByLogin("test");
        Assert.assertNull(userActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findByLoginWhenLoginExists() {
        Session session = this.sessionFactory.getCurrentSession();
        User userExpected = new User(9999,"test","test","test","test");
        session.persist(userExpected);
        session.flush();
        User userActual = new UserDAO().findByLogin("test");
        Assert.assertEquals(userExpected, userActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void listUsersInfoWhenUsersExists() {
        Session session = this.sessionFactory.getCurrentSession();
        User user9996 = new User(9996,"test","test","test","test");
        session.persist(user9996);
        User user9997 = new User(9997,"test","test","test","test");
        session.persist(user9997);
        UsersInfo usersInfo = new UsersInfo(user9997);
        User user9998 = new User(9998,"test","test","test","test");
        session.persist(user9998);
        User user9999 = new User(9999,"test","test","test","test");
        session.persist(user9999);
        session.flush();

        PaginationResult<UsersInfo> pagination = new UserDAO().listUsersInfo(2,1,10);
        Assert.assertEquals(usersInfo, pagination.getList().get(0));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void listUsersInfoWhenUsersNotExists() {
        PaginationResult<UsersInfo> pagination = new UserDAO().listUsersInfo(2,1,10);
        Assert.assertNull(pagination);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void addNewUserWhenUserNotExists() {
        UsersForm userForm = new UsersForm();
        userForm.setLogin("test");
        userForm.setPassword("test");
        userForm.setEmail("test");
        new UserDAO().addNewUser(userForm);

        Session session = this.sessionFactory.getCurrentSession();
        User user = session.get(User.class, new UserDAO().getUsersCount());
        Assert.assertNotNull(user);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void getUsersCountWhenUsersNotExists() {
        int nextId = new UserDAO().getUsersCount();
        Assert.assertEquals(0, nextId);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void getUsersCountWhenUsersExists() {
        Session session = this.sessionFactory.getCurrentSession();
        User user9996 = new User(9996,"test","test","test","test");
        session.persist(user9996);
        User user9997 = new User(9997,"test","test","test","test");
        session.persist(user9997);
        User user9998 = new User(9998,"test","test","test","test");
        session.persist(user9998);
        User user9999 = new User(9999,"test","test","test","test");
        session.persist(user9999);
        session.flush();

        int nextId = new UserDAO().getUsersCount();
        Assert.assertEquals(4, nextId);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void removeUserWhenUserExists() {
        Session session = this.sessionFactory.getCurrentSession();
        User user9996 = new User(9996,"test","test","test","test");
        session.persist(user9996);

        new UserDAO().removeUser(9996);
        Assert.assertNull(session.get(User.class, 9996));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void changeUserRole() {
        Session session = this.sessionFactory.getCurrentSession();
        User user9996 = new User(9996,"test","test","test","test");
        session.persist(user9996);

        new UserDAO().changeUserRole(9996,"test2");
        Assert.assertEquals(session.get(User.class, 9996).getRole(), "test2");
    }
}