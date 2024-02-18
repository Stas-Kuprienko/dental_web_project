package edu.dental;

import edu.dental.domain.APIManager;
import edu.dental.security.AuthenticationService;
import edu.dental.security.IFilterVerification;
import edu.dental.service.Repository;
import edu.dental.service.JsonObjectParser;

import java.util.Properties;


public enum WebAPI {

    INSTANCE;

    private static Properties SECRET_KEY;
    private AuthenticationService authenticationService;
    private Repository repository;
    private JsonObjectParser jsonObjectParser;
    private IFilterVerification filterVerification;

    WebAPI() {}

    public synchronized AuthenticationService getAuthenticationService() {
        if (authenticationService == null) {
            authenticationService = APIManager.INSTANCE.init(AuthenticationService.class);
        }
        return authenticationService;
    }

    public synchronized Repository getRepository() {
        if (repository == null) {
            repository = APIManager.INSTANCE.init(Repository.class);
        }
        return repository;
    }

    public synchronized JsonObjectParser getJsonParser() {
        if (jsonObjectParser == null) {
            jsonObjectParser = APIManager.INSTANCE.init(JsonObjectParser.class);
        }
        return jsonObjectParser;
    }

    public synchronized IFilterVerification getFilterVerification() {
        if (filterVerification == null) {
            filterVerification = APIManager.INSTANCE.init(IFilterVerification.class);
        }
        return filterVerification;
    }

    public static void setSecretKeyProperties(Properties secretKey) {
        SECRET_KEY = secretKey;
    }

    public static Properties getSecretKeyProperties() {
        return SECRET_KEY;
    }
}
