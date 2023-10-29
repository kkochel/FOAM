package pl.lodz.uni.biobank.foam.app.export;

import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.List;

@Service
public class FileExportLogService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT);
    private final FileExportLogRepository logRepository;
    private final UserExportFileService exportFileService;


    public FileExportLogService(FileExportLogRepository logRepository, UserExportFileRepository fileRepository, UserExportFileService exportFileService) {
        this.logRepository = logRepository;
        this.exportFileService = exportFileService;
    }

    public void save(FileExportLog fileExportLog) {
        logRepository.save(fileExportLog);
    }

    public void handleEvent(FileExportMessage event) {
        logRepository.save(new FileExportLog(event.stableId(), event.username(), event.uuid(), event.exportStage()));
        exportFileService.updateFileStage(event.stableId(), event.username(), event.exportStage());
    }

    public List<FileExportHistoryItemResponse> getFileHistory(String fileId, String userName) {
        return logRepository.getFileLogs(fileId, userName)
                .stream()
                .map(fel -> new FileExportHistoryItemResponse(fel.getExportStage().label, DATE_TIME_FORMATTER.format(fel.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()))))
                .sorted(Comparator.comparing(FileExportHistoryItemResponse::created))
                .toList();
    }
}
