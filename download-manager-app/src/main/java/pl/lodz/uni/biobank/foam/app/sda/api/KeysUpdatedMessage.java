package pl.lodz.uni.biobank.foam.app.sda.api;

import java.util.List;

public record KeysUpdatedMessage(
        String type,
        List<KeyMessage> keys,
        String user
) {
}
