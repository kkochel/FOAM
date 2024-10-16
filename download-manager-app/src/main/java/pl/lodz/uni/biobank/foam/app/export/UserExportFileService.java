package pl.lodz.uni.biobank.foam.app.export;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class UserExportFileService {
    private final UserExportFileRepository repository;

    public UserExportFileService(UserExportFileRepository repository) {
        this.repository = repository;
    }

    public void handleEvent(UserExportFilesEventList event) {
        List<UserFile> userFiles = event.list()
                .stream()
                .map(e -> new UserFile(e.datasetId(), e.fileId(), e.username(), e.fileName()))
                .toList();

        repository.saveAll(userFiles);
    }

    @Transactional
    public void updateFileStage(String fileId, String username, ExportStage stage) {
        UserFile file = repository.getFile(fileId, username);
        file.updateStage(stage);
    }

    public List<UserFileResponse> getFiles(String datasetId, String username) {
        return repository.getFiles(datasetId, username)
                .stream()
                .map(f -> new UserFileResponse(f.getStableId(), f.getFileName(), f.getExportStage() != null ? f.getExportStage().label : StringUtils.EMPTY))
                .sorted(Comparator.comparing(UserFileResponse::stableId))
                .toList();
    }
}
