package com.mtp.aqa.self.api.misc.model;

/**
 * An artificial class to model entities having ID and NAME fields only(tags, categories etc).
 */
public class IdNamePair {
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
