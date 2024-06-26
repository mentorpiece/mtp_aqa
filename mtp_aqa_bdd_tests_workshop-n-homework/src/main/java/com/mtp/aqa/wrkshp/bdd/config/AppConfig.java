package com.mtp.aqa.wrkshp.bdd.config;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:app.properties"})
public interface AppConfig extends Config {
    @Config.Key("url.base")
    String url();

    String admin();

    String password();
}
