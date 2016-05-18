package com.fyxridd.lib.eco.manager;

import com.fyxridd.lib.eco.EcoPlugin;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 与Vault交互
 */
public class VaultManager extends AbstractEconomy {
    public static final String ECO_NAME = "LibEco";
    private static final List<String> banks = new ArrayList<>();

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return ECO_NAME;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return EcoPlugin.instance.getEcoConfig().getRound();
    }

    @Override
    public String format(double amount) {
        return EcoPlugin.instance.getEcoConfig().getFormat().replace("{amount}", ""+amount);
    }

    @Override
    public String currencyNamePlural() {
        return EcoPlugin.instance.getEcoConfig().getPluralCurrencyName();
    }

    @Override
    public String currencyNameSingular() {
        return EcoPlugin.instance.getEcoConfig().getSingularCurrencyName();
    }

    @Override
    public boolean hasAccount(String name) {
        return EcoPlugin.instance.getEcoManager().init(name) != null;
    }

    @Override
    public boolean hasAccount(String name, String worldName) {
        return hasAccount(name);
    }

    @Override
    public double getBalance(String s) {
        return EcoPlugin.instance.getEcoManager().get(s);
    }

    @Override
    public double getBalance(String s, String s1) {
        return getBalance(s);
    }

    @Override
    public boolean has(String s, double v) {
        return getBalance(s) > v;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return has(s, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        double preAmount = getBalance(s);
        boolean success = EcoPlugin.instance.getEcoManager().del(s, v);
        double postAmount = getBalance(s);
        return new EconomyResponse(postAmount - preAmount, postAmount, success? EconomyResponse.ResponseType.SUCCESS: EconomyResponse.ResponseType.FAILURE, "fail");
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return withdrawPlayer(s, v);
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        double preAmount = getBalance(s);
        boolean success = EcoPlugin.instance.getEcoManager().add(s, v);
        double postAmount = getBalance(s);
        return new EconomyResponse(postAmount - preAmount, postAmount, success? EconomyResponse.ResponseType.SUCCESS: EconomyResponse.ResponseType.FAILURE, "fail");
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return depositPlayer(s, v);
    }

    @Override
    public EconomyResponse createBank(String s, String player) {
        return new EconomyResponse(0, getBalance(player), EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
    }

    @Override
    public List<String> getBanks() {
        return banks;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return EcoPlugin.instance.getEcoManager().init(playerName) != null;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }
}
