package eu.pmant.app.dto;

import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public enum MeetingStatus {

    WAIT_TRANSCRIBATION {
        @Nonnull
        @Override
        public List<MeetingStatus> getAllowedStatusesForTransition() {
            return List.of(TRANSCRIBATION_IN_PROGRESS);
        }
    },

    TRANSCRIBATION_IN_PROGRESS {
        @Nonnull
        @Override
        public List<MeetingStatus> getAllowedStatusesForTransition() {
            return List.of(TRANSCRIBATION_SUCCESS, TRANSCRIBATION_FAIL);
        }
    },

    TRANSCRIBATION_SUCCESS {
        @Nonnull
        @Override
        public List<MeetingStatus> getAllowedStatusesForTransition() {
            return List.of(MINUTES_OF_MEETING_IN_PROGRESS);
        }
    },

    TRANSCRIBATION_FAIL {
        @Nonnull
        @Override
        public List<MeetingStatus> getAllowedStatusesForTransition() {
            return List.of();
        }
    },

    MINUTES_OF_MEETING_IN_PROGRESS {
        @NotNull
        @Override
        public List<MeetingStatus> getAllowedStatusesForTransition() {
            return List.of(MINUTES_OF_MEETING_SUCCESS, MINUTES_OF_MEETING_FAIL);
        }
    },

    MINUTES_OF_MEETING_SUCCESS {
        @NotNull
        @Override
        public List<MeetingStatus> getAllowedStatusesForTransition() {
            return List.of();
        }
    },
    MINUTES_OF_MEETING_FAIL {
        @NotNull
        @Override
        public List<MeetingStatus> getAllowedStatusesForTransition() {
            return List.of();
        }
    };

    public MeetingStatus transitTo(MeetingStatus nextStatus) {
        List<MeetingStatus> allowedStatusesForTransition = getAllowedStatusesForTransition();
        if (allowedStatusesForTransition.contains(nextStatus)) {
            return nextStatus;
        }
        throw new RuntimeException("Запрещен переход из состояния " + this + " в " + nextStatus + ". " +
            "Допустимыми являются переходы в " + allowedStatusesForTransition);
    }

    @Nonnull
    public abstract List<MeetingStatus> getAllowedStatusesForTransition();

    public static Optional<MeetingStatus> of(String status) {
        for (MeetingStatus value : values()) {
            if (value.name().equals(status)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
