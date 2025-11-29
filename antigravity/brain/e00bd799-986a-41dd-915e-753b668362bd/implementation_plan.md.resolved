# Implementation Plan - DermaScan

## User Review Required
> [!IMPORTANT]
> **Final Tech Stack Confirmation**:
> *   **Frontend**: React Native (Expo) - Cross-platform mobile app.
> *   **Backend**: Java (Spring Boot) + PostgreSQL - Robust API and data management.
> *   **AI Service**: Python (FastAPI) Microservice.
>     *   **Model**: We will use a multi-class model like `Tanishq77/skin-condition-classifier` (Hugging Face) or `thatmehtaguy/Skin-Disease-Classification-using-CNN`.
>     *   *Capabilities*: These models can identify **Acne, Eczema, Psoriasis, Ringworm, and other common rashes**, in addition to potential cancerous lesions (depending on the specific dataset used).
>     *   *Future Proofing*: We can swap this model for `google/derm-foundation` later for more advanced analysis.

## Proposed Changes

### 1. Frontend (Mobile App)
*   **Framework**: React Native (Expo SDK 50+).
*   **Language**: TypeScript.
*   **UI Library**: Gluestack-UI (or Tamagui).
*   **Navigation**: Expo Router.
*   **Key Screens**:
    *   `Home`: Dashboard with recent scans.
    *   `Camera`: Custom camera view to capture skin issues.
    *   `Analysis`: Display results from the AI.

### 2. Backend (Java API)
*   **Framework**: Spring Boot 3.x.
*   **Database**: PostgreSQL.
*   **Endpoints**:
    *   `POST /api/scans`: Upload image & metadata.
    *   `GET /api/scans`: List user history.
    *   `GET /api/scans/{id}`: Get details.
*   **Integration**: Calls the Python AI service via HTTP.

### 3. AI Service (Python)
*   **Framework**: FastAPI (lightweight, fast).
*   **Dependencies**: `torch`, `transformers`, `pillow`.
*   **Function**: Accepts an image -> Preprocesses -> Runs Model -> Returns Probabilities (e.g., "Psoriasis: 85%").

## Verification Plan
*   **Step 1**: Initialize all 3 projects (Expo, Spring Boot, FastAPI).
*   **Step 2**: Create a "Hello World" connection (App -> Java -> Python).
*   **Step 3**: Implement Camera & Image Upload.
*   **Step 4**: Integrate the AI model and display real results.
