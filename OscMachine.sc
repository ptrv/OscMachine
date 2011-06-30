/*
   OscMachine.sc
   Musikanalyse und -synthese Modulprojekt WS0910, TU Berlin.
   
   Created by Peter Vasil on 2009-12-28.
   Copyright 2009 Peter Vasil. All rights reserved.
*/

/*
	TODO ControlSpecs for fx parameters.
	TODO Start sserver if not up. (Routine)

*/


OscMachine : Object {
	
	var window, fxWindow, buttons, buttonsPlay, buttonsSetOsc, synthText;
	var oscText1, oscText2, oscText3, oscText4, oscMsg1, oscMsg2, oscMsg3, oscMsg4;
	var responderNodes, responderNodesEfx, soundFileView, sampleLed; 
	var compNumber, soundFiles, samples;
	var bt1, bt2, envSliders, volSliders, btMute, btInspect;
	var compWidth = 100, countStart=0;
	var server, synth, redSamplers, fx1windowBt, fx1OnBt, fx1On;
	var fx1Params1, fx1Params2;
	var <>debugMode=true;
	var attacks, sustains, releases, sampleLengths, amps, ampsPre;
	var <diskPlay, ovlaps;
	var msg2On, msg3On, msg4On, msg2Bt, msg3Bt, msg4Bt;
	var synthDefs, synthsOnBts, synthsOn;
	
	*new { |trackNumber=1, server=nil, diskPlay=false, showWin=true, overlap|
		^super.new.init(trackNumber,server,diskPlay, showWin, overlap);
	}

	// initialising the instance.
	init { |trackNumber,argServer, argDiskPlay, showWin, overlap|
		//If no server is given as parameter, take default server.
		server = argServer ?? Server.default;
		ovlaps = overlap ?? 30;
		//checks if amples should play from disk or memory.
		diskPlay = argDiskPlay;
		if(debugMode){ 
			if(diskPlay) {
				"Samples are played from disk!".postln;
			}{
				"Samples are played from memory!".postln;
			};
		};
		
		//number of tracks.
		compNumber = trackNumber;	
		
		window = Window("OscMachine", Rect(350, 0, (compWidth + 8)*compNumber, 600));
		//set FlowLayout for window.
		//window.view.decorator = FlowLayout(window.view.bounds, margin: 10@10, gap: 10@10);
		window.view.decorator = FlowLayout(window.view.bounds);
		
		//Create all arrays with a given maximum size (compNumber).
		oscText1 = Array.new(compNumber);
		oscText2 = Array.new(compNumber);
		oscText3 = Array.new(compNumber);
		oscText4 = Array.new(compNumber);
		oscMsg1 = Array.new(compNumber);
		oscMsg2 = Array.new(compNumber);
		oscMsg3 = Array.new(compNumber);
		oscMsg4 = Array.new(compNumber);
		synthText = Array.new(compNumber);
		buttons = Array.new(compNumber);
		soundFiles = Array.new(compNumber);
		buttonsPlay = Array.new(compNumber);
		buttonsSetOsc = Array.new(compNumber);
		responderNodes = Array.new(compNumber);
		responderNodesEfx = Array.new(compNumber);
		soundFileView = Array.new(compNumber);
		samples = Array.new(compNumber);
		envSliders = Array.new(compNumber);
		volSliders = Array.new(compNumber);
		fx1windowBt	= Array.new(compNumber);
		attacks = Array.new(compNumber);
		sustains = Array.new(compNumber);
		releases = Array.new(compNumber);
		sampleLengths = Array.new(compNumber);
		amps = Array.new(compNumber);
		ampsPre = Array.new(compNumber);
		btMute = Array.new(compNumber);
		btInspect = Array.new(compNumber);
		fxWindow = Array.new(compNumber);
		fx1On = Array.new(compNumber);
		fx1OnBt = Array.new(compNumber);
		fx1Params1 = Array.new(compNumber);
		fx1Params2 = Array.new(compNumber);
		sampleLed = Array.new(compNumber);
		msg2On = Array.new(compNumber);
		msg3On = Array.new(compNumber);
		msg4On = Array.new(compNumber);
		msg2Bt = Array.new(compNumber);
		msg3Bt = Array.new(compNumber);
		msg4Bt = Array.new(compNumber);
		synthDefs = Array.new(compNumber);
		synthsOnBts = Array.new(compNumber);
		
		//fill all array with nil to be able to us put function.
		compNumber.do { |i|
			soundFiles.add(nil);
			samples.add(nil);
			responderNodes.add(nil);
			responderNodesEfx.add(nil);
			oscMsg1.add(nil);
			oscMsg2.add(nil);
			oscMsg3.add(nil);
			oscMsg4.add(nil);
//			fx1windowBt.add(nil);
			sustains.add(nil);
			sampleLengths.add(nil);
			synthDefs.add(nil);
			
		};
		
		//Fill array with default values.
		compNumber.do { |i|
			amps = amps.add(0.7);
			attacks = attacks.add(0.01);
			releases = releases.add(0.1);
			ampsPre = ampsPre.add(0.7);
			fx1On = fx1On.add(false);
			msg2On = msg2On.add(true);
			msg3On = msg3On.add(true);
			msg4On = msg4On.add(true);
			fx1Params1 = fx1Params1.add(0.2);
			fx1Params2 = fx1Params1.add(4);
		};

		//Choose between PVRedSampler and RedDiskInSamplerGiga.
		compNumber.do { |i|
			if(diskPlay) {
				redSamplers = redSamplers.add(RedDiskInSamplerGiga(server));
			}{
				redSamplers = redSamplers.add(PVRedSampler(server));
			};
			
		};

		//Track names
		compNumber.do { |i|
			StaticText(window, Rect(0,0,compWidth,20)).string_("Track "++i).align_(\center).background_(Color.new255(211, 211, 211));
		};
		//Indicator for playing the sample
		compNumber.do { |i|
			sampleLed = sampleLed.add(StaticText(window, Rect(0,0,compWidth,10))
			.background_(Color.black));
		};
		// TextField for OSC message 0 (address).
		compNumber.do { |i|
			oscText1 = oscText1.add( TextField(window, Rect(0,0,compWidth,20))
			.action_({ |field|
				field.value.postln;
				oscMsg1 = oscMsg1.put(i, field.value);
			});
			);
		};
		// TextField for OSC message 1
		compNumber.do { |i|
			//var container = HLayoutView(window, Rect(0,0,compWidth,20));
			oscText2 = oscText2.add( TextField(window, Rect(0,0,compWidth,20))
				.action_({ |field|
					oscMsg2 = oscMsg2.put(i, field.value);
				});
			);
/*			msg2Bt = msg2Bt.add(ToggleButton(container, "on", {
					msg2On[i] = true;
				}, {
					msg2On[i] = false;
				}, msg2On[i], 20,20));	
*/		};
		// TextField for OSC message 2 with toggle button to switch on/off.
		compNumber.do { |i|
			var container = HLayoutView(window, Rect(0,0,compWidth,20));
			oscText3 = oscText3.add( TextField(container, Rect(0,0,compWidth-26,20))
				.action_({ |field|
					oscMsg3 = oscMsg3.put(i, field.value);
				});
			);
			msg3Bt = msg3Bt.add(ToggleButton(container, "on", {
					msg3On[i] = true;
				}, {
					msg3On[i] = false;
				}, msg3On[i], 20,20));		
		};

/*		compNumber.do { |i|
			var container = HLayoutView(window, Rect(0,0,compWidth,20));
			oscText4 = oscText4.add( TextField(container, Rect(0,0,compWidth-26,20))
				.action_({ |field|
					oscMsg4 = oscMsg4.put(i, field.value);
				});
			);
			msg4Bt = msg4Bt.add(ToggleButton(container, "on", {
					msg4On[i] = true;
				}, {
					msg4On[i] = false;
				}, msg4On[i], 20,20));		
		};
*/
		// Button or creating OSCresponderNodes
		compNumber.do { |i|
			buttonsSetOsc = buttonsSetOsc.add(Button(window, Rect(0,0,compWidth,20))
			.states_([["Set responder " ++ i]])
			.action_({ this.setResponder(i) });
			);
		};
		//Button for opening a file dialog to load a sample file
		compNumber.do { |i|
			buttons = buttons.add( Button(window,Rect(0,0,compWidth,20))
			.states_([["Load sample " ++ i]])
			.action_({
				// It is possible to choose more than one file in the dialog 
				// but the last will be set as sample file 
				Dialog.getPaths({ arg paths;
					paths.do({ arg p;
						this.setSampleFile(i,p);
					})
				},{
						"cancelled".postln;
				});
			}));
		};
		// Play button
		compNumber.do { |i|
			buttonsPlay = buttonsPlay.add( Button(window,Rect(0,0,compWidth,20))
			.states_([["play "++i]])
			.action_({
				this.playSample(i);
			});
			);
		};
		
		// Opens an sample inspector window
		compNumber.do { |i|
			btInspect = btInspect.add(Button(window, Rect(0,0,compWidth,20))
				.states_([["Sample "++(i)++" Info"]])
				.action_({
					soundFiles[i].inspect;		
				});
			);
		};
		
		// Visual presentation of the sound file.
		compNumber.do { |i|
			soundFileView = soundFileView.add(SoundFileView(window, Rect(0,0, compWidth, 50)));
		};
		
		// Create sliders for envelope.
		compNumber.do { |i|
			// TODO better values for A, S, R
			var slders = Array.new(3);
			var txts = Array.new(3);
			//Create some layout containers to be able to group some gui components
			var containerV = VLayoutView(window, Rect(0,0,compWidth, 80));
			var containerS = HLayoutView(containerV, Rect(0,0,compWidth, 60));
			var containerT = HLayoutView(containerV, Rect(0,0,compWidth, 20));
			slders = slders.add(Slider(containerS, Rect(0, 0, 30, 60))
				.value_(attacks[i])
				.action_({ |view|
					attacks[i] = view.value * 1.99 + 0.01;
					if(debugMode){ attacks[i].postln; };			
				}));
			slders = slders.add(Slider(containerS, Rect(0, 0, 30, 60))
				.value_(if(sampleLengths[i] == nil) {1}{sustains[i]/sampleLengths[i]})
				.action_({ |view|
					if(sampleLengths[i] != nil) {sustains[i] = view.value * sampleLengths[i]};
					if(debugMode){ sustains[i].postln; };
				});
			);
			slders = slders.add(Slider(containerS, Rect(0, 0, 30, 60))
				.value_(releases[i])
				.action_({ |view|
					releases[i] = view.value * 2 + 0.01;
					if(debugMode){ releases[i].postln; };			
				}));
			envSliders = envSliders.add(slders);
			
			txts = txts.add(StaticText(containerT, Rect(0, 0, 30, 20)).string_("A").align_(\center));
			txts = txts.add(StaticText(containerT, Rect(0, 0, 30, 20)).string_("S").align_(\center));
			txts = txts.add(StaticText(containerT, Rect(0, 0, 30, 20)).string_("R").align_(\center));
			
		};
		
		// Mute button
		compNumber.do { |i|
			volSliders = volSliders.add(Slider(window, Rect(0,0,compWidth, 20))
				.value_(amps[i])
				.action_({ |view|
					if(btMute[i].state == false) {
						amps[i] = view.value;
					};
					ampsPre[i] = view.value;			
				});
			);
		};
		
		// Vol text
		compNumber.do { |i|
			StaticText(window, Rect(0,0,compWidth,20)).string_("Vol").align_(\center);
		};
		
		// Mute toggle button
		compNumber.do { |i|
			btMute = btMute.add(ToggleButton(window, "Mute "++i, {
				ampsPre[i] = amps[i];
				amps[i] = 0.0;
			}, {
				if (amps[i] == 0.0) {amps[i] = ampsPre[i]};
			}, false, compWidth, 20));
		};
		
		//Seperator
		compNumber.do { |i|
			StaticText(window, Rect(0,0,compWidth,10)).string_("------------").align_(\center);
		};
		
		//----------------------------
		//create fx windows
		compNumber.do { |i|
			fxWindow = fxWindow.add(Window("Fx"++i, Rect(100,100*i,150,100)).userCanClose_(false).visible_(false));
			Slider(fxWindow[i], Rect(20,20,100,10)).value_(0.2 / 1.01).action_({ |view|
				var val = view.value * 1 + 0.01;
				if(debugMode){ ("fxWindow "++i++" slider 1: "++(val)).postln; };
				fx1Params1[i] = val;
			});
			Slider(fxWindow[i], Rect(20,40,100,10)).value_(3 / 4).action_({ |view|
				var val = view.value * 4 + 1;
				if(debugMode){ ("fxWindow "++i++" slider 2: "++(val)).postln; };
				fx1Params2[i] = val;
			});
			Slider(fxWindow[i], Rect(20,60,100,10)).action_({ |view|
				var val = view.value;
				if(debugMode){ ("fxWindow "++i++" slider 3: "++(val)).postln; };
			});
			Slider(fxWindow[i], Rect(20,80,100,10)).action_({ |view|
				var val = view.value;
				if(debugMode){ ("fxWindow "++i++" slider 4: "++(val)).postln; };
			});
		};
		//----------------------------
		// Fx buttons
		compNumber.do { |i|
			fx1OnBt = fx1OnBt.add(ToggleButton(window, "Fx "++i++" on", {
				fx1On[i] = true;
			},{
				fx1On[i] = false;
			}, false, compWidth, 20));
		};
		
		// Buton for opening fx window.
		compNumber.do { |i|
			fx1windowBt = fx1windowBt.add(ToggleButton(window, "Fx "++i++" window", {
				fxWindow[i].visible_(true);
			},{
				fxWindow[i].visible_(false);
			}, false, compWidth, 20));
		};

		//Seperator
		compNumber.do { |i|
			StaticText(window, Rect(0,0,compWidth,10)).string_("------------").align_(\center);
		};
		
		//Synths
		compNumber.do { |i|
			var container = HLayoutView(window, Rect(0,0,compWidth,20));
			synthText = synthText.add(TextField(container, Rect(0,0,compWidth-26,20))
			.action_({ |field|
				field.value.postln;
				synthDefs = synthDefs.put(i, field.value);			
			});
			);
			synthsOnBts = synthsOnBts.add(ToggleButton(container, "on", {
				
			}, {
				
			}, false, 20,20));
		};

		
		bt1 = Button(window, Rect(0,0,compWidth,20))
		.states_([["show mapping"]])
		.action_({
			compNumber.do { |i|
				oscMsg1[i].postln;
				oscMsg2[i].postln;
				oscMsg3[i].postln;
			};
		});

		bt2 = Button(window, Rect(0,0,compWidth,20))
		.states_([["post oscMsg2"]])
		.action_({
			compNumber.do { |i|
				oscMsg2[i].postln;
			};
		});

		if(showWin) {
			window.front;
		};
		
		// Keyboard commands for mute buttons. 48 = 0, 49 = 1, etc.
		window.view.keyDownAction_( { |view, char, mod, uni, key| 
		    switch (uni,
		 		48,	{char.postln; if((uni-48)<compNumber) {btMute[uni-48].toggle};},
				49,	{char.postln; if((uni-48)<compNumber) {btMute[uni-48].toggle};},
				50,	{char.postln; if((uni-48)<compNumber) {btMute[uni-48].toggle};},
				51,	{char.postln; if((uni-48)<compNumber) {btMute[uni-48].toggle};},
				52,	{char.postln; if((uni-48)<compNumber) {btMute[uni-48].toggle};},
				53,	{char.postln; if((uni-48)<compNumber) {btMute[uni-48].toggle};},
				54,	{char.postln; if((uni-48)<compNumber) {btMute[uni-48].toggle};},
	 			55,	{char.postln; if((uni-48)<compNumber) {btMute[uni-48].toggle};},
				56,	{char.postln; if((uni-48)<compNumber) {btMute[uni-48].toggle};},
				57,	{char.postln; if((uni-48)<compNumber) {btMute[uni-48].toggle};},
				58,	{char.postln; if((uni-48)<compNumber) {btMute[uni-48].toggle};},
				{nil}); 
		});
		
		// cleanup when closing window
		window.onClose_({this.close});
	}
	// Here happens the clean up at closing of OscMachine
	close {
		if(window != nil) {
			window.close;
		};
		compNumber.do { |i|
			//redSamplers[i].stop(\snd1);
			//redSamplers[i].flush();
			redSamplers[i].free;
			//soundFiles[i].close;
			fxWindow[i].close;
			responderNodes[i].remove;
			("deleted respondernode " ++ i).postln;
			AppClock.clear;
		};
	}
	// Shows main window
	showWindow {
		window.front;
	}
	
	// Hides main window
	hideWindow {
		window.visible_(false);
	}
	// Set sample file.
	setSampleFile {	|pos,samplePath|
		if (pos < compNumber){
			var ovl = ovlaps;
			soundFiles = soundFiles.put(pos, SoundFile(samplePath));
			soundFileView[pos].soundfile = soundFiles[pos];
			soundFileView[pos].read(0, soundFiles[pos].numFrames, closeFile: true);
			//samples = samples.put(pos, Sample(soundFiles[pos].path));
			if(soundFiles[pos].numChannels == 1){ovl = ovlaps*2};
			redSamplers[pos].overlaps_(ovl);
			redSamplers[pos].prepareForPlay(\snd1, soundFiles[pos].path);
			sampleLengths[pos] = soundFiles[pos].numFrames/soundFiles[pos].sampleRate;
			sustains[pos] = sampleLengths[pos];
			//envSliders[pos][1].value_(1);
		}{
			"wrong position number".postln;
		}
	}

	playSample { |pos|
		if (soundFiles[pos] != nil) {
			if (soundFiles[pos].numChannels == 1){
			
					if(debugMode){"playing mono file".postln};
				
					if(fx1On[pos]) {
						redSamplers[pos].play(\snd1, 
												amp: amps[pos], 
												attack: attacks[pos], 
												sustain: sustains[pos]-attacks[pos]-releases[pos], 
												release: releases[pos], 
												out: 0, 
												loop: 0, 
												defMode: 1, 
												efx1: fx1Params1[pos],
												efx2: fx1Params2[pos]//,
												//efx3: (soundFiles[pos].numFrames / soundFiles[pos].sampleRate)
												);
						redSamplers[pos].play(\snd1, 
												amp: amps[pos], 
												attack: attacks[pos], 
												sustain: sustains[pos]-attacks[pos]-releases[pos], 
												release: releases[pos], 
												out: 1, 
												loop: 0, 
												defMode: 1, 
												efx1: fx1Params1[pos],
												efx2: fx1Params2[pos]//,
												//efx3: (soundFiles[pos].numFrames / soundFiles[pos].sampleRate)
												);
					}{
						redSamplers[pos].play(\snd1, 
												amp: amps[pos], 
												attack: attacks[pos], 
												sustain: sustains[pos]-attacks[pos]-releases[pos], 
												release: releases[pos], 
												out: 0, 
												loop: 0
												);
						redSamplers[pos].play(\snd1, 
												amp: amps[pos], 
												attack: attacks[pos], 
												sustain: sustains[pos]-attacks[pos]-releases[pos], 
												release: releases[pos], 
												out: 1, 
												loop: 0
												);
					};
				}{
					if(debugMode){"playing stereo file".postln};
					if(fx1On[pos]) {
						redSamplers[pos].play(\snd1, 
												amp: amps[pos], 
												attack: attacks[pos], 
												sustain: sustains[pos]-attacks[pos]-releases[pos], 
												release: releases[pos], 
												defMode: 1, 
												efx1: fx1Params1[pos],
												efx2: fx1Params2[pos]//,
												//efx3: (soundFiles[pos].numFrames / soundFiles[pos].sampleRate)
												);
					}{
						redSamplers[pos].play(\snd1, 
												amp: amps[pos], 
												attack: attacks[pos], 
												sustain: sustains[pos]-attacks[pos]-releases[pos], 
												release: releases[pos]
												);
					};
				};
				if(debugMode){("play " ++ pos).postln};
		
				//turn indicator on for playing for the time of the sample length
				{sampleLed[pos].background_(Color.red)}.defer;
				AppClock.sched(sustains[pos], {sampleLed[pos].background_(Color.black); nil});
			}{ 
				"No sample file loaded".postln;
			};
	}
	setOscMsg { |number, msg| //, msgEfx|
		if (number < compNumber){
			oscMsg1 = oscMsg1.put(number, msg.at(0));
			oscMsg2 = oscMsg2.put(number, msg.at(1));
			if(msg.at(2) != nil){oscMsg3 = oscMsg3.put(number, msg.at(2))}{msg3Bt[number].toggle};
			
			oscText1[number].value_(oscMsg1[number].asString);
			oscText2[number].value_(oscMsg2[number].asString);
			if(oscMsg3[number] != nil){oscText3[number].value_(oscMsg3[number].asString)};
			//buttonsSetOsc[number].valueAction_(0);
			
/*			if(msgEfx != nil){
				if(msgEfx.at(0) != nil){
					oscMsg4 = oscMsg4.put(number, msgEfx.at(0));
				}{
					msg4Bt[number].toggle;
				}
			};
			if(oscMsg4[number] != nil){oscText4[number].value_(oscMsg4[number].asString)};
*/			
			this.setResponder(number);
		}{
			"wrong number".postln;
		}
	}
	
	setResponder { |pos|
		responderNodes[pos].remove; 
		("Set responder"++pos).postln; 
		responderNodes = responderNodes.put(pos, OSCresponderNode(nil, oscMsg1[pos], { |time, resp, msg|
/*			msg.do {|i|
				i.value.postln;
			};
*/		
			if(msg3On[pos]==false){
				if(msg[1].asString == oscMsg2[pos].asString) {
					this.playSample(pos);
				};
			};
			if(msg3On[pos]==true) {
				if((msg[1].asString == oscMsg2[pos].asString) && (msg[2].asString == oscMsg3[pos].asString)) {
					if(debugMode){"von message 2".postln};
					this.playSample(pos);
				};
			};
		}).add;
		);
/*		msg4On[pos].value.postln;
		if(msg4On[pos] == true){
			"bin hier in respondernodesefx".postln;
			responderNodesEfx = responderNodesEfx.put(pos, OSCresponderNode(nil, "/fx", { |t, r, m|
				m.do {|i|
					i.value.postln;
				};
				if(m[1].asString == oscMsg4[pos].asString && m[2] == pos) {
					this.setEfx(pos, (m[3]*10));
				};
			}).add;
			);
		};
*/		
	}
}

PVRedAbstractSampler : RedAbstractSampler {
	//play with finite duration - if sustain=nil then use file length
	play {|key, attack= 0, sustain, release= 0, amp= 0.7, out= 0, group, loop= 0, defMode=0, efx1, efx2, efx3|
		var voc= this.prVoices(key).detect{|x|
			x.isPlaying.not;						//find first voice ready to play
		};
		if(voc.isNil, {
			(this.class.asString++": no free slots -increase overlaps or play slower").warn;
		}, {
			voc.play(attack, sustain, release, amp, out, group, loop, defMode, efx1, efx2, efx3);
		});
	}
	efx1_ {|val|
		keys.do{|voices| voices.do{|x| x.efx1_(val)}};
	}
}

PVRedAbstractSamplerVoice : RedAbstractSamplerVoice {
	play {|attack= 0, sustain, release= 0, amp= 0.7, out= 0, group, loop= 0|
		^this.subclassResponsibility(thisMethod)
	}
	efx1_{|val|
		synth.set(\nf, val);
	}
}

PVRedSampler : PVRedAbstractSampler {
	*initClass {
		//"in PVRedSampler !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!".postln;
		StartUp.add{
			8.do{|i|								//change here for more channels than 8
				SynthDef("PVredSampler-"++(i+1), {
					|i_out= 0, bufnum, amp= 0.7, attack= 0.01, sustain, release= 0.1, gate= 1, offset= 0|
					var src= PlayBuf.ar(
						i+1,
						bufnum,
						BufRateScale.ir(bufnum),
						1,
						BufFrames.ir(bufnum)*offset,
						0
					);
					var env= EnvGen.kr(
						Env(#[0, 1, 1, 0], [attack, sustain, release], -4),
						gate,
						1,
						0,
						1,
						2						//doneAction
					);
					Out.ar(i_out, src*env*amp);
				}, #['ir']).store;
				SynthDef("PVredSampler-"++(i+1)++"efx", {
					|i_out= 0, bufnum, amp= 0.7, attack= 0.01, sustain, release= 0.1, gate= 1, offset= 0, maxdlytime=0.5, delaytime=0.2, decay=4 |
					var src= PlayBuf.ar(
						i+1,
						bufnum,
						BufRateScale.ir(bufnum),
						1,
						BufFrames.ir(bufnum)*offset,
						0
					);
					var env= EnvGen.kr(
						Env(#[0, 1, 1, 0], [attack, sustain, release], -4),
						gate,
						1,
						0,
						1,
						2						//doneAction
					);
					//var reson = Resonz.ar(src, LFNoise2.kr(nf).range(100, 1000), 0.2, 5);
					var delay = CombC.ar(src, maxdlytime, delaytime, decay, 1, src);
					src = delay;
					Out.ar(i_out, src*env*amp);
				}, #['ir']).store;
				SynthDef("PVredSampler-"++(i+1)++"loop", {
					|i_out= 0, bufnum, amp= 0.7, attack= 0.01, release= 0.1, gate= 1, offset= 0|
					var src= PlayBuf.ar(
						i+1,
						bufnum,
						BufRateScale.ir(bufnum),
						1,
						BufFrames.ir(bufnum)*offset,
						1
					);
					var env= EnvGen.kr(
						Env(#[0, 1, 0], [attack, release], -4, 1),
						gate,
						1,
						0,
						1,
						2						//doneAction
					);
					Out.ar(i_out, src*env*amp);
				}, #['ir']).store;
				SynthDef("PVredSampler-"++(i+1)++"loopEnv", {
					|i_out= 0, bufnum, amp= 0.7, attack= 0.01, sustain, release= 0.1, gate= 1, offset= 0|
					var src= PlayBuf.ar(
						i+1,
						bufnum,
						BufRateScale.ir(bufnum),
						1,
						BufFrames.ir(bufnum)*offset,
						1
					);
					var env= EnvGen.kr(
						Env(#[0, 1, 1, 0], [attack, sustain, release], -4),
						gate,
						1,
						0,
						1,
						2						//doneAction
					);
					Out.ar(i_out, src*env*amp);
				}, #['ir']).store;
			}
		}
	}
	prCreateVoice {|sf, startFrame, argNumFrames|
		var len;
		if(argNumFrames.notNil, {
			len= argNumFrames/sf.sampleRate;
		}, {
			len= sf.numFrames-startFrame/sf.sampleRate;
		});
		^PVRedSamplerVoice(server, sf.path, sf.numChannels, startFrame, argNumFrames, len);
	}
}

PVRedSamplerVoice : PVRedAbstractSamplerVoice {
	defName {^"PVredSampler-"++channels}
	play {|attack, sustain, release, amp, out, group, loop, defMode, efx1, efx2, efx3|
		var name= this.defName;
		var mode = defMode;
		switch(loop,
			1, {name= name++"loop"},
			2, {name= name++"loopEnv"}
		);
		isPlaying= true;
		if(mode == 1 ) {
			name= name++"efx";
			synth= Synth.head(group ?? {server.defaultGroup}, name, [
				\i_out, out,
				\bufnum, buffer.bufnum,
				\amp, amp,
				\attack, attack,
				\sustain, sustain ?? {(length-attack-release).max(0)},
				\release, release,
				\delaytime, efx1,
				\decay, efx2//,
				//\maxdlytime, efx3
				//\filter, filter
				// TODO more fx params.
			]);
		}{
			synth= Synth.head(group ?? {server.defaultGroup}, name, [
				\i_out, out,
				\bufnum, buffer.bufnum,
				\amp, amp,
				\attack, attack,
				\sustain, sustain ?? {(length-attack-release).max(0)},
				\release, release
				//\filter, filter
			]);
		};
		
		OSCresponderNode(server.addr, '/n_end', {|t, r, m|
			if(m[1]==synth.nodeID, {
				isPlaying= false;
				isReleased= false;
				r.remove;
			});
		}).add;
	}
	prAllocBuffer {|action|
		var num= numFrames ? -1;
		buffer= Buffer.read(server, path, startFrame, num, action)
	}
}
	