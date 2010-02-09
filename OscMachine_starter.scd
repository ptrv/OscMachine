/*
   OscMachine_starter.scd
   Musikanalyse und -synthese Modulprojekt WS0910, TU Berlin.
   
   Created by Peter Vasil on 2009-12-28.
   Copyright 2009 Peter Vasil. All rights reserved.
*/

/*
	IF YOU WANT TO USE THIS FILE PLEASE CHANGE SAMPLE PATHS!!!
*/

OscMachine.new
OscMachine.new(10)
OscMachine.new(diskPlay: true);

(
a = OscMachine.new;
a.setSampleFile(0,"/Users/peter/scwork/snd/AltoSax.aif");
a.setOscMsg(0,["/play", "true", 1]);
)

(
a = OscMachine.new(2);
a.setSampleFile(0,"/Users/peter/scwork/snd/AltoSax.aif");
a.setOscMsg(0,["/play", "sample", 0]);
a.setSampleFile(1,"/Users/peter/scwork/snd/axaxaxas.aiff");
a.setOscMsg(1,["/play", "true", 1]);
//a.debugMode = false;
)

(
a = OscMachine.new(2);
a.setSampleFile(0,"/Users/peter/scwork/snd/AltoSax.aif");
a.setOscMsg(0,["/play", 0]);
a.setSampleFile(1,"/Users/peter/scwork/snd/axaxaxas.aiff");
a.setOscMsg(1,["/play", 1]);
//a.debugMode = false;
)

// with efx
(
a = OscMachine.new(2);
a.setSampleFile(0,"/Users/peter/scwork/snd/AltoSax.aif");
a.setOscMsg(0,["/play", 0], ["positionx"]);
a.setSampleFile(1,"/Users/peter/scwork/snd/axaxaxas.aiff");
a.setOscMsg(1,["/play", 1]);
//a.debugMode = false;
)

(
a = OscMachine.new(5, overlap:100);
a.setSampleFile(0,"/Users/peter/audio/samples/Renoise2 Sample Library/Samples/Misc/Misc 12.wav");
a.setOscMsg(0,["/collision", "true", 0]);
a.setSampleFile(1,"/Users/peter/audio/samples/Renoise2 Sample Library/Samples/Misc/Misc 13.wav");
a.setOscMsg(1,["/collision", "true", 1]);
a.setSampleFile(2,"/Users/peter/audio/samples/Renoise2 Sample Library/Samples/Misc/Misc 14.wav");
a.setOscMsg(2,["/collision", "true", 2]);
a.setSampleFile(3,"/Users/peter/audio/samples/Renoise2 Sample Library/Samples/Misc/Misc 15.wav");
a.setOscMsg(3,["/collision", "true", 3]);
a.setSampleFile(4,"/Users/peter/audio/samples/Renoise2 Sample Library/Samples/Misc/Misc 16.wav");
a.setOscMsg(4,["/collision", "true", 4]);
)


(
a = OscMachine.new(2, overlap:100);
a.setSampleFile(0,"/Users/peter/development/of_preRelease_v0.06_xcode_FAT/apps/of_myApps006/_b_circleCollisionOscSc_01/bin/data/sounds/36846__EcoDTR__LaserRocket.wav");
a.setOscMsg(0,["/collision", "true", 0]);
a.setSampleFile(1,"/Users/peter/development/of_preRelease_v0.06_xcode_FAT/apps/of_myApps006/_b_circleCollisionOscSc_01/bin/data/sounds/51463__smcameron__big_explosion.wav");
a.setOscMsg(1,["/collision", "true", 1]);
)

// no window mode
(
a = OscMachine.new(2, showWin: false);
a.setSampleFile(0,"/Users/peter/scwork/snd/AltoSax.aif");
a.setOscMsg(0,["/play", 0]);
a.setSampleFile(1,"/Users/peter/scwork/snd/axaxaxas.aiff");
a.setOscMsg(1,["/play", 1]);
//a.debugMode = false;
)

a.showWindow;
a.hideWindow;
// if window is not used you have to call close fuction
a.close;


// To play with Soundpainter
(
a = OscMachine.new(10);
a.setSampleFile(0,"/Users/peter/repos/OscMachine/audio/Misc14.wav");
a.setOscMsg(0,["/play", 0]);
a.setSampleFile(1,"/Users/peter/repos/OscMachine/audio/Misc16.wav");
a.setOscMsg(1,["/play", 1]);
a.setSampleFile(2,"/Users/peter/repos/OscMachine/audio/onoejinja.loop.wav");
a.setOscMsg(2,["/play", 2]);
a.setSampleFile(3,"/Users/peter/repos/OscMachine/audio/shofukuji.loop.wav");
a.setOscMsg(3,["/play", 3]);
a.setSampleFile(4,"/Users/peter/repos/OscMachine/audio/todaji.loop.wav");
a.setOscMsg(4,["/play", 4]);
a.setSampleFile(5,"/Users/peter/repos/OscMachine/audio/hokoji.loop.wav");
a.setOscMsg(5,["/play", 5]);
a.setSampleFile(6,"/Users/peter/repos/OscMachine/audio/kenchoji.loop.wav");
a.setOscMsg(6,["/play", 6]);
a.setSampleFile(7,"/Users/peter/audio/samples/Renoise2 Sample Library/Samples/Reverse/R.Clap.03.wav");
a.setOscMsg(7,["/play", 7]);
a.setSampleFile(8,"/Users/peter/audio/samples/Renoise2 Sample Library/Samples/Misc/Misc 10.wav");
a.setOscMsg(8,["/play", 8]);
a.setSampleFile(9,"/Users/peter/audio/samples/Renoise2 Sample Library/Samples/Misc/Misc 06.wav");
a.setOscMsg(9,["/play", 9]);
//a.debugMode = false;
)
a.close
//----------------------------------------------------------------------

(
SynthDef("reson", { arg in=0, out=0, nfreq=2.6;
	var sig;
	sig = In.ar(in);
	sig = Resonz.ar(sig, LFNoise2.kr(nfreq).range(100, 1000), 0.2, 5);
	ReplaceOut.ar(out, sig);	
}).store;

SynthDef("chop", { arg in=0, out=0, pfreq=5;
	var sig;
	sig = In.ar(in);
	sig = LFPulse.ar(pfreq, mul: sig);
	ReplaceOut.ar(out, sig);	
}).store;

SynthDef("sineosc1", { |freq=440, amp=0.5|
	var sig;
	sig = SinOsc.ar(freq, 0, amp);
	Out.ar(0, sig);	
}).store;

SynthDef("sawosc1", { |freq=440, amp= 0.5|
	var sig;
	sig = Saw.ar(freq, amp);
	Out.ar(1, sig);	
}).store;
)

~fx = Group.tail(Server.default);

~chop = Synth("chop", [\in, 1, \out, 1], ~fx)
~chop.set(\pfreq, 1);
~res = Synth("reson", target: ~fx)
~chop.free
~fx.run(false)
~fx.run(true)

~sinosc = Synth("sineosc1");
~sawosc = Synth("sawosc1");
~sinosc.set(\amp, 0.2);
~sawosc.set(\amp, 0.2);

~sinosc.free;
~sawosc.free;



(
o = OSCresponderNode(nil, "/fx", { |time, resp, msg|
		msg.collect {arg i;
			i.value.postln;
		};
        if(msg[1] == 'positionx'){
                ~sinosc.set(\freq, msg[2]); // we pass in the number of the buffer
        };
        if(msg[3] == 'positiony'){
                ~sawosc.set(\freq, msg[4]); // we pass in the number of the buffer
        };
}).add;
)
o.remove;
(
o = OSCresponderNode(nil, "/fx", { |time, resp, msg|
//		msg.collect {arg i;
//			i.value.postln;
//		};
//        if(msg[1] == 'positionx'){
//                ~chop.set(\pfreq, msg[2]/100); // we pass in the number of the buffer
//        };
        if(msg[3] == 'positiony'){
                ~reson.set(\nfreq, msg[4]/100); // we pass in the number of the buffer
        };
}).add;
)
o.remove;


