# WasteWise AI Platform

WasteWise AI is a civic-tech decision-support system designed for proactive waste management, intervention prioritization, and cleanup verification.

## Architecture

- **Backend**: Java Spring Boot 3.3.5, MySQL, JWT Authentication.
- **Frontend**: React, Tailwind CSS (EventMesh platform).
- **AI/ML Service**: FastAPI-based computer vision service.
- **Agent**: Kaggriculture style dynamic optimization and routing.

## Setup

1. **Backend**: Navigate to `backend/` and run using Maven/Gradle.
2. **Frontend**: Navigate to `frontend/`, run `npm install` and `npm run dev`.
3. **AI Service**: Navigate to `python-ai/`, activate `.venv`, install requirements, and run FastAPI server.

## Features

- Secure, alphanumeric authentication.
- Admin dashboard with restricted access.
- Image upload and AI analysis.
- Live Map view for verified hot-spots.
