package com.amanarora.gify;

public class Constants {
    private Constants(){}

    public static final String BASE_URL = "http://api.giphy.com";

    public static final class Injection {
        private Injection() {}

        public static final class Named {
            private Named() {}
            public static final String GIPHY_API_KEY = "giphy_api_key";
        }
    }
}
