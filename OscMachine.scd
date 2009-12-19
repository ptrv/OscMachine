(
//s.boot;
var window, buttons, buttonsPlay, oscText, synthText;
var responderNodes; 
var compNumber=3, samples, bt1, bt2,sample1;
var compWidth = 100;

window = Window("OscMachine", Rect(350, 100, (compWidth + 8)*compNumber, 300));
window.view.decorator = FlowLayout(window.view.bounds);

oscText = Array.new(compNumber);
synthText = Array.new(compNumber);
buttons = Array.new(compNumber);
samples = Array.new(compNumber);
buttonsPlay = Array.new(compNumber);
responderNodes = Array.new(compNumber);

compNumber.do { |i|
	oscText = oscText.add( TextField(window, Rect(0,0,compWidth,20))
	.action_({ |field|
	field.value.postln;
	responderNodes = responderNodes.put(i, OSCresponderNode(nil, field.value, { |time, resp, msg|
		msg[1].postln;
	}).add;);
	});
	);
};
compNumber.do { |i|
	samples.add(nil);
	responderNodes.add(nil);
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
.states_([["test"]])
.action_({sample1 = Buffer.loadDialog(s)});

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