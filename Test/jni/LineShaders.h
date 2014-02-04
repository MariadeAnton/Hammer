/*==============================================================================
Copyright (c) 2010-2013 QUALCOMM Austria Research Center GmbH.
All Rights Reserved.

@file 
    LineShaders.h

@brief
    Defines OpenGL shaders as char* strings.

==============================================================================*/

#ifndef _QCAR_LINE_SHADERS_H_
#define _QCAR_LINE_SHADERS_H_

static const char* lineMeshVertexShader = " \
  \
attribute vec4 vertexPosition; \
 \
uniform mat4 modelViewProjectionMatrix; \
 \
void main() \
{ \
   gl_Position = modelViewProjectionMatrix * vertexPosition; \
} \
";


static const char* lineFragmentShader = " \
 \
precision mediump float; \
 \
void main() \
{ \
   gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0); \
} \
";

#endif // _QCAR_LINE_SHADERS_H_
