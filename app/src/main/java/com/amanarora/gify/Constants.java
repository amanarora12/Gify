package com.amanarora.gify;

public class Constants {
    private Constants(){}

    public static final String BASE_URL = "http://api.giphy.com";
    public static final String EXTRA_GIF_URL_KEY = "extra_gif_url_key";

    public static final class Injection {
        private Injection() {}

        public static final class Named {
            private Named() {}
            public static final String GIPHY_API_KEY = "giphy_api_key";
        }
    }
}
