package pl.lodz.uni.biobank.foam.integration;

import org.junit.jupiter.api.Test;
import pl.lodz.uni.biobank.foam.shared.I2B2Integration;

import static org.assertj.core.api.Assertions.assertThat;

class I2B2IntegrationTest {

    @Test
    void shouldCreateI2B2IntegrationWithSubmissionFilePathAndStableId() {
        // given
        String submissionFilePath = "/path/to/i2b2/file.csv";
        String stableId = "DATASET-001";

        // when
        I2B2Integration integration = new I2B2Integration(submissionFilePath, stableId);

        // then
        assertThat(integration.submissionFilePath()).isEqualTo(submissionFilePath);
        assertThat(integration.stableId()).isEqualTo(stableId);
    }

    @Test
    void shouldSupportRecordEquality() {
        // given
        I2B2Integration integration1 = new I2B2Integration("/path/to/file.csv", "DATASET-001");
        I2B2Integration integration2 = new I2B2Integration("/path/to/file.csv", "DATASET-001");
        I2B2Integration integration3 = new I2B2Integration("/different/path.csv", "DATASET-002");

        // when/then
        assertThat(integration1).isEqualTo(integration2);
        assertThat(integration1).isNotEqualTo(integration3);
        assertThat(integration1.hashCode()).isEqualTo(integration2.hashCode());
    }

    @Test
    void shouldHaveProperToStringRepresentation() {
        // given
        I2B2Integration integration = new I2B2Integration("/submissions/i2b2/data.csv", "STUDY-2024");

        // when
        String toString = integration.toString();

        // then
        assertThat(toString).contains("/submissions/i2b2/data.csv");
        assertThat(toString).contains("STUDY-2024");
    }
}
