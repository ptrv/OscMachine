#ifndef _BALL_H
#define _BALL_H

#include "ofMain.h"
#include "ofxOsc.h"
#include "ofxVectorMath.h"

class ball : public ofxPoint2f{
	
	public:
	
		ball();
		
		void setup(float _x, float _y, float _xv, float _yv, 
					ofxOscSender _sOsc, int _s);	

		void update();
		
		void draw();
						
	private:

		void checkBoundaryCollision();
		void sendOscMessage();
			
		ofxVec2f	v;
		float		r;

		ofSoundPlayer	sound;
		ofxOscSender	senderOsc;
		int sample;
};

#endif