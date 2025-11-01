import React, {useState, useRef} from 'react';
import {Button} from '@/components/ui/button';
import {Upload, Mic, StopCircle, CheckCircle, AlertCircle} from 'lucide-react';
import {Progress} from '@/components/ui/progress';

export default function AudioImport({loadRecordings}: { loadRecordings: () => void }) {
    const [isRecording, setIsRecording] = useState(false);
    const [recorder, setRecorder] = useState<MediaRecorder | null>(null);
    const [stream, setStream] = useState<MediaStream | null>(null); // 👈 Храним активный поток
    const [audioUrl, setAudioUrl] = useState<string | null>(null);
    const [mimeType, setMimeType] = useState<string>('audio/webm');
    const [uploadProgress, setUploadProgress] = useState<number | null>(null);
    const [uploadStatus, setUploadStatus] = useState<'idle' | 'uploading' | 'success' | 'error'>('idle');
    const [uploadMessage, setUploadMessage] = useState<string>('');
    const chunks = useRef<Blob[]>([]);

    // 📤 Upload existing audio file
    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        setUploadProgress(0);
        setUploadStatus('uploading');
        setUploadMessage('Uploading file...');

        const formData = new FormData();
        formData.append('file', file);

        try {
            // Create a custom fetch with progress tracking
            const request = new XMLHttpRequest();
            
            request.upload.addEventListener('progress', (e) => {
                if (e.lengthComputable) {
                    const progress = Math.round((e.loaded / e.total) * 100);
                    setUploadProgress(progress);
                }
            });

            request.addEventListener('load', async () => {
                if (request.status >= 200 && request.status < 300) {
                    const data = JSON.parse(request.responseText);
                    if (data.success) {
                        setUploadProgress(100);
                        setUploadStatus('success');
                        setUploadMessage('File uploaded successfully!');
                        console.log('✅ Uploaded:', data);
                        loadRecordings();
                        
                        // Reset status after 3 seconds
                        setTimeout(() => {
                            setUploadStatus('idle');
                            setUploadMessage('');
                        }, 3000);
                    } else {
                        setUploadStatus('error');
                        setUploadMessage('Upload failed: ' + (data.message || 'Unknown error'));
                    }
                } else {
                    setUploadStatus('error');
                    setUploadMessage('Upload failed: Server error');
                }
            });

            request.addEventListener('error', () => {
                setUploadStatus('error');
                setUploadMessage('Upload failed: Network error');
            });

            request.open('POST', '/api/createRecording');
            request.withCredentials = true;
            request.send(formData);
        } catch (err) {
            setUploadStatus('error');
            setUploadMessage('Upload failed: ' + (err instanceof Error ? err.message : 'Unknown error'));
            console.error('⚠️ Upload failed:', err);
        } finally {
            e.target.value = '';
        }
    };

    // 🎙️ Start recording
    const startRecording = async () => {
        try {
            const userStream = await navigator.mediaDevices.getUserMedia({audio: true});
            setStream(userStream);

            // 🎯 Определяем формат в зависимости от браузера
            const preferredMimeType = MediaRecorder.isTypeSupported('audio/mp4;codecs=aac')
                ? 'audio/mp4'
                : 'audio/webm';
            setMimeType(preferredMimeType);

            const newRecorder = new MediaRecorder(userStream, {mimeType: preferredMimeType});
            setRecorder(newRecorder);
            chunks.current = [];

            newRecorder.ondataavailable = e => chunks.current.push(e.data);

            newRecorder.onstop = async () => {
                // ✅ Освобождаем микрофон
                userStream.getTracks().forEach(track => track.stop());
                setStream(null);

                const blob = new Blob(chunks.current, {type: preferredMimeType});
                const url = URL.createObjectURL(blob);
                setAudioUrl(url);

                // 📤 Отправляем запись на сервер
                setUploadProgress(0);
                setUploadStatus('uploading');
                setUploadMessage('Uploading recording...');

                const formData = new FormData();
                const ext = preferredMimeType.includes('mp4') ? 'm4a' : 'webm';
                formData.append('file', blob, `recording.${ext}`);

                try {
                    // Create a custom fetch with progress tracking
                    const request = new XMLHttpRequest();
                    
                    request.upload.addEventListener('progress', (e) => {
                        if (e.lengthComputable) {
                            const progress = Math.round((e.loaded / e.total) * 100);
                            setUploadProgress(progress);
                        }
                    });

                    request.addEventListener('load', async () => {
                        if (request.status >= 200 && request.status < 300) {
                            const data = JSON.parse(request.responseText);
                            if (data.success) {
                                setUploadProgress(100);
                                setUploadStatus('success');
                                setUploadMessage('Recording uploaded successfully!');
                                console.log('✅ Recorded & uploaded:', data);
                                loadRecordings();
                                
                                // Reset status after 3 seconds
                                setTimeout(() => {
                                    setUploadStatus('idle');
                                    setUploadMessage('');
                                }, 300);
                            } else {
                                setUploadStatus('error');
                                setUploadMessage('Upload failed: ' + (data.message || 'Unknown error'));
                            }
                        } else {
                            setUploadStatus('error');
                            setUploadMessage('Upload failed: Server error');
                        }
                    });

                    request.addEventListener('error', () => {
                        setUploadStatus('error');
                        setUploadMessage('Upload failed: Network error');
                    });

                    request.open('POST', '/api/createRecording');
                    request.withCredentials = true;
                    request.send(formData);
                } catch (err) {
                    setUploadStatus('error');
                    setUploadMessage('Upload failed: ' + (err instanceof Error ? err.message : 'Unknown error'));
                    console.error('⚠️ Upload error:', err);
                }
            };

            newRecorder.start();
            setIsRecording(true);
            console.log(`🎙️ Recording started with format: ${preferredMimeType}`);
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

            // ✅ На случай, если Safari не вызывает onstop быстро
            if (stream) {
                stream.getTracks().forEach(track => track.stop());
                setStream(null);
            }

            console.log('🛑 Recording stopped & microphone released.');
        }
    };

    return (
        <div className="flex flex-col gap-4">
            <div>
                <h3 className="text-lg font-semibold mb-1">Import Audio</h3>
                <p className="text-sm text-muted-foreground mb-3">
                    Upload from files or record directly
                </p>

                <input
                    type="file"
                    id="audio-upload"
                    className="hidden"
                    accept="audio/*,.mp3,.wav,.ogg,.m4a,.aac,.flac"
                    capture={false}
                    onChange={handleFileChange}
                />

                <div className="flex flex-wrap gap-3">
                    <Button
                        variant="outline"
                        onClick={() => document.getElementById('audio-upload')?.click()}
                    >
                        <Upload className="w-4 h-4 mr-2"/> Choose File
                    </Button>

                    {!isRecording ? (
                        <Button onClick={startRecording} variant="default">
                            <Mic className="w-4 h-4 mr-2"/> Record
                        </Button>
                    ) : (
                        <Button onClick={stopRecording} variant="destructive">
                            <StopCircle className="w-4 h-4 mr-2"/> Stop
                        </Button>
                    )}
                </div>

                {/* Upload progress and status */}
                {(uploadStatus === 'uploading' || uploadStatus === 'success' || uploadStatus === 'error') && (
                    <div className="mt-4">
                        {uploadStatus === 'uploading' && (
                            <div className="flex items-center gap-2">
                                <div className="flex-1">
                                    <Progress value={uploadProgress || 0} className="h-2" />
                                </div>
                                <span className="text-sm text-muted-foreground">{uploadProgress}%</span>
                            </div>
                        )}
                        <div className={`flex items-center gap-2 mt-2 ${uploadStatus === 'success' ? 'text-green-600' : uploadStatus === 'error' ? 'text-red-600' : 'text-blue-600'}`}>
                            {uploadStatus === 'success' && <CheckCircle className="w-4 h-4" />}
                            {uploadStatus === 'error' && <AlertCircle className="w-4 h-4" />}
                            <span className="text-sm">{uploadMessage}</span>
                        </div>
                    </div>
                )}

                {audioUrl && (
                    <div className="mt-4">
                        <p className="text-sm text-muted-foreground mb-1">Preview:</p>
                        <audio
                            controls
                            src={audioUrl}
                            onError={() =>
                                alert(
                                    '⚠️ Safari cannot play this file inline, but it was recorded and uploaded successfully.'
                                )
                            }
                            className="w-full"
                        />
                    </div>
                )}
            </div>
        </div>
    );
}
