package pl.lodz.uni.biobank.foam.app.cega_user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.lodz.uni.biobank.foam.app.sda.api.PasswordUpdatedMessage;

@Component
public class PasswordUpdatedListener {
    private static final Logger log = LoggerFactory.getLogger(PasswordUpdatedListener.class);
    private final CegaUserService cegaUserService;

    public PasswordUpdatedListener(CegaUserService cegaUserService) {
        this.cegaUserService = cegaUserService;
    }

    @EventListener
    public void handle(PasswordUpdatedMessage message) {
        cegaUserService.updatePassword(message);
        log.info("Password has been updated for user {}" , message.user());
    }
}
