package pl.lodz.uni.biobank.foam.app.permission;

public enum PermissionStatus {
    AVAILABLE("available"), REVOKED("revoked");

    public final String label;

    PermissionStatus(String label) {
        this.label = label;
    }
}
