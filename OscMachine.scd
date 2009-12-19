(
//s.boot;
var window, buttons, buttonsPlay, buttonNumber=8, samples, bt1, bt2,sample1;

window = Window("OscMachine", Rect(350, 100, 55*buttonNumber, 300));
window.view.decorator = FlowLayout(window.view.bounds);

buttons = Array.new(buttonNumber);
samples = Array.new(buttonNumber);
buttonsPlay = Array.new(buttonNumber);
buttonNumber.do { |i|
	samples.add(nil);
};

buttonNumber.do { |i|
	buttons = buttons.add( Button(window,Rect(0,0,50,20))
		.states_([["button " ++ i]])
		.action_({
			var obj = Buffer.loadDialog(s);
			samples.put(i, obj);
		}			 
		));
};
buttonNumber.do { |i|
	buttonsPlay = buttonsPlay.add( Button(window,Rect(0,0,50,20))
		.states_([["play " ++ i]])
		.action_({
			samples[i].play;
			("play " ++ i).postln;
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