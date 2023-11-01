package pl.lodz.uni.biobank.foam.c4ghfs


import no.uio.ifi.crypt4gh.stream.Crypt4GHInputStream
import no.uio.ifi.crypt4gh.util.KeyUtils
import org.springframework.util.FileSystemUtils
import org.testcontainers.shaded.org.apache.commons.io.FileUtils
import spock.lang.Specification

class C4GhServiceTest extends Specification {
    final SERVER_SEC_KEY = "src/test/resources/server_c4gh.sec"
    final REQUESTER_PUB_KEY = "src/test/resources/requester_c4gh.pub"
    final REQUESTER_SEC_KEY = "src/test/resources/requester_c4gh.sec"
    final ARCHIVED_FILE = "/john_doe/encrypted_file"
    final ARCHIVED_FILE_HEADER = "637279707434676801000000010000006c0000000000000078bf6a4ae74f432f0ed6a8a516bf6bd6cdeea5d024245aa6ce7284cd232c2967d1698bb5543c088c606481efcba9494695040e9dc7d326a9d57abd5d1c07b5920426ea81a3ae7560abd2fe845843983173ab943855dc58f2496437cc60af3dfc42cdba34"
    final RE_ENCRYPTED_FILE = "src/test/resources/requester/EGAD44444444/EGAF00000001.c4gh"
    final SERVER_PASSPHRASE = 'uxPFPxI4Oe4H5qJ1UO7kMCkvKgNHEzrX'
    final REQUESTER_PASSPHRASE = 'requester'
    def stageSender = Mock(ExportStageSender)


    def "Re-encrypt file"() {
        given:
        def sut = new C4ghService(new PosixArchiveFileTransmitter("src/test/resources/"), new PosixOutboxFileTransmitter("src/test/resources/", stageSender), stageSender)
        def task = new C4ghExportTask(UUID.randomUUID(), ARCHIVED_FILE_HEADER, ARCHIVED_FILE, "ted_owl/encrypted_file.c4gh", new File(REQUESTER_PUB_KEY).text, "requester", "EGAF00000001", "EGAD44444444")

        when:
        sut.encryptionWithReceiverPublicKey(task, SERVER_SEC_KEY, SERVER_PASSPHRASE)


        then:
        def requesterStream = new Crypt4GHInputStream(new FileInputStream(new File(RE_ENCRYPTED_FILE)), KeyUtils.getInstance().readPrivateKey(new File(REQUESTER_SEC_KEY), REQUESTER_PASSPHRASE.toCharArray()))
        requesterStream.getText().contentEquals("File 21\n")
    }

    def cleanup() {
        FileSystemUtils.deleteRecursively(new File(RE_ENCRYPTED_FILE))
        FileUtils.deleteDirectory(new File("src/test/resources/requester"))
    }
}
