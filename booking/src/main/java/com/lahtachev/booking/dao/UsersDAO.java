package com.lahtachev.booking.dao;

import com.lahtachev.booking.entity.User;
import com.lahtachev.booking.form.UsersForm;
import com.lahtachev.booking.model.UsersInfo;
import com.lahtachev.booking.pagination.PaginationResult;
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
                + " (e.idUser,e.login,e.password,e.status) "
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
        user.setLogin(newUser.getLogin());
        user.setPassword(encrPass);
        user.setStatus("ROLE_USER");
       session.persist(user);
        session.flush();

    }

    public int getUsersCount() {
        String sql = "Select max(o.idUser) from " + User.class.getName() + " o ";
        Session session = this.sessionFactory.getCurrentSession();
        Query<Integer> query = session.createQuery(sql, Integer.class);
        Integer value = (Integer) query.getSingleResult();
        if (value == null) {
            return 0;
        }
        return value;
    }

    public void changeUserStatus(int idUser,String status){
        Session session = this.sessionFactory.getCurrentSession();
        User user = findByIdUser(idUser);
        user.setStatus(status);
        session.persist(user);
        session.flush();
    }
}