package edu.dental;

import edu.dental.domain.APIManager;
import edu.dental.security.AuthenticationService;
import edu.dental.service.Repository;
import edu.dental.service.tools.JsonObjectParser;


public enum WebAPI {

    INSTANCE;

    private AuthenticationService authenticationService;
    private Repository repository;
    private JsonObjectParser jsonObjectParser;

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
}
