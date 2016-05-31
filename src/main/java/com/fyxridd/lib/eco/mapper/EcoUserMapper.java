package com.fyxridd.lib.eco.mapper;

import com.fyxridd.lib.eco.model.EcoUser;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

public interface EcoUserMapper {
    boolean exist(@Param("name") String name);

    /**
     * @return 不存在返回null
     */
    Collection<EcoUser> selectAll();

    void insert(EcoUser user);

    void update(EcoUser user);
}
