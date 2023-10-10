package pl.lodz.uni.biobank.foam.app.cega_user;


import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.lodz.uni.biobank.foam.app.sda.api.CegaUserMessage;

@Component
public class CegasUserListener {
    private final CegaUserService cegaUserService;

    public CegasUserListener(CegaUserService cegaUserService) {
        this.cegaUserService = cegaUserService;
    }

    @EventListener
    public void handleEvent(CegaUserMessage event) {
        cegaUserService.saveOrUpdate(event);
    }
}
