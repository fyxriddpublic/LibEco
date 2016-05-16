package com.fyxridd.lib.eco;

import com.fyxridd.lib.config.api.ConfigApi;
import com.fyxridd.lib.core.api.plugin.SimplePlugin;
import com.fyxridd.lib.eco.config.EcoConfig;
import com.fyxridd.lib.eco.manager.DaoManager;
import com.fyxridd.lib.eco.manager.EcoManager;
import com.fyxridd.lib.eco.manager.VaultManager;
import com.fyxridd.lib.sql.api.SqlApi;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import java.io.File;

public class EcoPlugin extends SimplePlugin{
    public static EcoPlugin instance;

    private EcoManager ecoManager;
    private DaoManager daoManager;

    @Override
    public void onEnable() {
        instance = this;

        //注册配置
        ConfigApi.register(pn, EcoConfig.class);
        //注册Mapper文件
        SqlApi.registerMapperXml(new File(dataPath, "EcoUserMapper.xml"));

        ecoManager = new EcoManager();
        daoManager = new DaoManager();
        //注册Economy服务
        Bukkit.getServicesManager().register(Economy.class, new VaultManager(), this, ServicePriority.Highest);

        super.onEnable();
    }

    public EcoManager getEcoManager() {
        return ecoManager;
    }

    public DaoManager getDaoManager() {
        return daoManager;
    }
}