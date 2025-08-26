# Complete Postman Guide for HMS Backend API Testing

Welcome to the Hospital Management System (HMS) Backend API testing guide! This comprehensive guide will walk you through testing all the APIs step by step, perfect for beginners.

## 🎯 What is This Guide About?

This Hospital Management System has APIs for:
- **User Authentication** (Login, Registration)
- **Patient Management** (View, Search, Update patient records)
- **Doctor Management** (View doctors, specializations)
- **Appointment System** (Create, Update, Schedule appointments)

## 📋 Prerequisites (What You Need Before Starting)

### 1. Install Required Software
- **Postman**: Download from [postman.com](https://www.postman.com/downloads/)
- **HMS Backend Server**: Your Spring Boot application should be running
- **MySQL Database**: Make sure MySQL is running with the hospital database

### 2. Start Your Backend Server
1. Open your terminal/command prompt
2. Navigate to your HMS backend folder
3. Run: `mvn spring-boot:run` or run the BackendApplication.java file
4. Wait for the message: "Started BackendApplication in X seconds"
5. Your server should be running on `http://localhost:8080`

## 🔧 Setting Up Postman (Step by Step)

### Step 1: Create Your First Environment
Think of an environment as a storage space for variables you'll use repeatedly.

1. Open Postman
2. Click the **gear icon (⚙️)** in the top right corner
3. Click **"Add"** to create a new environment
4. Name it: `HMS Backend`
5. Add these variables:

| Variable Name | Initial Value | Current Value |
|--------------|---------------|---------------|
| `base_url` | `http://localhost:8080` | `http://localhost:8080` |
| `auth_token` |  | (leave empty for now) |

6. Click **"Add"** to save

### Step 2: Create Your API Collection
A collection is like a folder that organizes all your API requests.

1. Click **"Collections"** in the left sidebar
2. Click **"Create Collection"**
3. Name it: `HMS Backend APIs`
4. Add description: `All API endpoints for Hospital Management System`
5. Click **"Create"**

### Step 3: Select Your Environment
1. In the top right corner, click the dropdown next to the eye icon
2. Select **"HMS Backend"** from the list

## 🚀 Testing APIs Step by Step

### 🔐 Section 1: Authentication APIs

Authentication is like showing your ID card - you need to prove who you are before accessing the system.

#### 📝 Test 1: Register an Admin User
Let's create the first admin user who can manage the system.

1. **Right-click** your collection → **"Add request"**
2. **Name**: `Register Admin`
3. **Method**: Select `POST` from dropdown
4. **URL**: `http://localhost:8080/api/auth/register/admin`
5. **Headers**: Click "Headers" tab, add:
   - **Key**: `Content-Type`
   - **Value**: `application/json`
6. **Body**: Click "Body" tab → select "raw" → choose "JSON" from dropdown
7. **Paste this JSON**:
```json
{
    "username": "admin",
    "password": "Admin@123",
    "name": "Hospital Administrator",
    "email": "admin@hospital.com"
}
```
8. **Click "Send"**
9. **Expected Response**: `"Admin registered successfully"`

#### 📝 Test 2: Login to Get Access Token
Now let's login to get a special token (like a digital key) to access protected features.

1. **Add new request**: `Login`
2. **Method**: `POST`
3. **URL**: `http://localhost:8080/api/auth/login`
4. **Headers**: `Content-Type: application/json`
5. **Body (JSON)**:
```json
{
    "username": "admin",
    "password": "Admin@123"
}
```
6. **Important - Add Post-response Script**:
   - Click the "Tests" tab
   - Paste this code (it automatically saves your login token):
```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("auth_token", jsonData.token);
    console.log("Token saved: " + jsonData.token);
}
```
7. **Click "Send"**
8. **Expected Response**: You should see a token in the response
9. **Check**: Look at your environment variables - the `auth_token` should now have a value

#### 📝 Test 3: Register a Doctor
Let's add a doctor to the system.

1. **Add new request**: `Register Doctor`
2. **Method**: `POST`
3. **URL**: `http://localhost:8080/api/auth/register/doctor`
4. **Headers**: `Content-Type: application/json`
5. **Body (JSON)**:
```json
{
    "username": "drmohit",
    "password": "Mohit@123",
    "name": "Dr. Mohit Sharma",
    "specialization": "Cardiology",
    "phone": "9876543210",
    "email": "mohit@hospital.com",
    "qualification": "MBBS, MD Cardiology",
    "experience": 8,
    "address": "123 Medical Plaza, Delhi",
    "consultationFee": 800.00
}
```
6. **Click "Send"**
7. **Expected Response**: `"Doctor registered successfully"`

#### 📝 Test 4: Register a Patient
Now let's register a patient.

1. **Add new request**: `Register Patient`
2. **Method**: `POST`
3. **URL**: `http://localhost:8080/api/auth/register/patient`
4. **Headers**: `Content-Type: application/json`
5. **Body (JSON)**:
```json
{
    "username": "rohitkumar",
    "password": "Rohit@123",
    "name": "Rohit Kumar",
    "age": 28,
    "gender": "Male",
    "phone": "8765432109",
    "email": "rohit@email.com",
    "address": "456 Green Park, Mumbai",
    "dateOfBirth": "1995-03-15",
    "bloodGroup": "B+",
    "emergencyContact": "8765432108",
    "medicalHistory": "Diabetes - Type 2"
}
```
6. **Click "Send"**
7. **Expected Response**: `"Patient registered successfully"`

### 👥 Section 2: Patient Management APIs

These APIs help manage patient information. Most require authentication.



#### 📝 Test 5: View All Patients
1. **Add new request**: `Get All Patients`
2. **Method**: `GET`
3. **URL**: `http://localhost:8080/api/patients`
4. **Headers**: Add this important header for authentication:
   - **Key**: `Authorization`
   - **Value**: `Bearer {{auth_token}}`
5. **Click "Send"**
6. **Expected Response**: List of all patients in the system

#### 📝 Test 6: Get Patient by ID
1. **Add new request**: `Get Patient by ID`
2. **Method**: `GET`
3. **URL**: `http://localhost:8080/api/patients/1` (assuming patient ID is 1)
4. **Headers**: `Authorization: Bearer {{auth_token}}`
5. **Click "Send"**
6. **Expected Response**: Details of the specific patient

#### 📝 Test 7: Search Patient by Email
1. **Add new request**: `Get Patient by Email`
2. **Method**: `GET`
3. **URL**: `http://localhost:8080/api/patients/email/rohit@email.com`
4. **Click "Send"** (no authentication needed for this endpoint)
5. **Expected Response**: Patient details matching that email

#### 📝 Test 8: Search Patients by Name
1. **Add new request**: `Search Patients by Name`
2. **Method**: `GET`
3. **URL**: `http://localhost:8080/api/patients/search?name=Rohit`
4. **Click "Send"**
5. **Expected Response**: All patients with "Rohit" in their name

### 👨‍⚕️ Section 3: Doctor Management APIs

#### 📝 Test 9: View All Doctors
1. **Add new request**: `Get All Doctors`
2. **Method**: `GET`
3. **URL**: `http://localhost:8080/api/doctors`
4. **Click "Send"** (no authentication needed)
5. **Expected Response**: List of all doctors

#### 📝 Test 10: Get Doctor by Specialization
1. **Add new request**: `Get Doctors by Specialization`
2. **Method**: `GET`
3. **URL**: `http://localhost:8080/api/doctors/specialization/Cardiology`
4. **Click "Send"**
5. **Expected Response**: All cardiologists

#### 📝 Test 11: Search Doctor by Name
1. **Add new request**: `Search Doctors by Name`
2. **Method**: `GET`
3. **URL**: `http://localhost:8080/api/doctors/search?name=Mohit`
4. **Click "Send"**
5. **Expected Response**: Doctors with "Mohit" in their name

#### 📝 Test 12: Get All Specializations
1. **Add new request**: `Get All Specializations`
2. **Method**: `GET`
3. **URL**: `http://localhost:8080/api/doctors/specializations`
4. **Click "Send"**
5. **Expected Response**: List of all medical specializations available

### 📅 Section 4: Appointment Management APIs

#### 📝 Test 13: Create an Appointment
1. **Add new request**: `Create Appointment`
2. **Method**: `POST`
3. **URL**: `http://localhost:8080/api/appointments`
4. **Headers**: 
   - `Authorization: Bearer {{auth_token}}`
   - `Content-Type: application/json`
5. **Body (JSON)**:
```json
{
    "patientId": 1,
    "doctorId": 1,
    "appointmentTime": "2025-08-30T10:30:00",
    "reason": "Heart checkup",
    "notes": "Patient experiencing chest discomfort",
    "status": "SCHEDULED"
}
```
6. **Click "Send"**
7. **Expected Response**: `"Appointment created successfully"`

#### 📝 Test 14: View All Appointments
1. **Add new request**: `Get All Appointments`
2. **Method**: `GET`
3. **URL**: `http://localhost:8080/api/appointments`
4. **Headers**: `Authorization: Bearer {{auth_token}}`
5. **Click "Send"**
6. **Expected Response**: List of all appointments

#### 📝 Test 15: Get Patient's Appointments
1. **Add new request**: `Get Patient Appointments`
2. **Method**: `GET`
3. **URL**: `http://localhost:8080/api/appointments/patient/1`
4. **Headers**: `Authorization: Bearer {{auth_token}}`
5. **Click "Send"**
6. **Expected Response**: All appointments for patient ID 1

#### 📝 Test 16: Get Doctor's Appointments
1. **Add new request**: `Get Doctor Appointments`
2. **Method**: `GET`
3. **URL**: `http://localhost:8080/api/appointments/doctor/1`
4. **Headers**: `Authorization: Bearer {{auth_token}}`
5. **Click "Send"**
6. **Expected Response**: All appointments for doctor ID 1

#### 📝 Test 17: Update Appointment Status
1. **Add new request**: `Update Appointment Status`
2. **Method**: `PATCH`
3. **URL**: `http://localhost:8080/api/appointments/1/status` (appointment ID 1)
4. **Headers**: 
   - `Authorization: Bearer {{auth_token}}`
   - `Content-Type: application/json`
5. **Body (JSON)**:
```json
{
    "status": "COMPLETED"
}
```
6. **Click "Send"**
7. **Expected Response**: `"Appointment status updated successfully"`

#### 📝 Test 18: Get Appointments by Date Range
1. **Add new request**: `Get Appointments by Date Range`
2. **Method**: `GET`
3. **URL**: `http://localhost:8080/api/appointments/date-range?startDate=2025-08-25T00:00:00&endDate=2025-08-31T23:59:59`
4. **Headers**: `Authorization: Bearer {{auth_token}}`
5. **Click "Send"**
6. **Expected Response**: All appointments within the specified date range

## 🔍 Understanding API Responses

### ✅ Success Responses
- **200 OK**: Request successful, data returned
- **201 Created**: New resource created successfully

### ❌ Error Responses
- **400 Bad Request**: Invalid data sent (check your JSON format)
- **401 Unauthorized**: Missing or invalid token (login again)
- **403 Forbidden**: You don't have permission
- **404 Not Found**: Resource doesn't exist
- **500 Internal Server Error**: Server problem (check server logs)




## 🔧 Troubleshooting Common Issues

### "Connection refused" Error
- **Problem**: Backend server not running
- **Solution**: Start your Spring Boot application

### "401 Unauthorized" Error
- **Problem**: Missing or expired token
- **Solution**: Login again to get a fresh token

### "400 Bad Request" Error
- **Problem**: Invalid JSON format
- **Solution**: Check your JSON syntax (missing commas, brackets, etc.)

### "404 Not Found" Error
- **Problem**: Wrong URL or resource doesn't exist
- **Solution**: Check the URL and make sure the resource exists
