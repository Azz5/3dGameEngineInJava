# 3D Game Engine in Java

A custom 3D game engine written in Java with GLSL shader support, featuring scene management, a shader-based rendering pipeline, and user input handling. This engine is designed for learning and experimentation with core graphics and engine architecture concepts.

## Features

- **Core Rendering Pipeline**: Supports vertex and fragment shaders via GLSL for flexible, real-time rendering.
- **Scene Management**: Hierarchical scene graph to organize and transform objects.
- **Mesh & Texture Handling**: Load and render 3D models with texture mapping support.
- **Camera System**: First-person and orbit-style camera controls.
- **Lighting**: Basic Phong lighting model with directional, point, and spot lights.
- **Input Handling**: Keyboard and mouse input for navigation and interaction.
- **Utilities**: Vector and matrix math classes, GLSL shader loader, and error logging.

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- GLFW library for window creation and input handling
- LWJGL (Lightweight Java Game Library) for OpenGL bindings
- GLSL-compatible GPU drivers

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Azz5/3dGameEngineInJava.git
   ```
2. Open the project in your IDE (e.g., IntelliJ IDEA or Eclipse).
3. Add LWJGL and GLFW dependencies to your build (e.g., via Maven or Gradle).
4. Ensure your run configuration uses the correct native library paths for LWJGL.

## Usage

1. Run the `main` method in `com.engine.Main`.
2. Use **W/A/S/D** or arrow keys to move the camera.
3. Move the mouse to look around.
4. Press **Esc** to exit.

## Project Structure

```plaintext
3dGameEngineInJava/
├── src/main/java/com/engine/
│   ├── Main.java            # Entry point
│   ├── rendering/           # Shader and OpenGL setup
│   ├── scene/               # Scene graph and object classes
│   ├── input/               # Keyboard and mouse handlers
│   ├── math/                # Vector and matrix math utilities
│   └── utils/               # Shader loader, logger, etc.
├── assets/                  # Example models and textures
└── README.md                # This file
``` 

## Contributing

Contributions are welcome! To contribute:

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/YourFeature`).
3. Commit your changes (`git commit -m "Add YourFeature"`).
4. Push to the branch (`git push origin feature/YourFeature`).
5. Open a Pull Request.

## License

This project is open-source and available under the MIT License. See [LICENSE](LICENSE) for details.
