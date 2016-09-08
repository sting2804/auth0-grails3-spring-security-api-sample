package auth0.grails3.api.sample

import grails.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

@Transactional
class DaoService {

    AdminService adminService
    RestTemplate restTemplate
    @Value('${auth0.managementToken}')
    String managementToken

    def makeHttpRequestToAuth0ManagementApi(String url, HttpMethod method, Class returnType, Object body = null) {
        HttpHeaders headers = new HttpHeaders()
        headers.set("Authorization", "Bearer $managementToken")
        HttpEntity httpEntity = new HttpEntity<Object>(body, headers)

        ResponseEntity entity = restTemplate.exchange(url, method, httpEntity, returnType)
        return entity.getBody()
    }

    def createLocalUsersFromListOfAuth0Users() {
        List auth0Users = adminService.findAllUsers()

        List<User> localUserList = User.list()
        if(localUserList.size()>auth0Users.size()){
            List auth0UserIdList = auth0Users*.user_id
            List wrongLocalUsers = localUserList.findAll { !auth0UserIdList.contains(it.userId)}
            wrongLocalUsers.each {
                it.delete(flush:true)
            }
            localUserList = User.list()
        }
        auth0Users.each { userMap ->
            User userForUpdate = localUserList.find { it.userId == userMap.user_id }
            if (userForUpdate) {
                userForUpdate.userId = userMap.user_id
                userForUpdate.email = userMap.email
                userForUpdate.username = userMap.name
                userForUpdate.emailVerified = userMap.email_verified
                userForUpdate.picture = userMap.picture
                userForUpdate.firstName = userMap.given_name
                userForUpdate.surname = userMap.family_name
                //userForUpdate.updatedAt = userMap.updated_at
                //userForUpdate.createdAt = userMap.created_at
            } else {
                userForUpdate = new User(
                        userId: userMap.user_id,
                        email: userMap.email,
                        username: userMap.name,
                        emailVerified: userMap.email_verified,
                        picture: userMap.picture,
                        firstName: userMap.given_name,
                        surname: userMap.family_name,
                        //updatedAt: userMap.updated_at,
                        //createdAt: userMap.created_at,
                )
            }
            userForUpdate.save(flush: true)
            log.debug("local user: " + userForUpdate)
        }
    }
}
