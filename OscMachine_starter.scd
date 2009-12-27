OscMachine.new
OscMachine.new(10)

(
a = OscMachine.new;
a.setSampleFile(0,"/users/peter/scwork/snd/AltoSax.aif");
a.setOscMsg(0,["/play", "true", 1]);
)

(
a = OscMachine.new(2);
a.setSampleFile(0,"/Users/peter/scwork/snd/AltoSax.aif");
a.setOscMsg(0,["/play", "true", 0]);
a.setSampleFile(1,"/Users/peter/scwork/snd/axaxaxas.aiff");
a.setOscMsg(1,["/play", "true", 1]);
)

(
a = OscMachine.new(2);
a.setSampleFile(0,"/Users/peter/development/of_preRelease_v0.06_xcode_FAT/apps/of_myApps/_b_circleCollisionOscSc_01/bin/data/sounds/36846__EcoDTR__LaserRocket.wav");
a.setOscMsg(0,["/collision", "true", 0]);
a.setSampleFile(1,"/Users/peter/development/of_preRelease_v0.06_xcode_FAT/apps/of_myApps/_b_circleCollisionOscSc_01/bin/data/sounds/51463__smcameron__big_explosion.wav");
a.setOscMsg(1,["/collision", "true", 1]);
)
