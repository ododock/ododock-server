package ododock.webserver.web;

public final class ResourcePath {

    // api
    public static final String API = "/api";
    public static final String API_VERSION = "/v1alpha1";

    // path varaibles
    public static final String PATH_VAR_ID = "id";
    public static final String PATH_VAR_SUB_ID = "subId";
    public static final String PATH_VAR_NAME = "name";

    // resources
    public static final String AUTH = "/auth";
    public static final String TOKEN = "/token";
    public static final String REFRESH = "/refresh";
    public static final String LOGOUT = "/logout";
    public static final String ACCOUNTS = "/accounts";
    public static final String ACCOUNTS_SUBRESOURCE_SOCIAL_ACCOUNTS = "/social-accounts";
    public static final String ACCOUNTS_SUBRESOURCE_PASSWORD = "/password";
    public static final String ACCOUNTS_SUBRESOURCE_PROFILE = "/profile";
    public static final String PROFILE_SUBRESOURCE_IMAGE = "/image";
    public static final String ACCOUNTS_SUBRESOURCE_CATEGORIES = "/categories";
    public static final String ACCOUNTS_SUBRESOURCE_CATEGORIES_POSITION = "/position";
    public static final String VERIFICATION = "/verification";
    public static final String ARTICLES = "/articles";
    public static final String ARTICLE_SUBRESOURCE_TAGS = "/tags";
    public static final String CONTENTS = "/contents";
    public static final String CURATIONS = "/curations";
    public static final String CURATIONS_SUBRESOURCE_POPULAR_BOOKS = "/popular-books";

    // static
    public static final String DOCS = "/static/docs";

    // urls
    public static final String LOCALHOST = "http://localhost:3000";
    public static final String AUTH_PROCESSING_URL = API + API_VERSION + AUTH + TOKEN;
    public static final String AUTH_REFRESH_URL = API + API_VERSION + AUTH + TOKEN + REFRESH;
    public static final String AUTH_LOGOUT_URL = API + API_VERSION + AUTH + LOGOUT;
    public static final String OAUTH2 = "/oauth2";
    public static final String AUTHORIZATION = "/authorization";
    public static final String CALLBACK = "/callback";
    public static final String OAUTH_CALLBACK = LOCALHOST + OAUTH2 + CALLBACK +"sub=%s&provider=%s&access_token=%s&refresh_token=%s";
    public static final String ACTUATOR = "/actuator/**";

}
