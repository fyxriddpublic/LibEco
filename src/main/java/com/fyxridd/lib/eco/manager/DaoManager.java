package com.fyxridd.lib.eco.manager;

import com.fyxridd.lib.core.api.SqlApi;
import com.fyxridd.lib.eco.mapper.EcoUserMapper;
import com.fyxridd.lib.eco.model.EcoUser;
import org.apache.ibatis.session.SqlSession;

import java.util.Collection;

/**
 * 与数据库交互
 */
public class DaoManager {
    public Collection<EcoUser> getAllEcoUsers() {
        SqlSession session = SqlApi.getSqlSessionFactory().openSession();
        try {
            EcoUserMapper mapper = session.getMapper(EcoUserMapper.class);
            return mapper.selectAll();
        } finally {
            session.close();
        }
    }

    public void saveOrUpdateEcoUsers(Collection<EcoUser> c) {
        if (c == null || c.isEmpty()) return;

        SqlSession session = SqlApi.getSqlSessionFactory().openSession();
        try {
            EcoUserMapper mapper = session.getMapper(EcoUserMapper.class);
            for (EcoUser user:c) {
                if (!mapper.exist(user.getName())) mapper.insert(user);
                else mapper.update(user);
            }
            session.commit();
        } finally {
            session.close();
        }
    }
}
