package pl.lodz.uni.biobank.foam.app.export;

public enum ExportStage {
    ACCEPTED("Accepted"),
    RE_ENCRYPTION("Re-encryption"),
    TRANSFER("Transfer to outbox"),
    READY("Ready to download"),
    FAILED("Failed, please contact with help desk"),
    DELETED("The file has been removed");

    public final String label;

    ExportStage(String label) {
        this.label = label;
    }
}
