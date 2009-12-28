#include "ball.h"

ball::ball(){
	v.set(0, 0);

	sound.loadSound("sounds/1.mp3", true);
	sound.setVolume(0.75f);
	sound.setMultiPlay(true);

}

void ball::setup(float _x, float _y, float _vx, float _vy, 
				ofxOscSender _sOsc, int _s){
	set(_x, _y);
	v.set(_vx, _vy);
	r = 6;
	senderOsc = _sOsc;
	sample = _s;
	cout<<sample<<endl;
}

void ball::update(){
	x += v.x;
	y += v.y;
	checkBoundaryCollision();
}

void ball::checkBoundaryCollision(){
	int w = ofGetWidth();
	int h = ofGetHeight();
    if(x > w-r){
		sendOscMessage();
		x = w-r;
		v.x *= -1;
    }else if(x < r){
		sendOscMessage();
		x = r;
		v.x *= -1;
    }else if(y > h-r){
		sendOscMessage();
		y = h-r;
		v.y *= -1;
    }else if(y < r){
		sendOscMessage();
		y = r;
		v.y *= -1;
	}
}

void ball::draw(){
	// draw small circle
	ofSetColor(0, 200, 100);
	ofFill();
	ofCircle(x, y, r);
	ofNoFill();
	ofCircle(x, y, r);
}

void ball::sendOscMessage(){
	ofxOscMessage m;
	m.setAddress("/collision");
	m.addStringArg("true");
	m.addIntArg(sample);
	senderOsc.sendMessage(m);
}