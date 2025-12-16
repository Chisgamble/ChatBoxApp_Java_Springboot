package app.chatbox.mapper;

import app.chatbox.dto.SpamReportDTO;
import app.chatbox.model.SpamReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface SpamReportMapper {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    @Mapping(target = "reportedId", source = "reported.id")
    @Mapping(target = "reportedUsername", source = "reported.username")
    @Mapping(target = "reportedEmail", source = "reported.email")
    @Mapping(target = "reporterId", source = "reporter.id")
    @Mapping(target = "isLocked", source = "reported.isLocked")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "formatInstant")
    @Mapping(target = "status", source = "status")
    SpamReportDTO toDTO(SpamReport report);

    @Named("formatInstant")
    default String formatInstant(Instant instant) {
        if (instant == null) return null;
        return FORMATTER.format(instant);
    }
}