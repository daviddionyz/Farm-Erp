package hu.foxpost.farmerp.enums;

public enum Roles {

    BOSS(1),
    SMALL_BOSS(2),
    WORKER(3);

    private int value;

    Roles(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static String getRole(int value) {
        for(Roles r: Roles.values()) {
            if(r.value == value) {
                return String.valueOf(r);
            }
        }

        return null;
    }
}
