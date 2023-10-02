package pl.lodz.uni.biobank.foam.app.cega_user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("cega-users")
public class CegaUserController {
    private final CegaUserService service;

    public CegaUserController(CegaUserService service) {
        this.service = service;
    }


    @GetMapping
    ResponseEntity<List<String>> getUsers() {
        List<String> users = service.getUsers();

        return ResponseEntity.ok(users);
    }

}
