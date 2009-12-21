OscMachine.new
OscMachine.new(10)

(
a = OscMachine.new;
a.setSampleFile(0,"/users/peter/scwork/snd/AltoSax.aif");
a.setOscMsg(0,["/play", "true", 1]);
)

(
a = OscMachine.new(2);
a.setSampleFile(0,"/users/peter/scwork/snd/AltoSax.aif");
a.setOscMsg(0,["/play", "true", 1]);
a.setSampleFile(1,"/users/peter/scwork/snd/axaxaxas.aiff");
a.setOscMsg(1,["/play", "true", 2]);

)
