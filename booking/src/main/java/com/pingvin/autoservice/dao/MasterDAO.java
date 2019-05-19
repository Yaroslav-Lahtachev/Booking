package com.pingvin.autoservice.dao;

import com.pingvin.autoservice.entity.Master;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class MasterDAO {
    @Autowired
    private SessionFactory sessionFactory;

    public MasterDAO() {
    }

    public Master findByIdMaster(int id){
        Session session = this.sessionFactory.getCurrentSession();
        return session.get(Master.class, id);
    }

    public int getFreeMaster(int IdOffer){
        return 1;
    }
}
