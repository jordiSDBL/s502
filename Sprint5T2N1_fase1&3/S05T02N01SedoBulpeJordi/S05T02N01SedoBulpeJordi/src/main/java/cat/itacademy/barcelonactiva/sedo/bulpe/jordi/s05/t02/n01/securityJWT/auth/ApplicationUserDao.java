package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.securityJWT.auth;

import java.util.Optional;

public interface ApplicationUserDao {

    Optional<ApplicationUser> selectApplicationUserByUsername(String username);

}
