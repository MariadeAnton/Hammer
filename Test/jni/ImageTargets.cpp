/*==============================================================================
Copyright (c) 2010-2013 QUALCOMM Austria Research Center GmbH.
All Rights Reserved.

@file 
    ImageTargets.cpp

@brief
    Sample for ImageTargets

==============================================================================*/


#include <jni.h>
#include <android/log.h>
#include <stdio.h>
#include <string.h>
#include <assert.h>
#include <math.h>
#ifdef USE_OPENGL_ES_1_1
#include <GLES/gl.h>
#include <GLES/glext.h>
#else
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#endif

#include <QCAR/QCAR.h>
#include <QCAR/CameraDevice.h>
#include <QCAR/Renderer.h>
#include <QCAR/VideoBackgroundConfig.h>
#include <QCAR/Trackable.h>
#include <QCAR/TrackableResult.h>
#include <QCAR/Tool.h>
#include <QCAR/Tracker.h>
#include <QCAR/MarkerTracker.h>
#include <QCAR/TrackerManager.h>
#include <QCAR/ImageTracker.h>
#include <QCAR/CameraCalibration.h>
#include <QCAR/UpdateCallback.h>
#include <QCAR/DataSet.h>
#include <QCAR/VirtualButton.h>
#include <QCAR/VirtualButtonResult.h>
#include <QCAR/Area.h>
#include <QCAR/Rectangle.h>
#include <QCAR/ImageTargetResult.h>

#include "SampleUtils.h"
#include "Texture.h"
#include "LineShaders.h"
#include "CubeShaders.h"
#include "Teapot.h"
#include "Irb.h"
#include "SampleMath.h"

#ifdef __cplusplus
extern "C"
{
#endif

// Textures:
int textureCount                = 0;
Texture** textures              = 0;

// OpenGL ES 2.0 specific:
#ifdef USE_OPENGL_ES_2_0
unsigned int shaderProgramID    = 0;
GLint vertexHandle              = 0;
GLint normalHandle              = 0;
GLint textureCoordHandle        = 0;
GLint mvpMatrixHandle           = 0;
GLint texSampler2DHandle        = 0;
#endif

// Screen dimensions:
unsigned int screenWidth        = 0;
unsigned int screenHeight       = 0;

unsigned int distance=0;
bool teapotModel=false;
// Indicates whether screen is in portrait (true) or landscape (false) mode
bool isActivityInPortraitMode   = false;

// The projection matrix used for rendering virtual objects:
QCAR::Matrix44F projectionMatrix;

// Constants:
static const float kObjectScale = 3.f;

QCAR::DataSet* dataSetStonesAndChips    = 0;
QCAR::DataSet* dataSetTarmac            = 0;
QCAR::DataSet* dataSetWood				= 0;
QCAR::DataSet* dataSetKand				= 0;

enum BUTTONS
{
    BUTTON_1                    = 1,
    BUTTON_2                    = 2,
    BUTTON_3                    = 4,
    BUTTON_4                    = 8
};

int buttonMask                  = 0;

// Virtual Button runtime creation:
bool updateBtns                   = false;
const char* virtualButtonColors[] = {"red", "blue", "yellow", "green"};
const int NUM_BUTTONS             = 4;


// OpenGL ES 2.0 specific (Virtual Buttons):
unsigned int vbShaderProgramID  = 0;
GLint vbVertexHandle            = 0;


bool switchDataSetAsap          = false;

// Object to receive update callbacks from QCAR SDK
class ImageTargets_UpdateCallback : public QCAR::UpdateCallback
{
    virtual void QCAR_onUpdate(QCAR::State& /*state*/)
    {
        if (switchDataSetAsap)
        {
            switchDataSetAsap = false;

            // Get the image tracker:
            QCAR::TrackerManager& trackerManager = QCAR::TrackerManager::getInstance();
            QCAR::ImageTracker* imageTracker = static_cast<QCAR::ImageTracker*>(
                trackerManager.getTracker(QCAR::Tracker::IMAGE_TRACKER));




            if (imageTracker == 0 || dataSetStonesAndChips == 0 || dataSetTarmac == 0 || dataSetKand== 0 || dataSetWood == 0 ||
                imageTracker->getActiveDataSet() == 0)
            {
                LOG("Failed to switch data set.");
                return;
            }

            if (imageTracker->getActiveDataSet() == dataSetStonesAndChips)
            {
                imageTracker->deactivateDataSet(dataSetStonesAndChips);

                imageTracker->activateDataSet(dataSetWood);
            }
            if (imageTracker->getActiveDataSet() == dataSetKand)
                        {
                            imageTracker->deactivateDataSet(dataSetKand);

                            imageTracker->activateDataSet(dataSetWood);
                        }

            else if(imageTracker->getActiveDataSet() == dataSetWood)
            {
                imageTracker->deactivateDataSet(dataSetWood);
                imageTracker->activateDataSet(dataSetStonesAndChips);
                imageTracker->activateDataSet(dataSetKand);
            }
        }
    }
};

ImageTargets_UpdateCallback updateCallback;

JNIEXPORT int JNICALL
Java_com_test_ImageTargets_getOpenGlEsVersionNative(JNIEnv *, jobject)
{
#ifdef USE_OPENGL_ES_1_1        
    return 1;
#else
    return 2;
#endif
}


JNIEXPORT void JNICALL
Java_com_test_ImageTargets_setActivityPortraitMode(JNIEnv *, jobject, jboolean isPortrait)
{
    isActivityInPortraitMode = isPortrait;
}



JNIEXPORT void JNICALL
Java_com_test_ImageTargets_switchDatasetAsap(JNIEnv *, jobject)
{
    switchDataSetAsap = true;
}


JNIEXPORT int JNICALL
Java_com_test_ImageTargets_initTracker(JNIEnv *, jobject)
{
    LOG("Java_com_qualcomm_QCARSamples_ImageTargets_ImageTargets_initTracker");
    
    // Initialize the image tracker:
    QCAR::TrackerManager& trackerManager = QCAR::TrackerManager::getInstance();
    QCAR::Tracker* tracker = trackerManager.initTracker(QCAR::Tracker::IMAGE_TRACKER);


    QCAR::Tracker* trackerBase = trackerManager.initTracker(QCAR::Tracker::MARKER_TRACKER);
    QCAR::MarkerTracker* markerTracker = static_cast<QCAR::MarkerTracker*>(trackerBase);
      if (markerTracker == NULL)
      {
          LOG("Failed to initialize MarkerTracker.");
          return 0;
      }

    if (!markerTracker->createFrameMarker(0, "Path", QCAR::Vec2F(200,200)))
    {
          LOG("Failed to create frame marker Path.");
    }

    if (tracker == NULL)
    {
        LOG("Failed to initialize ImageTracker.");
        return 0;
    }

    LOG("Successfully initialized ImageTracker.");
    return 1;
}


JNIEXPORT void JNICALL
Java_com_test_ImageTargets_deinitTracker(JNIEnv *, jobject)
{
    LOG("Java_com_qualcomm_QCARSamples_ImageTargets_ImageTargets_deinitTracker");

    // Deinit the image tracker:
    QCAR::TrackerManager& trackerManager = QCAR::TrackerManager::getInstance();
    trackerManager.deinitTracker(QCAR::Tracker::IMAGE_TRACKER);
}


JNIEXPORT int JNICALL
Java_com_test_ImageTargets_loadTrackerData(JNIEnv *, jobject)
{
    LOG("Java_com_qualcomm_QCARSamples_ImageTargets_ImageTargets_loadTrackerData");
    
    // Get the image tracker:
    QCAR::TrackerManager& trackerManager = QCAR::TrackerManager::getInstance();
    QCAR::ImageTracker* imageTracker = static_cast<QCAR::ImageTracker*>(
                    trackerManager.getTracker(QCAR::Tracker::IMAGE_TRACKER));
    if (imageTracker == NULL)
    {
        LOG("Failed to load tracking data set because the ImageTracker has not"
            " been initialized.");
        return 0;
    }

    dataSetWood = imageTracker->createDataSet();
     if (dataSetWood == 0)
     {
           LOG("Failed to create a new tracking data.");
           return 0;
     }

     dataSetKand = imageTracker->createDataSet();
         if (dataSetKand == 0)
         {
               LOG("Failed to create a new tracking data.");
               return 0;
         }


    // Create the data sets:
    dataSetStonesAndChips = imageTracker->createDataSet();
    if (dataSetStonesAndChips == 0)
    {
        LOG("Failed to create a new tracking data.");
        return 0;
    }

    dataSetTarmac = imageTracker->createDataSet();
    if (dataSetTarmac == 0)
    {
        LOG("Failed to create a new tracking data.");
        return 0;
    }

    // Load the data sets:
    if (!dataSetStonesAndChips->load("StonesAndChips.xml", QCAR::DataSet::STORAGE_APPRESOURCE))
    {
        LOG("Failed to load data set.");
        return 0;
    }

    if (!dataSetWood->load("Wood.xml", QCAR::DataSet::STORAGE_APPRESOURCE))
       {
           LOG("Failed to load data set.");
           return 0;
       }

    if (!dataSetKand->load("Kand.xml", QCAR::DataSet::STORAGE_APPRESOURCE))
          {
              LOG("Failed to load data set.");
              return 0;
          }



    if (!dataSetTarmac->load("Color.xml", QCAR::DataSet::STORAGE_APPRESOURCE))
    {
        LOG("Failed to load data set.");
        return 0;
    }

    // Activate the data set:
    if (!imageTracker->activateDataSet(dataSetWood))
    {
        LOG("Failed to activate data set.");
        return 0;
    }

    if (!imageTracker->activateDataSet(dataSetStonesAndChips))
        {
            LOG("Failed to activate data set.");
            return 0;
        }

    if (!imageTracker->activateDataSet(dataSetKand))
            {
                LOG("Failed to activate data set.");
                return 0;
            }

    if (!imageTracker->activateDataSet(dataSetTarmac))
      {
          LOG("Failed to activate data set.");
          return 0;
      }



    LOG("Successfully loaded and activated data set.");
    return 1;
}


JNIEXPORT int JNICALL
Java_com_test_ImageTargets_destroyTrackerData(JNIEnv *, jobject)
{
    LOG("Java_com_qualcomm_QCARSamples_ImageTargets_ImageTargets_destroyTrackerData");

    // Get the image tracker:
    QCAR::TrackerManager& trackerManager = QCAR::TrackerManager::getInstance();
    QCAR::ImageTracker* imageTracker = static_cast<QCAR::ImageTracker*>(
        trackerManager.getTracker(QCAR::Tracker::IMAGE_TRACKER));
    if (imageTracker == NULL)
    {
        LOG("Failed to destroy the tracking data set because the ImageTracker has not"
            " been initialized.");
        return 0;
    }
    
    if (dataSetStonesAndChips != 0)
    {
        if (imageTracker->getActiveDataSet() == dataSetStonesAndChips &&
            !imageTracker->deactivateDataSet(dataSetStonesAndChips))
        {
            LOG("Failed to destroy the tracking data set StonesAndChips because the data set "
                "could not be deactivated.");
            return 0;
        }

        if (!imageTracker->destroyDataSet(dataSetStonesAndChips))
        {
            LOG("Failed to destroy the tracking data set StonesAndChips.");
            return 0;
        }

        LOG("Successfully destroyed the data set StonesAndChips.");
        dataSetStonesAndChips = 0;
    }

    if (dataSetKand != 0)
    {
        if (imageTracker->getActiveDataSet() == dataSetKand &&
            !imageTracker->deactivateDataSet(dataSetKand))
        {
            LOG("Failed to destroy the tracking data set StonesAndChips because the data set "
                "could not be deactivated.");
            return 0;
        }

        if (!imageTracker->destroyDataSet(dataSetKand))
        {
            LOG("Failed to destroy the tracking data set StonesAndChips.");
            return 0;
        }

        LOG("Successfully destroyed the data set StonesAndChips.");
        dataSetKand = 0;
    }

    if (dataSetTarmac != 0)
    {
        if (imageTracker->getActiveDataSet() == dataSetTarmac &&
            !imageTracker->deactivateDataSet(dataSetTarmac))
        {
            LOG("Failed to destroy the tracking data set Tarmac because the data set "
                "could not be deactivated.");
            return 0;
        }

        if (!imageTracker->destroyDataSet(dataSetTarmac))
        {
            LOG("Failed to destroy the tracking data set Tarmac.");
            return 0;
        }

        LOG("Successfully destroyed the data set Tarmac.");
        dataSetTarmac = 0;
    }

    if (dataSetWood != 0)
       {
           if (imageTracker->getActiveDataSet() == dataSetWood &&
               !imageTracker->deactivateDataSet(dataSetWood))
           {
               LOG("Failed to destroy the tracking data set Wood because the data set "
                   "could not be deactivated.");
               return 0;
           }

           if (!imageTracker->destroyDataSet(dataSetWood))
           {
               LOG("Failed to destroy the tracking data set Wood.");
               return 0;
           }

           LOG("Successfully destroyed the data set Wood.");
           dataSetWood = 0;
       }

    return 1;
}


JNIEXPORT void JNICALL
Java_com_test_ImageTargets_onQCARInitializedNative(JNIEnv *, jobject)
{
    // Register the update callback where we handle the data set swap:
    QCAR::registerCallback(&updateCallback);

    // Comment in to enable tracking of up to 2 targets simultaneously and
    // split the work over multiple frames:
    // QCAR::setHint(QCAR::HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 2);
}

JNIEXPORT void JNICALL
Java_com_ImageTargets_addButtonToToggle(JNIEnv */*env*/, jobject /*obj*/, jint virtualButtonIdx)
{
    LOG("Java_com_qualcomm_QCARSamples_VirtualButtons_VirtualButtons_addButtonToToggle");

    assert(virtualButtonIdx >= 0 && virtualButtonIdx < NUM_BUTTONS);

    switch (virtualButtonIdx)
    {
        case 0:
            buttonMask |= BUTTON_1;
            break;

        case 1:
            buttonMask |= BUTTON_2;
            break;

        case 2:
            buttonMask |= BUTTON_3;
            break;

        case 3:
            buttonMask |= BUTTON_4;
            break;
    }
    updateBtns = true;
}

bool
toggleVirtualButton(QCAR::ImageTarget* imageTarget, const char* name,
                    float left, float top, float right, float bottom)
{
    LOG("toggleVirtualButton");

    bool buttonToggleSuccess = false;

    QCAR::VirtualButton* virtualButton = imageTarget->getVirtualButton(name);
    if (virtualButton != NULL)
    {
        LOG("Destroying Virtual Button");
        buttonToggleSuccess = imageTarget->destroyVirtualButton(virtualButton);
    }
    else
    {
        LOG("Creating Virtual Button");
        QCAR::Rectangle vbRectangle(left, top, right, bottom);
        QCAR::VirtualButton* virtualButton = imageTarget->createVirtualButton(name, vbRectangle);

        if (virtualButton != NULL)
        {
            // This is just a showcase. The values used here a set by default on Virtual Button creation
            virtualButton->setEnabled(true);
            virtualButton->setSensitivity(QCAR::VirtualButton::MEDIUM);
            buttonToggleSuccess = true;
        }
    }

    return buttonToggleSuccess;
}


// Object to receive update callbacks from QCAR SDK
class VirtualButton_UpdateCallback : public QCAR::UpdateCallback
{
    virtual void QCAR_onUpdate(QCAR::State& /*state*/)
    {
        if (updateBtns)
        {
            // Update runs in the tracking thread therefore it is guaranteed that the tracker is
            // not doing anything at this point. => Reconfiguration is possible.

            QCAR::ImageTracker* it = reinterpret_cast<QCAR::ImageTracker*>(
                QCAR::TrackerManager::getInstance().getTracker(QCAR::Tracker::IMAGE_TRACKER));
            assert(dataSetWood);

            // Deactivate the data set prior to reconfiguration:
            it->deactivateDataSet(dataSetWood);

            assert(dataSet->getNumTrackables() > 0);
            QCAR::Trackable* trackable = dataSetWood->getTrackable(0);

            assert(trackable);
            assert(trackable->getType() == QCAR::Trackable::IMAGE_TARGET);




            QCAR::ImageTarget* imageTarget = static_cast<QCAR::ImageTarget*>(trackable);


            if (buttonMask & BUTTON_1)
            {
                LOG("Toggle Button 1");

                toggleVirtualButton(imageTarget, virtualButtonColors[0],
                                    -108.68f, -53.52f, -75.75f, -65.87f);

            }
            if (buttonMask & BUTTON_2)
            {
                LOG("Toggle Button 2");

                toggleVirtualButton(imageTarget, virtualButtonColors[1],
                                    -45.28f, -53.52f, -12.35f, -65.87f);
            }
            if (buttonMask & BUTTON_3)
            {
                LOG("Toggle Button 3");

                toggleVirtualButton(imageTarget, virtualButtonColors[2],
                                    14.82f, -53.52f, 47.75f, -65.87f);
            }
            if (buttonMask & BUTTON_4)
            {
                LOG("Toggle Button 4");

                toggleVirtualButton(imageTarget, virtualButtonColors[3],
                                    76.57f, -53.52f, 109.50f, -65.87f);
            }

            // Reactivate the data set:
            it->activateDataSet(dataSetWood);

            buttonMask = 0;
            updateBtns = false;
        }
    }
} qcarUpdate;


JNIEXPORT void JNICALL
Java_com_test_ImageTargetsRenderer_renderFrame(JNIEnv *env, jobject obj)
{

	if(teapotModel)
	{
		jclass activityClass = env->GetObjectClass(obj);
		jmethodID renderJPCT = env->GetMethodID(activityClass, "renderJPCTModels", "(Z)V");
		env->CallVoidMethod(obj, renderJPCT , false);

	//LOG("Java_com_qualcomm_QCARSamples_ImageTargets_GLRenderer_renderFrame");

    // Clear color and depth buffer 
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    // Get the state from QCAR and mark the beginning of a rendering section
    QCAR::State state = QCAR::Renderer::getInstance().begin();
    
    // Explicitly render the Video Background
    QCAR::Renderer::getInstance().drawVideoBackground();
       
#ifdef USE_OPENGL_ES_1_1
    // Set GL11 flags:
    glEnableClientState(GL_VERTEX_ARRAY);
    glEnableClientState(GL_NORMAL_ARRAY);
    glEnableClientState(GL_TEXTURE_COORD_ARRAY);

    glEnable(GL_TEXTURE_2D);
    glDisable(GL_LIGHTING);
        
#endif

    glEnable(GL_DEPTH_TEST);

    // We must detect if background reflection is active and adjust the culling direction. 
    // If the reflection is active, this means the post matrix has been reflected as well,
    // therefore standard counter clockwise face culling will result in "inside out" models. 
    glEnable(GL_CULL_FACE);
    glCullFace(GL_BACK);
    if(QCAR::Renderer::getInstance().getVideoBackgroundConfig().mReflection == QCAR::VIDEO_BACKGROUND_REFLECTION_ON)
        glFrontFace(GL_CW);  //Front camera
    else
        glFrontFace(GL_CCW);   //Back camera


    // Did we find any trackables this frame?
    for(int tIdx = 0; tIdx < state.getNumTrackableResults(); tIdx++)
    {
        // Get the trackable:
        const QCAR::TrackableResult* trackableResult = state.getTrackableResult(tIdx);
        const QCAR::Trackable& trackable = trackableResult->getTrackable();
        QCAR::Matrix44F modelViewMatrix =
            QCAR::Tool::convertPose2GLMatrix(trackableResult->getPose());






              assert(trackableResult->getType() == QCAR::TrackableResult::IMAGE_TARGET_RESULT);
              const QCAR::ImageTargetResult* imageTargetResult =
                  static_cast<const QCAR::ImageTargetResult*>(trackableResult);

              // Set the texture used for the teapot model:
               int textureIndex = 0;
               GLfloat vbVertices[imageTargetResult->getNumVirtualButtons()*24];
               unsigned char vbCounter=0;

               	   	   if (strcmp(trackable.getName(), "chips") == 0)
                      {
                          textureIndex = 0;
                      }
                      else if (strcmp(trackable.getName(), "stones") == 0)
                      {
                          textureIndex = 1;
                      }
                      else if(strcmp(trackable.getName(), "kand") == 0)
                      {
                          textureIndex = 2;
                      }
                      else if(strcmp(trackable.getName(), "wood") == 0)
                      {



                    	  	 // Iterate through this targets virtual buttons:
                    	  	 for (int i = 0; i < imageTargetResult->getNumVirtualButtons(); ++i)
                    	  	 {
                    	  		 const QCAR::VirtualButtonResult* buttonResult = imageTargetResult->getVirtualButtonResult(i);
                    	  		 const QCAR::VirtualButton& button = buttonResult->getVirtualButton();

                    	  		 // If the button is pressed, than use this texture:
                    	  		 if (buttonResult->isPressed())
                    	  		 {
                    	  			 // Run through button name array to find texture index
                    	  			 for (int j = 0; j < NUM_BUTTONS; ++j)
                    	  			 {
                    	  				 if (strcmp(button.getName(), virtualButtonColors[j]) == 0)
                    	  				 {
                    	  					 textureIndex = j+1;
                    	  					 break;
                    	  				 }
                    	  			 }
                    	  		 }

                    	  		 const QCAR::Area* vbArea = &button.getArea();
                    	  		 assert(vbArea->getType() == QCAR::Area::RECTANGLE);
                    	  		 const QCAR::Rectangle* vbRectangle = static_cast<const QCAR::Rectangle*>(vbArea);


                    	  		 // We add the vertices to a common array in order to have one single
                    	  		 // draw call. This is more efficient than having multiple glDrawArray calls
                    	  		 vbVertices[vbCounter   ]=vbRectangle->getLeftTopX();
                    	  		 vbVertices[vbCounter+ 1]=vbRectangle->getLeftTopY();
                    	  		 vbVertices[vbCounter+ 2]=0.0f;
                    	  		 vbVertices[vbCounter+ 3]=vbRectangle->getRightBottomX();
                    	  		 vbVertices[vbCounter+ 4]=vbRectangle->getLeftTopY();
                    	  		 vbVertices[vbCounter+ 5]=0.0f;
                    	  		 vbVertices[vbCounter+ 6]=vbRectangle->getRightBottomX();
                    	  		 vbVertices[vbCounter+ 7]=vbRectangle->getLeftTopY();
                    	  		 vbVertices[vbCounter+ 8]=0.0f;
                    	  		 vbVertices[vbCounter+ 9]=vbRectangle->getRightBottomX();
                    	  		 vbVertices[vbCounter+10]=vbRectangle->getRightBottomY();
                    	  		 vbVertices[vbCounter+11]=0.0f;
                    	  		 vbVertices[vbCounter+12]=vbRectangle->getRightBottomX();
                    	  		 vbVertices[vbCounter+13]=vbRectangle->getRightBottomY();
                    	  		 vbVertices[vbCounter+14]=0.0f;
                    	  		 vbVertices[vbCounter+15]=vbRectangle->getLeftTopX();
                    	  		 vbVertices[vbCounter+16]=vbRectangle->getRightBottomY();
                    	  		 vbVertices[vbCounter+17]=0.0f;
                    	  		 vbVertices[vbCounter+18]=vbRectangle->getLeftTopX();
                    	  		 vbVertices[vbCounter+19]=vbRectangle->getRightBottomY();
                    	  		 vbVertices[vbCounter+20]=0.0f;
                    	  		 vbVertices[vbCounter+21]=vbRectangle->getLeftTopX();
                    	  		 vbVertices[vbCounter+22]=vbRectangle->getLeftTopY();
                    	  		 vbVertices[vbCounter+23]=0.0f;
                    	  		 vbCounter+=24;

                    	  	 }



                      	 }




        const Texture* const thisTexture = textures[textureIndex];

#ifdef USE_OPENGL_ES_1_1
        // Load projection matrix:
        glMatrixMode(GL_PROJECTION);
        glLoadMatrixf(projectionMatrix.data);

        // Load model view matrix:
        glMatrixMode(GL_MODELVIEW);
        glLoadMatrixf(modelViewMatrix.data);
        glTranslatef(0.f, 0.f, kObjectScale);
        glScalef(kObjectScale, kObjectScale, kObjectScale);

        // Draw object:
        glBindTexture(GL_TEXTURE_2D, thisTexture->mTextureID);
        glTexCoordPointer(2, GL_FLOAT, 0, (const GLvoid*) &teapotTexCoords[0]);
        glVertexPointer(3, GL_FLOAT, 0, (const GLvoid*) &teapotVertices[0]);
        glNormalPointer(GL_FLOAT, 0,  (const GLvoid*) &teapotNormals[0]);
        glDrawElements(GL_TRIANGLES, NUM_TEAPOT_OBJECT_INDEX, GL_UNSIGNED_SHORT,
                       (const GLvoid*) &teapotIndices[0]);
#else

        QCAR::Matrix44F modelViewProjection;

        SampleUtils::translatePoseMatrix(0.0f, 0.0f, distance,
                                         &modelViewMatrix.data[0]);
        SampleUtils::scalePoseMatrix(kObjectScale, kObjectScale, kObjectScale,
                                     &modelViewMatrix.data[0]);
        SampleUtils::multiplyMatrix(&projectionMatrix.data[0],
                                    &modelViewMatrix.data[0] ,
                                    &modelViewProjection.data[0]);

        glUseProgram(shaderProgramID);

        glVertexAttribPointer(vertexHandle, 3, GL_FLOAT, GL_FALSE, 0,
                              (const GLvoid*) &teapotVertices[0]);
        glVertexAttribPointer(normalHandle, 3, GL_FLOAT, GL_FALSE, 0,
                              (const GLvoid*) &teapotNormals[0]);
        glVertexAttribPointer(textureCoordHandle, 2, GL_FLOAT, GL_FALSE, 0,
                              (const GLvoid*) &teapotTexCoords[0]);
        
        glEnableVertexAttribArray(vertexHandle);
        glEnableVertexAttribArray(normalHandle);
        glEnableVertexAttribArray(textureCoordHandle);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, thisTexture->mTextureID);
        glUniform1i(texSampler2DHandle, 0 );

        glUniformMatrix4fv(mvpMatrixHandle, 1, GL_FALSE,
                           (GLfloat*)&modelViewProjection.data[0] );
        glDrawElements(GL_TRIANGLES, NUM_TEAPOT_OBJECT_INDEX, GL_UNSIGNED_SHORT,
                       (const GLvoid*) &teapotIndices[0]);

        glDisableVertexAttribArray(vertexHandle);
        glDisableVertexAttribArray(normalHandle);
        glDisableVertexAttribArray(textureCoordHandle);


if (vbCounter>0)
        {
                           	              // Render frame around button
                  glUseProgram(vbShaderProgramID);

                  glVertexAttribPointer(vbVertexHandle, 3, GL_FLOAT, GL_FALSE, 0,
                           	                  (const GLvoid*) &vbVertices[0]);

                  glEnableVertexAttribArray(vbVertexHandle);

                  glUniformMatrix4fv(mvpMatrixHandle, 1, GL_FALSE,
                           	                  (GLfloat*)&modelViewProjection.data[0] );

                           	              // We multiply by 8 because that's the number of vertices per button
                           	              // The reason is that GL_LINES considers only pairs. So some vertices
                  glDrawArrays(GL_LINES, 0, imageTargetResult->getNumVirtualButtons()*8);

                  SampleUtils::checkGlError("VirtualButtons drawButton");

                  glDisableVertexAttribArray(vbVertexHandle);
          }


        SampleUtils::checkGlError("ImageTargets renderFrame");
#endif

    }




    glDisable(GL_DEPTH_TEST);

#ifdef USE_OPENGL_ES_1_1        
    glDisable(GL_TEXTURE_2D);
    glDisableClientState(GL_VERTEX_ARRAY);
    glDisableClientState(GL_NORMAL_ARRAY);
    glDisableClientState(GL_TEXTURE_COORD_ARRAY);
#endif

   }

	else
	{


	const QCAR::CameraCalibration& cameraCalibration =       QCAR::CameraDevice::getInstance().getCameraCalibration();
	QCAR::Vec2F size = cameraCalibration.getSize();
	QCAR::Vec2F focalLength = cameraCalibration.getFocalLength();
	float fovyRadians = 2 * atan(0.5f * size.data[1] / focalLength.data[1]);
	float fovRadians = 2 * atan(0.5f * size.data[0] / focalLength.data[0]);

	jclass activityClass = env->GetObjectClass(obj);
	jfloatArray modelviewArray = env->NewFloatArray(16);
	jmethodID updateMatrixMethod = env->GetMethodID(activityClass, "updateModelviewMatrix",    "([F)V");

	jmethodID fovMethod = env->GetMethodID(activityClass, "setFov", "(F)V");
	jmethodID fovyMethod = env->GetMethodID(activityClass, "setFovy", "(F)V");



	// test
	jclass newClass = env->GetObjectClass(obj);
	jmethodID updateCameraMethod = env->GetMethodID(newClass, "updateCamera", "()V");

	// Get the state from QCAR and mark the beginning of a rendering section
	QCAR::State state = QCAR::Renderer::getInstance().begin();
	// Explicitly render the Video Background
	QCAR::Renderer::getInstance().drawVideoBackground();
	// Did we find any trackables this frame?

	jmethodID renderJPCT = env->GetMethodID(activityClass, "renderJPCTModels", "(Z)V");

	if(state.getNumTrackableResults()<=0)
	{
		env->CallVoidMethod(obj, renderJPCT , false);
	}

	else
	{
		env->CallVoidMethod(obj, renderJPCT , true);
		for(int tIdx = 0; tIdx < state.getNumTrackableResults(); tIdx++)
		{
			// Get the trackable:
			const QCAR::TrackableResult* result = state.getTrackableResult(tIdx);
			const QCAR::Trackable& trackable = result->getTrackable();
			QCAR::Matrix44F modelViewMatrix = QCAR::Tool::convertPose2GLMatrix(result->getPose());

		     assert(trackableResult->getType() == QCAR::TrackableResult::MARKER_RESULT);


			QCAR::Matrix44F inverseMV = SampleMath::Matrix44FInverse(modelViewMatrix);
			QCAR::Matrix44F invTranspMV = SampleMath::Matrix44FTranspose(inverseMV);

				//Camera position
				float cam_x = invTranspMV.data[12];
				float cam_y = invTranspMV.data[13];
				float cam_z = invTranspMV.data[14];

				//Camera orientation axis (camera viewing direction, camera right direction and camera up direction)
				float cam_right_x = invTranspMV.data[0];
				float cam_right_y = invTranspMV.data[1];
				float cam_right_z = invTranspMV.data[2];

				float cam_up_x = -invTranspMV.data[4];
				float cam_up_y = -invTranspMV.data[5];
				float cam_up_z = -invTranspMV.data[6];

				float cam_dir_x = invTranspMV.data[8];
				float cam_dir_y = invTranspMV.data[9];
				float cam_dir_z = invTranspMV.data[10];



			jmethodID setCamVectors = env->GetMethodID(activityClass, "setCamVectors", "([F)V");

	//		SampleUtils::rotatePoseMatrix(180.0f, 1.0f, 0, 0, &modelViewMatrix.data[0]);
			SampleUtils::translatePoseMatrix(0.0f, 0.0f, distance, &modelViewMatrix.data[0]);

			env->SetFloatArrayRegion(modelviewArray, 0, 16, modelViewMatrix.data);

			// Passes the model view matrix to java
			jfloatArray dir = env->NewFloatArray(16);
			env->SetFloatArrayRegion(dir, 0, 16, invTranspMV.data);

			env->CallVoidMethod(obj, setCamVectors, dir);
			env->CallVoidMethod(obj, fovyMethod, fovyRadians);
			env->CallVoidMethod(obj, fovMethod, fovRadians);
			env->CallVoidMethod(obj, updateCameraMethod);
			env->CallVoidMethod(obj, updateMatrixMethod , modelviewArray);

		}
	}

	QCAR::Renderer::getInstance().end();
	env->DeleteLocalRef(modelviewArray);
	}




	////////////////////////////////////////////////////////////////////////////////////////


	     ////////////////////////////////////////////////////////////////////////////////////////////////////
}


void
configureVideoBackground()
{
    // Get the default video mode:
    QCAR::CameraDevice& cameraDevice = QCAR::CameraDevice::getInstance();
    QCAR::VideoMode videoMode = cameraDevice.
                                getVideoMode(QCAR::CameraDevice::MODE_DEFAULT);


    // Configure the video background
    QCAR::VideoBackgroundConfig config;
    config.mEnabled = true;
    config.mSynchronous = true;
    config.mPosition.data[0] = 0.0f;
    config.mPosition.data[1] = 0.0f;
    
    if (isActivityInPortraitMode)
    {
        //LOG("configureVideoBackground PORTRAIT");
        config.mSize.data[0] = videoMode.mHeight
                                * (screenHeight / (float)videoMode.mWidth);
        config.mSize.data[1] = screenHeight;

        if(config.mSize.data[0] < screenWidth)
        {
            LOG("Correcting rendering background size to handle missmatch between screen and video aspect ratios.");
            config.mSize.data[0] = screenWidth;
            config.mSize.data[1] = screenWidth * 
                              (videoMode.mWidth / (float)videoMode.mHeight);
        }
    }
    else
    {
        //LOG("configureVideoBackground LANDSCAPE");
        config.mSize.data[0] = screenWidth;
        config.mSize.data[1] = videoMode.mHeight
                            * (screenWidth / (float)videoMode.mWidth);

        if(config.mSize.data[1] < screenHeight)
        {
            LOG("Correcting rendering background size to handle missmatch between screen and video aspect ratios.");
            config.mSize.data[0] = screenHeight
                                * (videoMode.mWidth / (float)videoMode.mHeight);
            config.mSize.data[1] = screenHeight;
        }
    }

    LOG("Configure Video Background : Video (%d,%d), Screen (%d,%d), mSize (%d,%d)", videoMode.mWidth, videoMode.mHeight, screenWidth, screenHeight, config.mSize.data[0], config.mSize.data[1]);

    // Set the config:
    QCAR::Renderer::getInstance().setVideoBackgroundConfig(config);
}
JNIEXPORT void JNICALL
Java_com_test_ImageTargetsRenderer_showTeapot(
                            JNIEnv* env, jobject obj, jboolean teapot)
{
	teapotModel=teapot;
}

JNIEXPORT void JNICALL
Java_com_test_ImageTargets_increaseDistance(
                            JNIEnv* env, jobject obj)
{
	if(distance==1500)
		distance=0;
	else
		distance+=500;
}

JNIEXPORT void JNICALL
Java_com_test_ImageTargets_initApplicationNative(
                            JNIEnv* env, jobject obj, jint width, jint height)
{
    LOG("Java_com_qualcomm_QCARSamples_ImageTargets_ImageTargets_initApplicationNative");
    
    // Store screen dimensions
    screenWidth = width;
    screenHeight = height;
        
    // Handle to the activity class:
    jclass activityClass = env->GetObjectClass(obj);

    jmethodID getTextureCountMethodID = env->GetMethodID(activityClass,
                                                    "getTextureCount", "()I");
    if (getTextureCountMethodID == 0)
    {
        LOG("Function getTextureCount() not found.");
        return;
    }

    textureCount = env->CallIntMethod(obj, getTextureCountMethodID);    
    if (!textureCount)
    {
        LOG("getTextureCount() returned zero.");
        return;
    }

    textures = new Texture*[textureCount];

    jmethodID getTextureMethodID = env->GetMethodID(activityClass,
        "getTexture", "(I)Lcom/test/Texture;");

    if (getTextureMethodID == 0)
    {
        LOG("Function getTexture() not found.");
        return;
    }

    // Register the textures
    for (int i = 0; i < textureCount; ++i)
    {

        jobject textureObject = env->CallObjectMethod(obj, getTextureMethodID, i); 
        if (textureObject == NULL)
        {
            LOG("GetTexture() returned zero pointer");
            return;
        }

        textures[i] = Texture::create(env, textureObject);
    }
    LOG("Java_com_qualcomm_QCARSamples_ImageTargets_ImageTargets_initApplicationNative finished");
}


JNIEXPORT void JNICALL
Java_com_test_ImageTargets_deinitApplicationNative(
                                                        JNIEnv* env, jobject obj)
{
    LOG("Java_com_qualcomm_QCARSamples_ImageTargets_ImageTargets_deinitApplicationNative");

    // Release texture resources
    if (textures != 0)
    {    
        for (int i = 0; i < textureCount; ++i)
        {
            delete textures[i];
            textures[i] = NULL;
        }
    
        delete[]textures;
        textures = NULL;
        
        textureCount = 0;
    }
}


JNIEXPORT void JNICALL
Java_com_test_ImageTargets_startCamera(JNIEnv *,
                                                                         jobject)
{
    LOG("Java_com_qualcomm_QCARSamples_ImageTargets_ImageTargets_startCamera");

    // Select the camera to open, set this to QCAR::CameraDevice::CAMERA_FRONT 
    // to activate the front camera instead.
    QCAR::CameraDevice::CAMERA camera = QCAR::CameraDevice::CAMERA_DEFAULT;

    // Initialize the camera:
    if (!QCAR::CameraDevice::getInstance().init(camera))
        return;

    // Configure the video background
    configureVideoBackground();

    // Select the default mode:
    if (!QCAR::CameraDevice::getInstance().selectVideoMode(
                                QCAR::CameraDevice::MODE_DEFAULT))
        return;

    // Start the camera:
    if (!QCAR::CameraDevice::getInstance().start())
        return;

    // Uncomment to enable flash
    //if(QCAR::CameraDevice::getInstance().setFlashTorchMode(true))
    //    LOG("IMAGE TARGETS : enabled torch");

    // Uncomment to enable infinity focus mode, or any other supported focus mode
    // See CameraDevice.h for supported focus modes
    //if(QCAR::CameraDevice::getInstance().setFocusMode(QCAR::CameraDevice::FOCUS_MODE_INFINITY))
    //    LOG("IMAGE TARGETS : enabled infinity focus");

    // Start the tracker:
    QCAR::TrackerManager& trackerManager = QCAR::TrackerManager::getInstance();
    QCAR::Tracker* imageTracker = trackerManager.getTracker(QCAR::Tracker::IMAGE_TRACKER);
    if(imageTracker != 0)
        imageTracker->start();
    QCAR::Tracker* markerTracker = trackerManager.getTracker(QCAR::Tracker::MARKER_TRACKER);
      if(markerTracker != 0)
          markerTracker->start();
}


JNIEXPORT void JNICALL
Java_com_test_ImageTargets_stopCamera(JNIEnv *, jobject)
{
    LOG("Java_com_qualcomm_QCARSamples_ImageTargets_ImageTargets_stopCamera");

    // Stop the tracker:
    QCAR::TrackerManager& trackerManager = QCAR::TrackerManager::getInstance();
    QCAR::Tracker* imageTracker = trackerManager.getTracker(QCAR::Tracker::IMAGE_TRACKER);
    if(imageTracker != 0)
        imageTracker->stop();
    
    QCAR::CameraDevice::getInstance().stop();
    QCAR::CameraDevice::getInstance().deinit();
}


JNIEXPORT void JNICALL
Java_com_test_ImageTargets_setProjectionMatrix(JNIEnv *, jobject)
{
    LOG("Java_com_qualcomm_QCARSamples_ImageTargets_ImageTargets_setProjectionMatrix");

    // Cache the projection matrix:
    const QCAR::CameraCalibration& cameraCalibration =
                                QCAR::CameraDevice::getInstance().getCameraCalibration();
    projectionMatrix = QCAR::Tool::getProjectionGL(cameraCalibration, 2.0f, 2500.0f);
}

// ----------------------------------------------------------------------------
// Activates Camera Flash
// ----------------------------------------------------------------------------
JNIEXPORT jboolean JNICALL
Java_com_test_ImageTargets_activateFlash(JNIEnv*, jobject, jboolean flash)
{
    return QCAR::CameraDevice::getInstance().setFlashTorchMode((flash==JNI_TRUE)) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL
Java_com_test_ImageTargets_autofocus(JNIEnv*, jobject)
{
    return QCAR::CameraDevice::getInstance().setFocusMode(QCAR::CameraDevice::FOCUS_MODE_TRIGGERAUTO) ? JNI_TRUE : JNI_FALSE;
}


JNIEXPORT jboolean JNICALL
Java_com_test_ImageTargets_setFocusMode(JNIEnv*, jobject, jint mode)
{
    int qcarFocusMode;

    switch ((int)mode)
    {
        case 0:
            qcarFocusMode = QCAR::CameraDevice::FOCUS_MODE_NORMAL;
            break;
        
        case 1:
            qcarFocusMode = QCAR::CameraDevice::FOCUS_MODE_CONTINUOUSAUTO;
            break;
            
        case 2:
            qcarFocusMode = QCAR::CameraDevice::FOCUS_MODE_INFINITY;
            break;
            
        case 3:
            qcarFocusMode = QCAR::CameraDevice::FOCUS_MODE_MACRO;
            break;
    
        default:
            return JNI_FALSE;
    }
    
    return QCAR::CameraDevice::getInstance().setFocusMode(qcarFocusMode) ? JNI_TRUE : JNI_FALSE;
}


JNIEXPORT void JNICALL
Java_com_test_ImageTargetsRenderer_initRendering(
                                                    JNIEnv* env, jobject obj)
{
    LOG("Java_com_qualcomm_QCARSamples_ImageTargets_ImageTargetsRenderer_initRendering");

    // Define clear color
    glClearColor(0.0f, 0.0f, 0.0f, QCAR::requiresAlpha() ? 0.0f : 1.0f);
    
    // Now generate the OpenGL texture objects and add settings
    for (int i = 0; i < textureCount; ++i)
    {
        glGenTextures(1, &(textures[i]->mTextureID));
        glBindTexture(GL_TEXTURE_2D, textures[i]->mTextureID);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, textures[i]->mWidth,
                textures[i]->mHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE,
                (GLvoid*)  textures[i]->mData);
    }
#ifndef USE_OPENGL_ES_1_1
  
    shaderProgramID     = SampleUtils::createProgramFromBuffer(cubeMeshVertexShader,
                                                            cubeFragmentShader);

    vertexHandle        = glGetAttribLocation(shaderProgramID,
                                                "vertexPosition");
    normalHandle        = glGetAttribLocation(shaderProgramID,
                                                "vertexNormal");
    textureCoordHandle  = glGetAttribLocation(shaderProgramID,
                                                "vertexTexCoord");
    mvpMatrixHandle     = glGetUniformLocation(shaderProgramID,
                                                "modelViewProjectionMatrix");
    texSampler2DHandle  = glGetUniformLocation(shaderProgramID, 
                                                "texSampler2D");
                                                
#endif

}


JNIEXPORT void JNICALL
Java_com_test_ImageTargetsRenderer_updateRendering(
                        JNIEnv* env, jobject obj, jint width, jint height)
{
    LOG("Java_com_qualcomm_QCARSamples_ImageTargets_ImageTargetsRenderer_updateRendering");

    // Update screen dimensions
    screenWidth = width;
    screenHeight = height;

    // Reconfigure the video background
    configureVideoBackground();
}

#ifdef __cplusplus
}
#endif
