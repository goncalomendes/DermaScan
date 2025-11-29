from fastapi import FastAPI, UploadFile, File
from pydantic import BaseModel
import uvicorn

app = FastAPI()

class AnalysisResult(BaseModel):
    condition: str
    confidence: float
    details: str

@app.get("/")
def read_root():
    return {"message": "DermaScan AI Service is running"}

@app.post("/analyze", response_model=AnalysisResult)
async def analyze_image(file: UploadFile = File(...)):
    # Placeholder for actual model inference
    # TODO: Load model and predict
    return {
        "condition": "Unknown",
        "confidence": 0.0,
        "details": "Model not yet integrated"
    }

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
