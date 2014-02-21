#!/usr/bin/jjs
# Get pljusak page
$EXEC("curl -g http://pljusak.com/sticker300.php?stanica=samobor1");
var input = $OUT;

# Extract temperature information
var tempRegEx = /<td class="fav_l300">([-]*\d+\.\d+).*<\/td>/;
var match = tempRegEx.exec(input);
var currentTemp = 'Temperature is ' + match[1] + ' degrees celsius';

# Use voicerss to convert to audio
var voiceUrl = 'http://api.voicerss.org/?key=4f40c34342784996adc9a9b867e37588&hl=en-gb&src=';
voiceUrl += encodeURI(currentTemp);

# Get audio
var voiceCurl = "curl -s -o currentTemp.mp3 \"" + voiceUrl + "\"";
$EXEC(voiceCurl);

# Play the audio
$EXEC('c:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe currentTemp.mp3');