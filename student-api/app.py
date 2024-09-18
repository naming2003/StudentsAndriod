from flask import Flask, request
import joblib
import numpy as np
import warnings

# Suppress warnings
warnings.filterwarnings("ignore")

app = Flask(__name__)

# Load the model
model = joblib.load("students.pkl")

@app.route('/api/students', methods=['POST'])
def house():
    Age = int(request.form.get('Age')) 
    Gender = int(request.form.get('Gender'))
    StudyTimeWeekly = float(request.form.get('StudyTimeWeekly'))  
    Absences = int(request.form.get('Absences')) 
    Tutor = int(request.form.get('Tutor')) 
    Parental = int(request.form.get('Parental')) 
    Extra = int(request.form.get('Extra')) 
    Volunt = int(request.form.get('Volunt')) 
    GPA = float(request.form.get('GPA'))

    # Prepare the input for the model
    x = np.array([[Age, Gender, StudyTimeWeekly, Absences, Tutor, Parental, Extra, Volunt, GPA ]])

    # Predict using the model
    prediction = model.predict(x)

    # Return the result
    return {'Grade': int(prediction[0])}, 200    

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=3000)