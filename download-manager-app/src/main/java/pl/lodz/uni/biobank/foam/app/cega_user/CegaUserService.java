package pl.lodz.uni.biobank.foam.app.cega_user;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.lodz.uni.biobank.foam.app.sda.api.CegaUserMessage;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CegaUserService {
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
    }

    public String getUserFullName(String username) {
        return cegaUserRepository
                .findByUsername(username)
                .map(CegaUser::getFullName)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

//    'c4gh-v1'
    public CegaUserKey getPublicC4ghKey(String username) {
        return cegaUserRepository
                .userWithKeys(username)
                .getKeys()
                .stream()
                .filter(k ->k.getType().equals("c4gh-v1"))
                .findAny()
                .orElseThrow(()-> new RuntimeException("User dont have C$GH public key"));
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
}
