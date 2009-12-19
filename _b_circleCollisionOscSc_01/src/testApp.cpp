#include "testApp.h"
#include "stdio.h"

//--------------------------------------------------------------
testApp::testApp(){

}

//--------------------------------------------------------------
void testApp::setup(){
	ofSetVerticalSync(true);
	ofEnableSmoothing();
	ofBackground(51, 51, 51);
	
	// open an outgoing connection to HOST:PORT
	sender.setup(HOST, PORT);
}

//--------------------------------------------------------------
void testApp::update(){
	for(int i=0; i<ballList.size(); i++){
		ballList[i].update();
	}
}


//--------------------------------------------------------------
void testApp::draw(){
	for(int i=0; i<ballList.size(); i++){
		ballList[i].draw();
	}
}

//--------------------------------------------------------------
void testApp::keyPressed(int key){

}

//--------------------------------------------------------------
void testApp::keyReleased(int key){

}

//--------------------------------------------------------------
void testApp::mouseMoved(int x, int y ){

}

//--------------------------------------------------------------
void testApp::mouseDragged(int x, int y, int button){

}

//--------------------------------------------------------------
void testApp::mousePressed(int x, int y, int button){
	ball b;
	b.setup(x, y, ofRandom(-PI, PI), ofRandom(-PI, PI), sender, int(ofRandom(0, 2)));
	ballList.push_back(b);
}

//--------------------------------------------------------------
void testApp::mouseReleased(int x, int y, int button){

}

//--------------------------------------------------------------
void testApp::resized(int w, int h){

}

