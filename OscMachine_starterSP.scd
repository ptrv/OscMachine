/*
   OscMachine_starter.scd
   Musikanalyse und -synthese Modulprojekt WS0910, TU Berlin.
   
   Created by Peter Vasil on 2009-12-28.
   Copyright 2009 Peter Vasil. All rights reserved.
*/


// To play with Soundpainter
(
//get current path for sample files;
//var path = "";
var path = Document.current.dir;
a = OscMachine.new(10);
a.setSampleFile(0,path++"/audio/Misc14.wav");
a.setOscMsg(0,["/play", 0]);
a.setSampleFile(1,path++"/audio/Misc16.wav");
a.setOscMsg(1,["/play", 1]);
a.setSampleFile(2,path++"/audio/onoejinja.loop.wav");
a.setOscMsg(2,["/play", 2]);
a.setSampleFile(3,path++"/audio/shofukuji.loop.wav");
a.setOscMsg(3,["/play", 3]);
a.setSampleFile(4,path++"/audio/todaji.loop.wav");
a.setOscMsg(4,["/play", 4]);
a.setSampleFile(5,path++"/audio/hokoji.loop.wav");
a.setOscMsg(5,["/play", 5]);
a.setSampleFile(6,path++"/audio/kenchoji.loop.wav");
a.setOscMsg(6,["/play", 6]);
a.setSampleFile(7,path++"/audio/R.Clap.03.wav");
a.setOscMsg(7,["/play", 7]);
a.setSampleFile(8,path++"/audio/Misc 10.wav");
a.setOscMsg(8,["/play", 8]);
a.setSampleFile(9,path++"/audio/Misc 06.wav");
a.setOscMsg(9,["/play", 9]);
//a.debugMode = false;
)
