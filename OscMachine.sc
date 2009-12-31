/*
   OscMachine.sc
   Musikanalyse und -synthese Modulprojekt WS0910, TU Berlin.
   
   Created by Peter Vasil on 2009-12-28.
   Copyright 2009 Peter Vasil. All rights reserved.
*/

OscMachine : Object {
	
	var window, fxWindow, buttons, buttonsPlay, buttonsSetOsc, synthText;
	var oscText1, oscText2, oscText3, oscMsg1, oscMsg2, oscMsg3;
	var responderNodes, soundFileView; 
	var compNumber, soundFiles, samples;
	var bt1, bt2, btFx1, envSliders, volSliders, btMute, btInspect;
	var compWidth = 100;
	var server, synth, redSamplers,  fx1, fx1On;
	var <>debugMode=true;
	//var mainGroups, srcGroups, efxGroups;
	var attacks, sustains, releases, sampleLengths, amps, ampsPre;
	var <diskPlay;
	
	*new { |trackNumber=1, server=nil, diskPlay=false|
		^super.new.init(trackNumber,server,diskPlay);
	}

	init { |trackNumber,argServer, argDiskPlay|
		//If no server is given as parameter, take default server.
		server = argServer ?? Server.default;
		
		//checks if amples should play from disk or memory.
		diskPlay = argDiskPlay;
		if(debugMode){ 
			if(diskPlay) {
				"Samples are played from disk!".postln;
			}{
				"Samples are played from memory".postln;
			};
		};
		//srcGrp = Group.head(server);
		//efxGrp = Group.tail(server);
		
		//number of tracks.
		compNumber = trackNumber;	
		
		window = Window("OscMachine", Rect(350, 0, (compWidth + 8)*compNumber, 600));
		//set FlowLayout for window.
		window.view.decorator = FlowLayout(window.view.bounds);

		//Create all array with a given maximum size(compNumber).
		oscText1 = Array.new(compNumber);
		oscText2 = Array.new(compNumber);
		oscText3 = Array.new(compNumber);
		oscMsg1 = Array.new(compNumber);
		oscMsg2 = Array.new(compNumber);
		oscMsg3 = Array.new(compNumber);
		synthText = Array.new(compNumber);
		buttons = Array.new(compNumber);
		soundFiles = Array.new(compNumber);
		buttonsPlay = Array.new(compNumber);
		buttonsSetOsc = Array.new(compNumber);
		responderNodes = Array.new(compNumber);
		soundFileView = Array.new(compNumber);
		samples = Array.new(compNumber);
		envSliders = Array.new(compNumber);
		volSliders = Array.new(compNumber);
/*		mainGroups = Array.new(compNumber);
		srcGroups = Array.new(compNumber);
		efxGroups = Array.new(compNumber);
*/		btFx1 = Array.new(compNumber);
//		fx1	= Array.new(compNumber);
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
		
		//fill all array with nil to be able to us put function.
		compNumber.do { |i|
			soundFiles.add(nil);
			samples.add(nil);
			responderNodes.add(nil);
			oscMsg1.add(nil);
			oscMsg2.add(nil);
			oscMsg3.add(nil);
//			fx1.add(nil);
			sustains.add(nil);
			sampleLengths.add(nil);
		};
		
		//Fill array with default values.
		compNumber.do { |i|
			amps = amps.add(0.7);
			attacks = attacks.add(0.01);
			releases = releases.add(0.1);
			ampsPre = ampsPre.add(0.7);
		};
/*		compNumber.do { |i|
			mainGroups = mainGroups.add(Group.head(server));
			
		};
		compNumber.do { |i|			
			srcGroups = srcGroups.add(Group.head(mainGroups[i]));
		};
		compNumber.do { |i|
			efxGroups = efxGroups.add(Group.tail(mainGroups[i]));				
		};
*/		compNumber.do { |i|
			if(diskPlay) {
				redSamplers = redSamplers.add(RedDiskInSamplerGiga(server));
			}{
				redSamplers = redSamplers.add(PVRedSampler(server));
			};
			
		};

/*		compNumber.do { |i|
			SynthDef("OscMachine-fx1-"++i, {ReplaceOut.ar(0, Resonz.ar(In.ar(0,2), LFNoise2.kr(2.6).range(100, 1000), 0.2, 5))}).memStore;
			//fx1 = fx1.add(Synth("OscMachine-fx1-"++i, target: efxGrp));
		};
*/		compNumber.do { |i|
			oscText1 = oscText1.add( TextField(window, Rect(0,0,compWidth,20))
			.action_({ |field|
				field.value.postln;
				oscMsg1 = oscMsg1.put(i, field.value);
			});
			);
		};
		compNumber.do { |i|
		oscText2 = oscText2.add( TextField(window, Rect(0,0,compWidth,20))
			.action_({ |field|
				oscMsg2 = oscMsg2.put(i, field.value);
			});
			);	
		};

		compNumber.do { |i|
		oscText3 = oscText3.add( TextField(window, Rect(0,0,compWidth,20))
			.action_({ |field|
				oscMsg3 = oscMsg3.put(i, field.value);
			});
			);	
		};

		compNumber.do { |i|
			buttonsSetOsc = buttonsSetOsc.add(Button(window, Rect(0,0,compWidth,20))
			.states_([["Set responder " ++ i]])
			.action_({ this.setResponder(i) });
			);
		};

		compNumber.do { |i|
			buttons = buttons.add( Button(window,Rect(0,0,compWidth,20))
			.states_([["Load sample " ++ i]])
			.action_({
				Dialog.getPaths({ arg paths;
					paths.do({ arg p;
						this.setSampleFile(i,p);
					})
				},{
						"cancelled".postln;
				});
			}));
		};
		compNumber.do { |i|
			buttonsPlay = buttonsPlay.add( Button(window,Rect(0,0,compWidth,20))
			.states_([["play " ++ i]])
			.action_({
				this.playSample(i);
			});
			);
		};

		compNumber.do { |i|
			btInspect = btInspect.add(Button(window, Rect(0,0,compWidth,20))
				.states_([["Sample "++(i)++" Info"]])
				.action_({
					soundFiles[i].inspect;		
				});
			);
		};
		compNumber.do { |i|
			soundFileView = soundFileView.add(SoundFileView(window, Rect(0,0, compWidth, 50)));
		};

		compNumber.do { |i|
			var slders = Array.new(3);
			var txts = Array.new(3);
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
		
		compNumber.do { |i|
			volSliders = volSliders.add(Slider(window, Rect(0,0,compWidth, 20))
				.value_(amps[i])
				.action_({ |view|
					if(btMute[i].state != true) {
						amps[i] = view.value;
					};
					ampsPre[i] = view.value;			
				});
			);
		};
		
		compNumber.do { |i|
			StaticText(window, Rect(0,0,compWidth,20)).string_("Vol").align_(\center);
		};
		compNumber.do { |i|
			btMute = btMute.add(ToggleButton(window, "Mute "++i, {
				ampsPre[i] = amps[i];
				amps[i] = 0.0;
			}, {
				if (amps[i] == 0.0) {amps[i] = ampsPre[i]};
			}, false, compWidth, 20));
		};
		
		compNumber.do { |i|
			StaticText(window, Rect(0,0,compWidth,10)).string_("------------").align_(\center);
		};
		
		//----------------------------
		//create fx windows
		compNumber.do { |i|
			fxWindow = fxWindow.add(Window("Fx"++i, Rect(100,100*i,150,100)).userCanClose_(false).visible_(false));
		};
		//----------------------------
		compNumber.do { |i|
			fx1On = fx1On.add(ToggleButton(window, "Fx "++i++" on", {
				
			},{
				
			}, false, compWidth, 20));
		};
		compNumber.do { |i|
			fx1 = fx1.add(ToggleButton(window, "Fx "++i++" window", {
				fxWindow[i].visible_(true);
			},{
				fxWindow[i].visible_(false);
			}, false, compWidth, 20));
		};
		compNumber.do { |i|
			synthText = synthText.add(TextField(window, Rect(0,0,compWidth,20))
			.action_({ |field|
				field.value.postln;			
			});
			);
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

		window.front;
		window.onClose_({compNumber.do { |i|
			("deleted respondernode " ++ i).postln;
			responderNodes[i].remove;
		}});
		
	}
	
	setSampleFile {	|pos,samplePath|
		if (pos < compNumber){
			soundFiles = soundFiles.put(pos, SoundFile(samplePath));
			soundFileView[pos].soundfile = soundFiles[pos];
			soundFileView[pos].read(0, soundFiles[pos].numFrames);
			//samples = samples.put(pos, Sample(soundFiles[pos].path));
			redSamplers[pos].overlaps_(30);
			redSamplers[pos].prepareForPlay(\snd1, soundFiles[pos].path);
			sampleLengths[pos] = soundFiles[pos].numFrames/soundFiles[pos].sampleRate;
			sustains[pos] = sampleLengths[pos];
			//envSliders[pos][1].value_(1);
		}{
			"wrong position number".postln;
		}
	}
	
	playSample { |pos|
		if (soundFiles[pos].numChannels == 1){
			
				if(debugMode){"playing mono file".postln};
/*				redSamplers[pos].play(\snd1, attack: attacks[pos], release: releases[pos], out: 0, loop: 0, group: srcGroups[pos]);
				redSamplers[pos].play(\snd1, attack: attacks[pos], release: releases[pos], out: 1, loop: 0, group: srcGroups[pos]);
*/				redSamplers[pos].play(\snd1, amp: amps[pos], attack: attacks[pos], sustain: sustains[pos]-attacks[pos]-releases[pos], release: releases[pos], out: 0, loop: 0);
				redSamplers[pos].play(\snd1, amp: amps[pos], attack: attacks[pos], sustain: sustains[pos]-attacks[pos]-releases[pos], release: releases[pos], out: 1, loop: 0);
			}{
				if(debugMode){"playing stereo file".postln};
				redSamplers[pos].play(\snd1, amp: amps[pos], attack: attacks[pos], sustain: sustains[pos]-attacks[pos]-releases[pos], release: releases[pos]);
			};
			if(debugMode){("play " ++ pos).postln};
	}
	setOscMsg { |number, msg|
		if (number < compNumber){
			oscMsg1 = oscMsg1.put(number, msg.at(0));
			oscMsg2 = oscMsg2.put(number, msg.at(1));
			oscMsg3 = oscMsg3.put(number, msg.at(2));
			oscText1[number].value_(oscMsg1[number].asString);
			oscText2[number].value_(oscMsg2[number].asString);
			oscText3[number].value_(oscMsg3[number].asString);
			//buttonsSetOsc[number].valueAction_(0);
			this.setResponder(number);
		}{
			"wrong number".postln;
		}
	}
	
	setResponder { |pos|
		responderNodes[pos].remove; "Set responder".postln; responderNodes = responderNodes.put(pos, OSCresponderNode(nil, oscMsg1[pos], { |time, resp, msg|
/*			msg.do {|i|
				i.value.postln;
			};
*/				
			//msg[1].postln;
			//msg[2].postln;
				
			if(msg[1] == oscMsg2[pos]) {
				if(debugMode){
					msg[1].value.postln;
					"von message 1".postln;
				};
			};
			if(msg[2] == oscMsg3[pos]) {
				//msg[2].postln;
				oscMsg3[pos].asInt.postln;
				if(debugMode){"von message 2".postln};
				this.playSample(pos);
			};
		}).add;
		)
	}
}

PVRedAbstractSampler : RedAbstractSampler {
	//play with finite duration - if sustain=nil then use file length
	play {|key, attack= 0, sustain, release= 0, amp= 0.7, out= 0, group, loop= 0|
		var voc= this.prVoices(key).detect{|x|
			x.isPlaying.not;						//find first voice ready to play
		};
		if(voc.isNil, {
			(this.class.asString++": no free slots -increase overlaps or play slower").warn;
		}, {
			voc.play(attack, sustain, release, amp, out, group, loop);
		});
	}
}

PVRedAbstractSamplerVoice : RedAbstractSamplerVoice {
	play {|attack= 0, sustain, release= 0, amp= 0.7, out= 0, group, loop= 0|
		^this.subclassResponsibility(thisMethod)
	}
}

PVRedSampler : PVRedAbstractSampler {
	*initClass {
		"in PVRedSampler !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!".postln;
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
					var reson = Resonz.ar(src, LFNoise2.kr(2.6).range(100, 1000), 0.2, 5);
					//if (filter) {src = reson};
					Out.ar(i_out, src*env*amp);
				}, #['ir']).store;
				SynthDef("PVredSampler-"++(i+1)++"efx", {
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
					var reson = Resonz.ar(src, LFNoise2.kr(2.6).range(100, 1000), 0.2, 5);
					src = reson;
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
	play {|attack, sustain, release, amp, out, group, loop|
		var name= this.defName;
		switch(loop,
			1, {name= name++"loop"},
			2, {name= name++"loopEnv"}
		);
		isPlaying= true;
		synth= Synth.head(group ?? {server.defaultGroup}, name, [
			\i_out, out,
			\bufnum, buffer.bufnum,
			\amp, amp,
			\attack, attack,
			\sustain, sustain ?? {(length-attack-release).max(0)},
			\release, release
			//\filter, filter
		]);
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
	