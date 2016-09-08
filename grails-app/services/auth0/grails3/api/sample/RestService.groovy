package auth0.grails3.api.sample

import org.grails.web.util.WebUtils

import javax.servlet.http.HttpServletRequest

class RestService {

    static HttpServletRequest getRequest() {
        return WebUtils.retrieveGrailsWebRequest().getCurrentRequest()
    }

}
