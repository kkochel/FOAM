package pl.lodz.uni.biobank.foam.app.sda.handlers;

public enum CegaMessageType {
    PERMISSION("permission"),
    PERMISSION_DELETED("permission.deleted"),
    PASSWORD_UPDATED("password.updated"),
    KEYS_UPDATED("keys.updated"),
    DEAD_LETTER("unknown");

    public final String label;

    CegaMessageType(String label) {
        this.label = label;
    }

    public static CegaMessageType findByLabel(String label) {
        for (CegaMessageType type : values()) {
            if (type.label.equals(label)) {
                return type;
            }
        }

        return DEAD_LETTER;
    }
}
