# Spring Security OAuth2
This project shows manual configuration of an oauth2 authorisation
and resource server using spring oauth2. As the name implies, `oauth2-auth-server`
is the authentication server which authorizes requests made by registered resource servers
on behalf of end users.
`resource-server-app` acts as the resource server with minimal configuration thanks to Spring oauth2

## FEATURES
- Custom user entity
- Custom granted authority
- Custom token store using JPA
- Custom access token
- Custom refresh token
- Persistent UserDetailsService
- Dynamic client registration

#### Running the Application
 Be sure  to replace environment variables in `application-dev.yml` files
 
    $ git clone
    $ cd /path/to/directory
    $ ./mvnw spring-boot:run
 
#### oauth2-auth-server-app
- User details and registered clients are stored in the sql database
- Tokens are stored in MongoDB
Repositories can be swapped out at will. But, if this is done, then annotations will have to be changed in the data models


### MORE information
Here are some things not yet fully implemented
- At the moment, the authentication and resource servers use the `DefaultTokenServices` implementation of `AuthorizationServerTokenServices` and `ResourceServerTokenServices` to verify tokens for my use case, this is perfectly fine. But a `TokenEnhancer` is not yet implemented and this will be done at a later time.
- The password grant type is the only grant type manually configured. other grant types are handled by spring auto configuration
- The client




