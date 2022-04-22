package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.securityJWT.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.securityJWT.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    PLAYER(Sets.newHashSet(PLAYER_READ)),
    ADMIN(Sets.newHashSet(PLAYER_READ, PLAYER_WRITE));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
