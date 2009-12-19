(
//s.boot;
var window, buttons, buttonsPlay, oscText, synthText, compNumber=1, samples, bt1, bt2,sample1;

window = Window("OscMachine", Rect(350, 100, 55*compNumber, 300));
window.view.decorator = FlowLayout(window.view.bounds);

oscText = Array.new(compNumber);
synthText = Array.new(compNumber);
buttons = Array.new(compNumber);
samples = Array.new(compNumber);
buttonsPlay = Array.new(compNumber);

compNumber.do { |i|
	oscText = oscText.add( TextField(window, Rect(0,0,50,20))
	.action_({ |field|
	field.value.postln;
	});
	);
};
compNumber.do { |i|
	samples.add(nil);
};

compNumber.do { |i|
	buttons = buttons.add( Button(window,Rect(0,0,50,20))
	.states_([["sample " ++ i]])
	.action_({
		var obj = Buffer.loadDialog(s);
		samples.put(i, obj);
	}			 
	));
};
compNumber.do { |i|
	buttonsPlay = buttonsPlay.add( Button(window,Rect(0,0,50,20))
	.states_([["play " ++ i]])
	.action_({
		samples[i].play;
		("play " ++ i).postln;
		});
		);
};

compNumber.do { |i|
	synthText = synthText.add(TextField(window, Rect(0,0,50,20))
	.action_({ |field|
		field.value.postln;			
	});
	);
};

bt1 = Button(window, Rect(0,0,50,20))
.states_([["test"]])
.action_({sample1 = Buffer.loadDialog(s)});

bt2 = Button(window, Rect(0,0,50,20))
.states_([["play test"]])
.action_({sample1.play});

window.front;


)