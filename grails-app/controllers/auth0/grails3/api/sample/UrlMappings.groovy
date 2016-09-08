package auth0.grails3.api.sample

class UrlMappings {

    static mappings = {
        home:"/portal/home"(controller: 'home')
        "/portal/createUser"(controller: 'home', action: [GET: 'createUser'])
        "/portal/findAllUsers"(controller: 'home', action: [GET: 'findAllUsers'])

        "500"(controller: 'error', action: "handleUnexpectedError")
        //"/"(view:"/index")
/*        "500"(controller: 'error', exception: AccessDeniedException,
                action: "handleAuthenticationError")
        "500"(controller: 'error', exception: HttpClientErrorException,
                action: "handleHttpClientErrorException")

        "401"(controller: 'error', action: "handleAuthenticationError")
        "403"(controller: 'error', action: "handleForbiddenError")
        "404"(controller: 'error', action: "handleNotFoundError")*/
    }
}
