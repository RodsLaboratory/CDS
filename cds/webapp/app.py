from flask import Flask, request, render_template, redirect, url_for
import subprocess
import os

app = Flask(__name__)
UPLOAD_FOLDER = 'uploads'
os.makedirs(UPLOAD_FOLDER, exist_ok=True)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/upload', methods=['POST'])
def upload_file():
    if 'file' not in request.files:
        return redirect(request.url)
    file = request.files['file']
    if file.filename == '':
        return redirect(request.url)
    if file:
        filepath = os.path.join(app.config['UPLOAD_FOLDER'], file.filename)
        file.save(filepath)
        output = execute_program(filepath)
        return render_template('result.html', output=output)

def execute_program(filepath):
    # Replace 'your_program' with the actual program you want to run
    result = subprocess.run(['your_program', filepath], capture_output=True, text=True)
    return result.stdout

if __name__ == '__main__':
    app.run(debug=True)