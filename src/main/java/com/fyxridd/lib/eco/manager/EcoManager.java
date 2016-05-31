package com.fyxridd.lib.eco.manager;

import com.fyxridd.lib.core.api.PlayerApi;
import com.fyxridd.lib.core.api.config.ConfigApi;
import com.fyxridd.lib.core.api.config.Setter;
import com.fyxridd.lib.core.realname.NotReadyException;
import com.fyxridd.lib.eco.EcoPlugin;
import com.fyxridd.lib.eco.config.EcoConfig;
import com.fyxridd.lib.eco.model.EcoUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.EventExecutor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EcoManager {
    private EcoConfig config;

    //缓存
    //全部读取
    private Map<String, EcoUser> ecoUsers;

    //优化策略
    //需要保存的玩家列表
    private Set<EcoUser> needUpdateList = new HashSet<>();

    public EcoManager() {
        //添加配置监听
        ConfigApi.addListener(EcoPlugin.instance.pn, EcoConfig.class, new Setter<EcoConfig>() {
            @Override
            public void set(EcoConfig value) {
                config = value;
            }
        });
        //读取数据
        loadData();
        //注册事件
        {
            //插件停止事件
            Bukkit.getPluginManager().registerEvent(PluginDisableEvent.class, EcoPlugin.instance, EventPriority.NORMAL, new EventExecutor() {
                @Override
                public void execute(Listener listener, Event e) throws EventException {
                    saveAll();
                }
            }, EcoPlugin.instance);
            //玩家进服事件
            Bukkit.getPluginManager().registerEvent(PlayerJoinEvent.class, EcoPlugin.instance, EventPriority.LOWEST, new EventExecutor() {
                @Override
                public void execute(Listener listener, Event e) throws EventException {
                    PlayerJoinEvent event = (PlayerJoinEvent) e;
                    init(event.getPlayer().getName());
                }
            }, EcoPlugin.instance);
        }
        //计时器: 保存
        Bukkit.getScheduler().scheduleSyncRepeatingTask(EcoPlugin.instance, new Runnable() {
            @Override
            public void run() {
                saveAll();
            }
        }, 726, 726);
    }

    /**
     * 初始化
     * @param name 大小写可以不正确
     * @return 如果玩家不存在或其它异常返回null
     */
    public EcoUser init(String name) {
        if (name == null) return null;
        //玩家存在性检测
        try {
            name = PlayerApi.getRealName(null, name);
        } catch (NotReadyException e) {
            return null;
        }
        if (name == null) return null;

        EcoUser user = ecoUsers.get(name);
        if (user == null) {
            user = new EcoUser(name, 0);
            ecoUsers.put(name, user);
            needUpdateList.add(user);
        }
        return user;
    }

    public double get(Player p) {
        if (p == null) return -1;

        return get(p.getName());
    }

    /**
     * @return 不存在返回-1
     */
    public double get(String name) {
        if (name == null) return -1;
        //玩家存在性检测
        try {
            name = PlayerApi.getRealName(null, name);
        } catch (NotReadyException e) {
            return -1;
        }
        if (name == null) return -1;

        EcoUser eu = init(name);
        if (eu == null) return -1;
        return eu.getMoney();
    }

    public boolean set(Player p, int amount) {
        if (p == null) return false;

        return set(p.getName(), (double) amount);
    }

    public boolean set(String name, int amount) {
        if (name == null) return false;

        return set(name, (double) amount);
    }

    public boolean set(Player p, double amount) {
        if (p == null) return false;

        return set(p.getName(), amount);
    }

    /**
     * @param name 大小写可以不正确
     */
    public boolean set(String name, double amount) {
        if (name == null) return false;
        //玩家存在性检测
        try {
            name = PlayerApi.getRealName(null, name);
        } catch (NotReadyException e) {
            return false;
        }
        if (name == null) return false;
        //
        EcoUser eu = init(name);
        if (eu == null) return false;
        if (amount < 0) amount = 0;
        else if (amount > config.getMax()) amount = config.getMax();
        eu.setMoney(amount);
        //添加更新
        needUpdateList.add(eu);
        return true;
    }

    public boolean add(Player p, int amount) {
        if (p == null) return false;

        return add(p.getName(), (double) amount);
    }

    public boolean add(String name, int amount) {
        if (name == null) return false;

        return add(name, (double) amount);
    }

    public boolean add(Player p, double amount) {
        if (p == null) return false;

        return add(p.getName(), amount);
    }

    public boolean add(String name, double amount) {
        if (name == null) return false;
        //玩家存在性检测
        try {
            name = PlayerApi.getRealName(null, name);
        } catch (NotReadyException e) {
            return false;
        }
        if (name == null) return false;
        //
        if (amount <= 0) return true;
        //
        EcoUser eu = init(name);
        if (eu == null) return false;
        double result = eu.getMoney() + amount;
        if (result < 0 || result > config.getMax()) result = config.getMax();
        return set(name, result);
    }

    public boolean del(Player p, int amount) {
        if (p == null) return false;

        return del(p.getName(), (double) amount);
    }

    public boolean del(String name, int amount) {
        if (name == null) return false;

        return del(name, (double) amount);
    }

    public boolean del(Player p, double amount) {
        if (p == null) return false;

        return del(p.getName(), amount);
    }

    public boolean del(String name, double amount) {
        if (name == null) return false;
        //玩家存在性检测
        try {
            name = PlayerApi.getRealName(null, name);
        } catch (NotReadyException e) {
            return false;
        }
        if (name == null) return false;
        //
        if (amount <= 0) return true;
        //
        EcoUser eu = init(name);
        if (eu == null) return false;
        double result = eu.getMoney() - amount;
        if (result < 0 || result > config.getMax()) result = 0;
        return set(name, result);
    }

    private void saveAll() {
        if (!needUpdateList.isEmpty()) {
            EcoPlugin.instance.getDaoManager().saveOrUpdateEcoUsers(needUpdateList);
            needUpdateList.clear();
        }
    }

    private void loadData() {
        ecoUsers = new HashMap<>();
        for (EcoUser eu : EcoPlugin.instance.getDaoManager().getAllEcoUsers()) ecoUsers.put(eu.getName(), eu);
    }
}
