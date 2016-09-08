package auth0.grails3.api.sample

import com.auth0.spring.security.api.Auth0UserDetails
import grails.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

@Transactional
class AdminService {

    DaoService daoService

    @Value('${auth0.connection}')
    String connection
    @Value('${auth0.domain}')
    String domain
    @Value('${auth0.clientId}')
    String clientId
    @Value('${auth0.clientSecret}')
    String clientSecret
    @Value('${auth0.managementToken}')
    String managementToken
    @Value('${auth0.issuer}')
    String issuer


    List findAllUsers() {
        String url = issuer + "api/v2/users?q=app_metadata.clients%3A (\"$domain\")&search_engine=v2"
        List users = daoService.makeHttpRequestToAuth0ManagementApi(url, HttpMethod.GET, List.class)
        return users
    }

    def createUser(String password, String email, String role = null) {
        if(!role || role!="ROLE_INTEGRATOR")
            role = "ROLE_USER"

        String url = issuer + "api/v2/users"
        Map userMap = [
                connection: connection,
                name: email,
                nickname: email.substring(0, email.indexOf('@')),
                password: password,
                email: email,
                email_verified: false,
                app_metadata:[
                        clients:[
                                domain
                        ],
                        roles: [
                                role
                        ]
                ]
        ]
        return daoService.makeHttpRequestToAuth0ManagementApi(url, HttpMethod.POST, Object, userMap)
    }

    boolean ensureAdmin() {
        final Auth0UserDetails currentUser = getAuth0UserDetails()
        log.info(currentUser.username)
        return true
    }

    Auth0UserDetails getAuth0UserDetails() {
        final Authentication authentication = SecurityContextHolder.context.authentication
        def a = authentication.credentials
        def b = authentication.details
        final Auth0UserDetails currentUser = (Auth0UserDetails) authentication.principal
        return currentUser
    }

    def getAuth0UserMap() {
        final Authentication authentication = SecurityContextHolder.context.authentication
        return authentication?.details
    }

}
