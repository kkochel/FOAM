package pl.lodz.uni.biobank.foam.outbox;

import java.util.List;

public record Credentials(String passwordHash, List<String> publicKey) {
}
