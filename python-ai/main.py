from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from models import AnalyzeWasteRequest, VerifyCleanupRequest, AiAnalysisResponse, VerifyCleanupResponse
from vision_service import analyze_waste_image, verify_cleanup
from dotenv import load_dotenv

load_dotenv()

app = FastAPI(title="WasteWise AI Inference Service")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.post("/analyze-waste", response_model=AiAnalysisResponse)
def analyze_waste(request: AnalyzeWasteRequest):
    return analyze_waste_image(request.image)

@app.post("/verify-cleanup", response_model=VerifyCleanupResponse)
def verify(request: VerifyCleanupRequest):
    return verify_cleanup(request.before_image, request.after_image)

@app.get("/health")
async def health():
    return {"status": "ok"}
