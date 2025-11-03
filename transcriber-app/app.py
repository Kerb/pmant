import uuid
import threading
import os
from flask import Flask, request, jsonify
from faster_whisper import WhisperModel

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False

# Инициализируем модель один раз при запуске контейнера
model_size = "medium"
model = WhisperModel(model_size, device="cpu", compute_type="int8")

# Память для хранения задач
tasks = {}


def transcribe_audio(task_id, filepath):
    try:
        segments, info = model.transcribe(filepath, beam_size=5)
        text = "".join([f"[{int(seg.start // 3600):02d}:{int((seg.start % 3600) // 60):02d}:{int(seg.start % 60):02d}] {seg.text.strip()}\n" for seg in segments])

        tasks[task_id]["status"] = "done"
        tasks[task_id]["text"] = text
        tasks[task_id]["language"] = info.language
        tasks[task_id]["language_probability"] = info.language_probability
    except Exception as e:
        tasks[task_id]["status"] = "error"
        tasks[task_id]["error"] = str(e)
    finally:
        os.remove(filepath)  # Удаляем временный файл


@app.route("/createTranscribeTask", methods=["POST"])
def create_transcribe_task():
    if "file" not in request.files:
        return jsonify({"error": "no file uploaded"}), 400

    file = request.files["file"]
    task_id = str(uuid.uuid4())
    temp_path = f"/tmp/{task_id}.wav"
    file.save(temp_path)

    tasks[task_id] = {"status": "processing"}

    # Запускаем обработку в отдельном потоке
    thread = threading.Thread(target=transcribe_audio, args=(task_id, temp_path))
    thread.start()

    return jsonify({"task_id": task_id})


@app.route("/getTranscribeTask", methods=["GET"])
def get_transcribe_task():
    task_id = request.args.get("task_id")
    if not task_id:
        return jsonify({"status": "error", "text": "task_id is required"}), 400

    task = tasks.get(task_id)
    if not task:
        return jsonify({"status": "error", "text": "task not found"}), 404

    return jsonify(task)


if __name__ == "__main__":
    # Flask слушает все интерфейсы внутри контейнера
    app.run(host="0.0.0.0", port=8000)
