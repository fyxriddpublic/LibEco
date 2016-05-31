package com.fyxridd.lib.eco;

import com.fyxridd.lib.core.api.SqlApi;
import com.fyxridd.lib.core.api.config.ConfigApi;
import com.fyxridd.lib.core.api.config.Setter;
import com.fyxridd.lib.core.api.plugin.SimplePlugin;
import com.fyxridd.lib.eco.config.EcoConfig;
import com.fyxridd.lib.eco.manager.DaoManager;
import com.fyxridd.lib.eco.manager.EcoManager;
import com.fyxridd.lib.eco.manager.VaultManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import java.io.File;

public class EcoPlugin extends SimplePlugin{
    public static EcoPlugin instance;

    private DaoManager daoManager;
    private EcoManager ecoManager;

    private EcoConfig ecoConfig;

    @Override
    public void onLoad() {
        super.onLoad();
        //注册Economy服务
        Bukkit.getServicesManager().register(Economy.class, new VaultManager(), this, ServicePriority.Highest);
    }

    @Override
    public void onEnable() {
        instance = this;

        //注册配置
        ConfigApi.register(pn, EcoConfig.class);
        //添加配置监听
        ConfigApi.addListener(pn, EcoConfig.class, new Setter<EcoConfig>() {
            @Override
            public void set(EcoConfig value) {
                ecoConfig = value;
            }
        });
        //注册Mapper文件
        SqlApi.registerMapperXml(new File(dataPath, "EcoUserMapper.xml"));

        daoManager = new DaoManager();
        ecoManager = new EcoManager();

        super.onEnable();
    }

    public EcoManager getEcoManager() {
        return ecoManager;
    }

    public DaoManager getDaoManager() {
        return daoManager;
    }

    public EcoConfig getEcoConfig() {
        return ecoConfig;
    }
}