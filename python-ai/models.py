from typing import Optional
from pydantic import BaseModel

class AiAnalysisResponse(BaseModel):
    category: str
    subType: str
    confidence: float
    contamination: str
    conditionDesc: str
    recyclable: bool
    disposalCategory: str
    recommendation: str
    explanation: str
    impactScore: int
    impactReason: str
    correctlySegregated: bool

class AnalyzeWasteRequest(BaseModel):
    image: str  # Base64 encoded image

class VerifyCleanupRequest(BaseModel):
    before_image: str
    after_image: str

class VerifyCleanupResponse(BaseModel):
    status: str
    confidence: float
    explanation: str
