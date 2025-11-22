package com.unmei21.core.uuid;

import java.util.UUID;

public class GenID {
    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}
