# Description
A map maker that can be used to create birds-eye-view maps of towns, cities, villages... any sort of settlements.

![example](https://github.com/user-attachments/assets/37f97267-3be7-4643-909c-2ecce18be88d)

## Built Using
Built using Java Maven, making use of the LWJGL library suites. The rendering is done using OpenGL, using vertex-buffers, fragment and vertex shaders.

It uses the Power Voronoi Diagram library made by ArlindNocaj: https://github.com/ArlindNocaj/power-voronoi-diagram
As well as the imgui-java library, which makes bindings for the imgui library, made by SpaiR: https://github.com/SpaiR/imgui-java
The rest of the libraries are from the LWJGL suite of libararies: https://www.lwjgl.org/

## Background
This application was built for my Highschool Capstone Project, and was before I learned linear algebra (thus the terrifyingly messy equations for converting between screen and world coordinates).

## Release
WIP, I am currenlty trying to make the application work on both windows and linux machines.
