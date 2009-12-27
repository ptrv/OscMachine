OscMachine : Object {
	
	var window, fxwindow, buttons, buttonsPlay, buttonsSetOsc, synthText;
	var oscText1, oscText2, oscText3, oscMsg1, oscMsg2, oscMsg3;
	var responderNodes, soundFileView; 
	var compNumber, soundFiles, samples;
	var bt1, bt2,sample1;
	var compWidth = 100;
	var synth;
	
	*new { |trackNumber=1|
		^super.new.init(trackNumber);
	}

	init { |trackNumber|
		
		synth = SynthDef("oscmachineplayer", { arg buf=0, numCh=1,rate=1; 
				var sig;
				sig = PlayBuf.ar(numCh, buf, rate * BufRateScale.ir(buf), loop: 0, doneAction: 2);  
				sig = sig * 1.0;
				Out.ar(0, sig);
				});
		compNumber = trackNumber;	
		window = Window("OscMachine", Rect(350, 100, (compWidth + 8)*compNumber, 300));
		window.view.decorator = FlowLayout(window.view.bounds);

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

		compNumber.do { |i|
			soundFiles.add(nil);
			samples.add(nil);
			responderNodes.add(nil);
			oscMsg1.add(nil);
			oscMsg2.add(nil);
			oscMsg3.add(nil);
		};

		compNumber.do { |i|
			oscText1 = oscText1.add( TextField(window, Rect(0,0,compWidth,20))
			.action_({ |field|
				field.value.postln;
				oscMsg1 = oscMsg1.put(i, field.value)
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
			.states_([["sample " ++ i]])
			.action_({
				//var obj = Buffer.loadDialog(s);
				(
				Dialog.getPaths({ arg paths;
					paths.do({ arg p;
						//p.postln;
						soundFiles.put(i, SoundFile(p));
						soundFileView[i].soundfile = soundFiles[i];
						soundFileView[i].read(0, soundFiles[i].numFrames);
						//soundFileView[i].timeCursorOn_(false);
						
						samples.put(i, Sample(soundFiles[i].path));
					})
				},{
					"cancelled".postln;
				});
				);

			}			 
			));
		};
		compNumber.do { |i|
			buttonsPlay = buttonsPlay.add( Button(window,Rect(0,0,compWidth,20))
			.states_([["play " ++ i]])
			.action_({
				//soundFiles[i].play;
				//soundFiles[i].postln;
				//f = SoundFile(soundFiles[i]);
				//soundFiles[i].play;
				synth = Synth(\oscmachineplayer, [buf: samples[i].bufnumIr, numCh: samples[i].numChannels, rate: 1]);
				("play " ++ i).postln;
				});
				);
		};

		compNumber.do { |i|
			soundFileView = soundFileView.add(SoundFileView(window, Rect(0,0, compWidth, 50)));
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
			samples = samples.put(pos, Sample(soundFiles[pos].path));
			
		}{
			"wrong position number".postln;
		}
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
			/*msg.do {|i|
									i.value.postln;
								};*/
				
	/*		msg[1].postln;
			msg[2].postln;
	*/		
			if(msg[1] == oscMsg2[pos]) {
				msg[1].value.postln;
				"von message 1".postln;
			}{
				//"nichts".postln;
			};
			if(msg[2] == oscMsg3[pos]) {
				//msg[2].postln;
				oscMsg3[pos].asInt.postln;
				"von message 2".postln;
				//soundFiles[oscMsg3[pos].asInt - 1].play;
				//soundFiles[pos].play;
				synth = Synth(\oscmachineplayer, [buf: samples[pos].bufnumIr, numCh: samples[pos].numChannels, rate: 1]);
			}{
				//"nichts2".postln;
			};
		}).add;
		)
		
	}
}
