import os
import json
import base64
import google.generativeai as genai
from models import AiAnalysisResponse, VerifyCleanupResponse
from prompts import WASTE_ANALYSIS_SYSTEM_PROMPT, CLEANUP_VERIFICATION_PROMPT

genai.configure(api_key=os.getenv("GEMINI_API_KEY", "DUMMY_KEY_FOR_NOW"))

def analyze_waste_image(base64_str: str) -> AiAnalysisResponse:
    try:
        model = genai.GenerativeModel("gemini-1.5-flash")
        
        # In a real app we'd decode base64 to bytes, wrap in Part, etc.
        # But for hackathon speed mapping, if key is dummy, return a mock response immediately!
        if os.getenv("GEMINI_API_KEY") is None or os.getenv("GEMINI_API_KEY") == "DUMMY_KEY_FOR_NOW":
             return AiAnalysisResponse(
                 category="Plastic",
                 subType="PET Bottle",
                 confidence=0.92,
                 contamination="Mild dirt",
                 conditionDesc="Crushed plastic water bottle",
                 recyclable=True,
                 disposalCategory="Dry Waste",
                 recommendation="Empty liquids and crush to save space.",
                 explanation="PET plastic is highly recyclable if kept clean.",
                 impactScore=60,
                 impactReason="Recyclable but takes 400 years to decompose if littered.",
                 correctlySegregated=True
             )

        # Build generative content for actual call. Assuming jpeg for speed.
        image_part = {
            "mime_type": "image/jpeg",
            "data": base64.b64decode(base64_str)
        }
        
        response = model.generate_content([WASTE_ANALYSIS_SYSTEM_PROMPT, image_part])
        text_resp = response.text.strip().replace("```json", "").replace("```", "")
        parsed = json.loads(text_resp)
        return AiAnalysisResponse(**parsed)
    except Exception as e:
        print(f"Vision error {e}")
        # Return safe fallback
        return AiAnalysisResponse(
                 category="Unknown",
                 subType="Unknown",
                 confidence=0.5,
                 contamination="Unknown",
                 conditionDesc="Unable to process image.",
                 recyclable=False,
                 disposalCategory="Mixed Waste",
                 recommendation="Ensure lighting is good and try again.",
                 explanation="The AI couldn't clearly identify the item.",
                 impactScore=50,
                 impactReason="Default score due to processing error.",
                 correctlySegregated=False
             )

def verify_cleanup(before_b64: str, after_b64: str) -> VerifyCleanupResponse:
    # Dummy mock for speed
    return VerifyCleanupResponse(
        status="COMPLETED",
        confidence=0.88,
        explanation="Visible waste footprint has been removed entirely from the after image."
    )
