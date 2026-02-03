from fastapi import FastAPI
from pydantic import BaseModel
from typing import Dict

app = FastAPI()

class RiskRequest(BaseModel):
    userId: str
    features: Dict[str, int]

class  RiskResponse(BaseModel):
    userId: str
    score: float
    decision: str

@app.post("/score", response_model=RiskResponse)
def score_risk(req: RiskRequest):
    login_5m = req.features.get("login_5m", 0)
    login_1h = req.features.get("login_1h", 0)

    if login_5m > 3:
        score = 0.9
        decision = "BLOCK"

    elif login_1h > 5:
        score = 0.7
        decision = "CHALLENGE"

    else: 
        score = 0.2
        decision = "ALLOW"

    return {
        "userId": req.userId,
        "score": score,
        "decision": decision
    }