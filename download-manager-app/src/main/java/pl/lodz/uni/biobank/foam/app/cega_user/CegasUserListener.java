package pl.lodz.uni.biobank.foam.app.cega_user;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.lodz.uni.biobank.foam.app.sda.api.CegaUserMessage;

@Component
public class CegasUserListener {
    private static final Logger log = LoggerFactory.getLogger(CegasUserListener.class);
    private final CegaUserService cegaUserService;

    public CegasUserListener(CegaUserService cegaUserService) {
        this.cegaUserService = cegaUserService;
    }

    @EventListener
    public void handleEvent(CegaUserMessage event) {
        log.info("Handle CegaUserMessage: {}", event);
        cegaUserService.saveOrUpdate(event);
    }
}
