package com.pingvin.autoservice.dao;

import com.pingvin.autoservice.entity.User;
import com.pingvin.autoservice.form.UsersForm;
import com.pingvin.autoservice.model.UsersInfo;
import com.pingvin.autoservice.pagination.PaginationResult;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Repository
@Transactional
public class  UsersDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsersDAO() {
    }

    public User findByIdUser(int idUser) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.get(User.class, idUser);
    }

    public User findByLogin(String login) {
        Session session = this.sessionFactory.getCurrentSession();
        return (User)session.createCriteria(User.class).add(Restrictions.eq("login", login)).uniqueResult();
    }

    public PaginationResult<UsersInfo> listUsersInfo(int page, int maxResult, int maxNavPage) {
        String sql = "Select new " + UsersInfo.class.getName()
                + " (e.id,e.login,e.password,e.email,e.role) "
                + " from " + User.class.getName() + " e ";
        Session session = this.sessionFactory.getCurrentSession();
        Query<UsersInfo> query = session.createQuery(sql,UsersInfo.class);
        return new PaginationResult<UsersInfo>(query, page, maxResult, maxNavPage);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void addNewUser(UsersForm newUser) {
        Session session = this.sessionFactory.getCurrentSession();
        User user = new User();
        String encrPass = this.passwordEncoder.encode(newUser.getPassword());
        int idUser = getUsersCount() + 1;
        user.setIdUser(idUser);
        user.setLogin(newUser.getLogin());
        user.setPassword(encrPass);
        user.setRole("ROLE_USER");
        user.setEmail(newUser.getEmail()); //newUser.getLogin()+"@mail.com"
        session.persist(user);
        session.flush();

    }

    public int getUsersCount() {
        String sql = "Select max(o.id) from " + User.class.getName() + " o ";
        Session session = this.sessionFactory.getCurrentSession();
        Query<Integer> query = session.createQuery(sql, Integer.class);
        Integer value = (Integer) query.getSingleResult();
        if (value == null) {
            return 0;
        }
        return value;
    }

    public void changeUserRole(int idUser,String role){
        Session session = this.sessionFactory.getCurrentSession();
        User user = findByIdUser(idUser);
        user.setRole(role);
        session.persist(user);
        session.flush();
    }
}