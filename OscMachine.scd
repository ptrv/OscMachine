(
//s.boot;
var window, buttons, buttonsPlay, buttonsSetOsc, synthText;
var oscText1, oscText2, oscText3, oscMsg1, oscMsg2, oscMsg3;
var responderNodes; 
var compNumber=2, samples, bt1, bt2,sample1;
var compWidth = 100;

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
	.action_({responderNodes[i].remove; responderNodes = responderNodes.put(i, OSCresponderNode(nil, oscMsg1[i], { |time, resp, msg|
/*		msg.do {|i|
				i.value.postln;
			};
*/		
/*		if(msg[0] = oscMsg1[i]) {
			msg[0].postln;
			"von message 1".postln;
		};
*/	
/*		msg[1].postln;
		msg[2].postln;
*/		
		if(msg[1] == oscMsg2[i].value) {
			msg[1].value.postln;
			"von message 1".postln;
		}{
			"nichts".postln;
		};
		if(msg[2] == oscMsg3[i]) {
			//msg[2].postln;
			//"von message 2".postln;
		}{
			"nichts2".postln;
		};
	}).add;
	)
	}););
};

compNumber.do { |i|
	buttons = buttons.add( Button(window,Rect(0,0,compWidth,20))
	.states_([["sample " ++ i]])
	.action_({
		//var obj = Buffer.loadDialog(s);
		(
		CocoaDialog.getPaths({ arg paths;
			paths.do({ arg p;
				//p.postln;
				samples.put(i, p);
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
		samples[i].postln;
		f = SoundFile(samples[i]);
		f.play;
		("play " ++ i).postln;
		});
		);
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
.states_([["play test"]])
.action_({sample1.play});

window.front;
window.onClose_({compNumber.do { |i|
	("deleted respondernode" ++ i).postln;
	responderNodes[i].remove;
}});

)
resonderNodes[0].remove;