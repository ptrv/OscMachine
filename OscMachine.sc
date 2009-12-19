OscMachine : Object {
	var window, buttons, buttonNumber, samples;
	*new { 
		^super.new.init;
	}

	init {
				
		window = Window("OscMachine", Rect(200, 400, 400, 300));
		window.view.decorator = FlowLayout(window.view.bounds);
		buttonNumber = 10;
		buttons = Array.new(buttonNumber);
		samples = Array.new(buttonNumber);
		// buttonNumber.do { |i|
		// 			 	samples.put(i, nil);
		// 			 };
					 
		buttonNumber.do { |i|
		  	buttons = buttons.add( Button(window,Rect(0,0,50,20))
				.states_([["button " ++ i]])
				.action_({
					var obj = Buffer.loadDialog(Server.default);
					samples.add(obj);
				}			 
				));
		};
  
		window.front;
	}
}
