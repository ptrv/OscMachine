OscMachine : Object {
	
	var window, buttons, buttonsPlay, buttonsSetOsc, synthText;
	var oscText1, oscText2, oscText3, oscMsg1, oscMsg2, oscMsg3;
	var responderNodes, soundFileView; 
	var compNumber, samples, bt1, bt2,sample1;
	var compWidth = 100;
	
	*new { |trackNumber=1|
		^super.new.init(trackNumber);
	}

	init { |trackNumber|
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
		samples = Array.new(compNumber);
		buttonsPlay = Array.new(compNumber);
		buttonsSetOsc = Array.new(compNumber);
		responderNodes = Array.new(compNumber);
		soundFileView = Array.new(compNumber);

		compNumber.do { |i|
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
						samples.put(i, SoundFile(p));
						soundFileView[i].soundfile = samples[i];
						soundFileView[i].read(0, samples[i].numFrames);
						//soundFileView[i].timeCursorOn_(false);
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
				//samples[i].play;
				//samples[i].postln;
				//f = SoundFile(samples[i]);
				samples[i].play;
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
	
	setSampleFile {	|pos,sample|
		if (pos < compNumber){
			samples = samples.put(pos, SoundFile(sample));
			soundFileView[pos].soundfile = samples[pos];
			soundFileView[pos].read(0, samples[pos].numFrames);
			
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
			buttonsSetOsc[number].valueAction = 0;
			this.setResponder(number);
		}{
			"wrong number".postln;
		}
	}
	
	setResponder { |pos|
		responderNodes[pos].remove; "Set responder".postln; responderNodes = responderNodes.put(pos, OSCresponderNode(nil, oscMsg1[pos], { |time, resp, msg|
	/*		msg.do {|i|
					i.value.postln;
				};
	*/		
	/*		msg[1].postln;
			msg[2].postln;
	*/		
			if(msg[1] == oscMsg2[pos].asSymbol) {
				msg[1].value.postln;
				"von message 1".postln;
			}{
				"nichts".postln;
			};
			if(msg[2] == oscMsg3[pos].asInt) {
				msg[2].postln;
				"von message 2".postln;
				samples[msg[2] - 1].play;
			}{
				"nichts2".postln;
			};
		}).add;
		)
		
	}
}
