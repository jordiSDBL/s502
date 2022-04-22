package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.securityJWT.security;

public enum ApplicationUserPermission {
    PLAYER_READ("player:read"),
    PLAYER_WRITE("player:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
