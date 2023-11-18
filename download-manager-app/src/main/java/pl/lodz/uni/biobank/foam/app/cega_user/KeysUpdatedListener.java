package pl.lodz.uni.biobank.foam.app.cega_user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.lodz.uni.biobank.foam.app.sda.api.KeysUpdatedMessage;

@Component
public class KeysUpdatedListener {
    private static final Logger log = LoggerFactory.getLogger(KeysUpdatedListener.class);
    private final CegaUserService cegaUserService;

    public KeysUpdatedListener(CegaUserService cegaUserService) {
        this.cegaUserService = cegaUserService;
    }

    @EventListener
    public void handle(KeysUpdatedMessage message) {
        cegaUserService.updateKeys(message);
        log.info("Keys for user has been updated {}" , message);
    }
}
