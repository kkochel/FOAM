package pl.lodz.uni.biobank.foam.app.cega_user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.uni.biobank.foam.app.sda.api.CegaUserMessage;
import pl.lodz.uni.biobank.foam.app.sda.api.KeysUpdatedMessage;
import pl.lodz.uni.biobank.foam.app.sda.api.PasswordUpdatedMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CegaUserService {
    private static final Logger log = LoggerFactory.getLogger(CegaUserService.class);
    private final CegaUserRepository cegaUserRepository;

    public CegaUserService(CegaUserRepository cegaUserRepository) {
        this.cegaUserRepository = cegaUserRepository;
    }

    public void saveOrUpdate(CegaUserMessage event) {
        CegaUser cu = cegaUserRepository
                .findByUsername(event.username())
                .orElseGet(CegaUser::new);

        mapEventToEntity(event, cu);
        cegaUserRepository.save(cu);
        log.info("User cega event processed correctly. Username: {} ", event.username());
    }

    public UserData getUserData(Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        boolean keyPresent = !getPublicC4ghKey(username).isEmpty();
        String fullName = getUserFullName(username);

        return new UserData(fullName, keyPresent);
    }

    private String getUserFullName(String username) {
        return cegaUserRepository
                .findByUsername(username)
                .map(CegaUser::getFullName)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    //'c4gh-v1'
    public List<CegaUserKey> getPublicC4ghKey(String username) {
        return cegaUserRepository
                .userWithC4ghKeys(username)
                .map(cegaUser -> cegaUser
                        .getKeys()
                        .stream()
                        .filter(k -> k.getType().equals("c4gh-v1"))
                        .collect(Collectors.toList())).orElseGet(ArrayList::new);

    }

    private static void mapEventToEntity(CegaUserMessage event, CegaUser cu) {
        Set<CegaUserKey> keys = event.keys().stream()
                .map(k -> new CegaUserKey(k.type(), k.key()))
                .collect(Collectors.toSet());

        cu.setEmail(event.email());
        cu.setCountry(event.country());
        cu.setFullName(event.fullName());
        cu.setInstitution(event.institution());
        cu.setPassword(event.passwordHash());
        cu.setFullName(event.fullName());
        cu.setUsername(event.username());
        cu.setKeys(keys);
    }

    @Transactional
    public void updateKeys(KeysUpdatedMessage message) {
        Set<CegaUserKey> keys = message
                .keys()
                .stream()
                .map(k -> new CegaUserKey(k.type(), k.key()))
                .collect(Collectors.toSet());

        cegaUserRepository
                .userWithKeys(message.user())
                .setKeys(keys);
    }

    @Transactional
    public void updatePassword(PasswordUpdatedMessage message) {
        cegaUserRepository
                .findByUsername(message.user())
                .ifPresent(cegaUser -> cegaUser.setPassword(message.passwordHash()));
    }
}
