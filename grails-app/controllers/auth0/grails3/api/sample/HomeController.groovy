package auth0.grails3.api.sample

import grails.converters.JSON
import org.springframework.security.access.prepost.PreAuthorize

@PreAuthorize("hasAuthority('ROLE_ADMIN')")
class HomeController {

    static defaultAction = "home"

    AdminService adminService

    def home() {
        log.info("Home page")

        adminService.ensureAdmin()
        println User.list()
        def user = adminService.getAuth0UserMap()
        render ([user: user] as JSON)
    }

    def createUser(){
        def user = adminService.createUser(params.password, params.email)
        render ([user: user] as JSON)
    }

    def findAllUsers(){
        render ([users: adminService.findAllUsers()] as JSON)
    }
}
