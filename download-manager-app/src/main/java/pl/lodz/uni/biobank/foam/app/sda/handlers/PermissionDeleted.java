package pl.lodz.uni.biobank.foam.app.sda.handlers;

public class PermissionDeleted implements MessageHandler{
    @Override
    public void setNext(MessageHandler handler) {

    }

    @Override
    public void handle(CegaMessageType type, String message) {
        throw new RuntimeException();
    }
}
