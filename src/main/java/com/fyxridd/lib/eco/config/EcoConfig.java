package com.fyxridd.lib.eco.config;

import com.fyxridd.lib.core.api.config.basic.Path;
import com.fyxridd.lib.core.api.config.limit.Min;

public class EcoConfig {
    @Path("max")
    @Min(0)
    private double max;

    @Path("round")
    @Min(-1)
    private int round;

    @Path("format")
    private String format;

    @Path("singularCurrencyName")
    private String singularCurrencyName;

    @Path("pluralCurrencyName")
    private String pluralCurrencyName;

    public double getMax() {
        return max;
    }

    public int getRound() {
        return round;
    }

    public String getFormat() {
        return format;
    }

    public String getSingularCurrencyName() {
        return singularCurrencyName;
    }

    public String getPluralCurrencyName() {
        return pluralCurrencyName;
    }
}
