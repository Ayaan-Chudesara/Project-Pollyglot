# 🦜 PollyGlot
PollyGlot is a web application that provides a simple and intuitive interface for text translation and multi-language text-to-speech (TTS). It features a robust backend built with Spring Boot that handles translation requests via the Hugging Face Inference API, and a responsive frontend built with Angular.

# ✨ Features
Seamless Translation: Translate text from English to French, Spanish, and Japanese.

Multi-language Text-to-Speech: Listen to the translated text spoken in the correct language.

User-Friendly Interface: A clean and modern frontend built with Angular for a smooth user experience.

Modular Architecture: A decoupled design with a Spring Boot backend and an Angular frontend, allowing for easy scaling and maintenance.

# 🛠️ Technologies Used
### Backend
Spring Boot: The core framework for the RESTful API.

Java 17: The programming language used for the backend.

Maven: Dependency management and build tool.

Hugging Face Inference API: Used for powering the machine translation models (Helsinki-NLP/opus-mt-en-fr, opus-mt-en-es, opus-mt-en-ja).

### Frontend
Angular: The framework for building the single-page application.

TypeScript: The primary language for the Angular application.

HTML & CSS: For structuring and styling the user interface.

Web Speech API: The browser's native API for text-to-speech functionality.

# 🚀 Installation & Setup
### Prerequisites
Java Development Kit (JDK) 17 or higher

Apache Maven

Node.js & npm

Angular CLI: Install globally with npm install -g @angular/cli.

Hugging Face API Token: You'll need to create a Hugging Face account and generate a new Access Token from your profile settings.

1. Backend Setup
Clone this repository to your local machine.

Navigate to the pollyglot-backend directory.

Open src/main/resources/application.properties and add your Hugging Face API token:

Properties

huggingface.api.token=hf_YOUR_HUGGINGFACE_API_TOKEN

Run the application from your IDE or using the following Maven command:

./mvnw spring-boot:run

The backend will start on http://localhost:8080.

2. Frontend Setup
Navigate to the pollyglot-frontend directory.

Install the npm dependencies:
npm install

Start the Angular development server:
ng serve

The frontend will be available at http://localhost:4200.

# 👨‍💻 Usage
Ensure both the backend (http://localhost:8080) and the frontend (http://localhost:4200) are running.

Open your web browser and go to http://localhost:4200.

Enter the text you wish to translate into the text area.

Select a target language using the radio buttons.

Click the "Translate" button to see the translated text.

Click the "Speak" button to hear the translated text read aloud by your browser's text-to-speech engine.
