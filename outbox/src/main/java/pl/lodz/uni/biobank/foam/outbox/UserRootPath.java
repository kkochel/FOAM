package pl.lodz.uni.biobank.foam.outbox;

@FunctionalInterface
public interface UserRootPath {
    String getPath(String outboxFolder, String username);
}
