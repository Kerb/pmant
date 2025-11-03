package eu.pmant.app.service;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public interface SpeechRecognizeService {

    String recognizeSpeech(String audioFilePath);

    TranscriptionResult getTranscriptionResult(String taskId);

    @Data
    @ToString
    class TranscriptionResult {
        private final String status;
        private final String text;
    }

    @RequiredArgsConstructor
    @Getter
    enum WhisperStatus {
        DONE("done"),
        IN_PROGRESS("processing"),
        FAILED("failed");

        private final String status;
    }
}
