package pl.lodz.uni.biobank.foam.app.cega_user;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;

import java.util.Objects;

@Embeddable
public class CegaUserKey {
    private String type;

    @Lob
    private String key;

    protected CegaUserKey() {
    }

    public CegaUserKey(String type, String key) {
        this.type = type;
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CegaUserKey that)) return false;
        return Objects.equals(type, that.type) && Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, key);
    }
}
