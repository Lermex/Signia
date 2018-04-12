# Signia
Signia is an interactive system for learning sign languages that uses Machine Learning to recognize the user's signs
through his PC camera. The general teaching style is inspired by [Duolingo](duolingo.com)

This is a programming demo project created for [UEL](uel.ac.uk).

![Screenshot](https://user-images.githubusercontent.com/895159/38692054-95bf9ef0-3e7a-11e8-84db-6392162c74b9.jpeg)

## Technologies
We combine JavaFX and JFoenix for the UI with OpenCV for direct camera access and TensorFlow running Google's ImageNet
neural network for image recognition.

## Building / Running
This project is built using Gradle. Install Gradle on your machine or use the supplied wrapper, then either:
1. To build an executable uberjar (i.e. a jar containing all dependencies)
```
gradle shadowJar
```
2. To run directly from Gradle:
```
gradle runShadow
```

## Retraining the image recognition neural network (a TensorFlow graph)
The ML graph used by this project can be reproduced or modified by following the
[TensorFlow for Poets](https://codelabs.developers.google.com/codelabs/tensorflow-for-poets/)
tutorial from Google
