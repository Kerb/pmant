package eu.pmant.app.dto;

import jakarta.annotation.Nonnull;

import java.util.List;

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
            return List.of();
        }
    },

    TRANSCRIBATION_FAIL{
        @Nonnull
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

}
