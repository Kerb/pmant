import React, { useState, useRef } from 'react';
import { Button } from '@/components/ui/button'; // или свой Button
import { Pause, Play, Upload, Mic, StopCircle } from 'lucide-react';

export default function AudioImport({ loadRecordings }: { loadRecordings: () => void }) {
    const [isRecording, setIsRecording] = useState(false);
    const [recorder, setRecorder] = useState<MediaRecorder | null>(null);
    const [audioUrl, setAudioUrl] = useState<string | null>(null);
    const chunks = useRef<Blob[]>([]);

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        const formData = new FormData();
        formData.append('file', file);

        try {
            const res = await fetch('/api/createRecording', {
                method: 'POST',
                body: formData,
                credentials: 'include'
            });
            const data = await res.json();
            if (data.success) {
                console.log('✅ Uploaded:', data);
                loadRecordings();
            } else {
                console.error('❌ Upload error:', data);
            }
        } catch (err) {
            console.error('⚠️ Upload failed:', err);
        } finally {
            e.target.value = ''; // allow re-selecting the same file
        }
    };

    // 🎙️ Start recording
    const startRecording = async () => {
        try {
            const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
            const newRecorder = new MediaRecorder(stream);
            setRecorder(newRecorder);
            chunks.current = [];

            newRecorder.ondataavailable = e => chunks.current.push(e.data);
            newRecorder.onstop = async () => {
                const blob = new Blob(chunks.current, { type: 'audio/webm' });
                const url = URL.createObjectURL(blob);
                setAudioUrl(url);

                const formData = new FormData();
                formData.append('file', blob, 'recording.webm');

                const res = await fetch('/api/createRecording', {
                    method: 'POST',
                    body: formData,
                    credentials: 'include'
                });

                const data = await res.json();
                if (data.success) {
                    console.log('✅ Recorded & uploaded:', data);
                    loadRecordings();
                } else {
                    console.error('❌ Error uploading recording:', data);
                }
            };

            newRecorder.start();
            setIsRecording(true);
            console.log('🎙️ Recording started...');
        } catch (err) {
            console.error('🚫 Cannot record audio:', err);
            alert('Microphone access denied or unavailable.');
        }
    };

    // ⏹️ Stop recording
    const stopRecording = () => {
        if (recorder && recorder.state !== 'inactive') {
            recorder.stop();
            setIsRecording(false);
            console.log('🛑 Recording stopped.');
        }
    };

    return (
        <div className="flex flex-col gap-4">
            <div>
                <h3 className="text-lg font-semibold mb-1">Import Audio</h3>
                <p className="text-sm text-muted-foreground mb-3">Upload from files or record directly</p>

                <input
                    type="file"
                    id="audio-upload"
                    className="hidden"
                    accept="audio/*,.mp3,.wav,.ogg,.m4a,.aac,.flac"
                    capture="false"
                    onChange={handleFileChange}
                />

                <div className="flex flex-wrap gap-3">
                    <Button
                        variant="outline"
                        onClick={() => document.getElementById('audio-upload')?.click()}
                    >
                        <Upload className="w-4 h-4 mr-2" /> Choose File
                    </Button>

                    {!isRecording ? (
                        <Button onClick={startRecording} variant="default">
                            <Mic className="w-4 h-4 mr-2" /> Record
                        </Button>
                    ) : (
                        <Button onClick={stopRecording} variant="destructive">
                            <StopCircle className="w-4 h-4 mr-2" /> Stop
                        </Button>
                    )}
                </div>

                {audioUrl && (
                    <div className="mt-4">
                        <p className="text-sm text-muted-foreground mb-1">Preview:</p>
                        <audio controls src={audioUrl} className="w-full" />
                    </div>
                )}
            </div>
        </div>
    );
}
