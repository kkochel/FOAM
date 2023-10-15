package pl.lodz.uni.biobank.foam.app.cega_user;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table( schema = "cega", name = "cega_users")
public class CegaUser {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "cega_user_generator")
    @SequenceGenerator(name = "cega_user_generator", sequenceName = "cega_user_seq", schema = "cega", allocationSize = 1)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "country")
    private String country;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "institution")
    private String institution;

    @Column(name = "password")
    private String password;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(schema = "cega")
    private Set<CegaUserKey> keys = new HashSet<>();

    protected CegaUser() {
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<CegaUserKey> getKeys() {
        return keys;
    }

    public void setKeys(Set<CegaUserKey> keys) {
        this.keys = keys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CegaUser cegaUser)) return false;
        return Objects.equals(username, cegaUser.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
