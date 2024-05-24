package com.cozary.red_panda;

import com.cozary.red_panda.init.ModEntityTypes;
import com.cozary.red_panda.init.ModSound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedPanda {

    public static final String MOD_ID = "red_panda";
    public static final String MOD_NAME = "RedPanda";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {

        ModEntityTypes.loadClass();
        ModSound.loadClass();

    }
}
