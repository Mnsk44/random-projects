#define SERIAL_BUFFER_SIZE 256

#include <WiFiEsp.h>
#include <Servo.h>

// Emulate Serial1 on pins 6/7 if not present
#ifndef HAVE_HWSERIAL1
#include <SoftwareSerial.h>
SoftwareSerial Serial1(6, 7); // RX, TX
#endif

// Wifi connection stuff
char ssid[] = "WIFI_NAME";            // your network SSID (name)
char pass[] = "WIFI_PASSWORD";        // your network password
int status = WL_IDLE_STATUS;     // the Wifi radio's status
// Esp server
WiFiEspServer server(80);

// Blind moving stuff
bool windowStateAuto = false;
int room = 1;
String header;

// Servo control stuff
Servo myservo;
int pos = 90;
int manualTurn = 90;
bool shouldServoTurn = false;

void setup()
{
  myservo.write(pos);
  myservo.attach(9);
  // initialize serial for debugging
  Serial.begin(115200);
  // initialize serial for ESP module
  Serial1.begin(9600);
  // initialize ESP module
  WiFi.init(&Serial1);

  // check for the presence of the shield
  if (WiFi.status() == WL_NO_SHIELD) {
    //Serial.println(F("WiFi shield not present"));
    // don't continue
    while (true);
  }

  // attempt to connect to WiFi network
  while ( status != WL_CONNECTED) {
    //Serial.print(F("Attempting to connect to WPA SSID: "));
    //Serial.println(ssid);
    // Connect to WPA/WPA2 network
    status = WiFi.begin(ssid, pass);
  }
  //printWifiStatus();

  // start the web server on port 80
  server.begin();
}


void loop()
{
  // listen for incoming clients
  WiFiEspClient client = server.available();
  if (client) {
    // an http request ends with a blank line
    String currentLine = "";
    while (client.connected()) {
      if (client.available()) {
        char c = client.read();
        Serial.write(c);
        header += c;
        // if you've gotten to the end of the line (received a newline
        // character) and the line is blank, the http request has ended,
        // so you can send a reply
        if (c == '\n') {
          if (currentLine.length() == 0) {
            updateVariables();
            // send a standard http response header
            // use \r\n instead of many println statements to speedup data send
            client.print(F("HTTP/1.1 200 OK\r\n"
                           "Content-Type: text/html\r\n"
                           "\r\n"));


            client.print(F("<!DOCTYPE HTML>\r\n"
                           "<html>\r\n"
                           "<head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"; charset=\"utf-8\";/>\r\n"
                           "<link rel=\"icon\" href=\"data:,\">\r\n"
                           "<script>function sliderChange() {var a = document.getElementById('turn_link'); var b = document.getElementById('myRange'); a.href = \"/manual/\" + b.value.toString();}</script>\r\n"
                           "<style>html { font-family: Helvetica; display: inline-block; margin: 0px auto; padding-left: 5%; padding-right: 5%; text-align: left;}\r\n"
                           ".button { background-color: #195B6A; border: none; color: white; padding: 16px 16px; text-align: center;\r\n"
                           "text-decoration: none; font-size: 16px; margin: 2px; cursor: pointer;}\r\n"
                           ".button2 {background-color: #77878A; text-align: center;}\r\n"
                           ".slidecontainer {width: 100%;}\r\n"
                           ".slider {width: 100%; height: 25px; background: #d3d3d3; opacity: 0.7;}\r\n"
                           ".container {display: flex; justify-content: space-between;}</style></head>\r\n"
                           "<center><h1>Sälekaihtimet</h1></center>\r\n"));

            if (room == 0) {
              client.print(F("<p><div class=\"container\"><div><a href=\"/makkari\"><button class=\"button\">Makkari</button></a></div>"
                             "<div><a href=\"/olohuone\"><button class=\"button button2\">Olohuone</button></a></div>"
                             "<div><a href=\"/keittio\"><button class=\"button button2\">Keittiö</button></a></p></div></div>"));
            } else if (room == 1) {
              client.print(F("<p><div class=\"container\"><div><a href=\"/makkari\"><button class=\"button button2\">Makkari</button></a></div>"
                             "<div><a href=\"/olohuone\"><button class=\"button\">Olohuone</button></a></div>"
                             "<div><a href=\"/keittio\"><button class=\"button button2\">Keittiö</button></a></p></div></div>"));
            } else {
              client.print(F("<p><div class=\"container\"><div><a href=\"/makkari\"><button class=\"button button2\">Makkari</button></a></div>"
                             "<div><a href=\"/olohuone\"><button class=\"button button2\">Olohuone</button></a></div>"
                             "<div><a href=\"/keittio\"><button class=\"button\">Keittiö</button></a></p></div></div>"));
            }
            // If the output5State is off, it displays the ON button
            if (windowStateAuto) {
              client.println(F("<p><a href=\"/auto/on\"><button class=\"button\">Auto: on</button></a></p>"));
            } else {
              client.println(F("<p><div class=\"container\"><div><a href=\"/auto/off\"><button class=\"button button2\">Auto: off</button></a></div>"
                               "<div><a href=\"/manual\" id = turn_link><button class=\"button\" >Käännä</button></a></div></div>"
                               "<div class=\"slidecontainer\"><input type=\"range\" min=\"1\" max=\"179\" value=\"90\" class=\"slider\" id=\"myRange\" oninput=\"sliderChange()\"></div>"
                               "<div class=\"container\"><div>Alas</div><div>Auki</div><div>Ylös</div></div></p>"));
            }

            client.print(F("</html>\r\n"));
            break;
          } else { // if you got a newline, then clear currentLine
            currentLine = "";
          }
        } else if (c != '\r') {  // if you got anything else but a carriage return character,
          currentLine += c;      // add it to the end of the currentLine
        }
      }
    }
    // give the web browser time to receive the data
    header = "";
    // close the connection:
    client.stop();
  } else {
    if (windowStateAuto) {
      loopServoAuto();
    } else if (shouldServoTurn) {
      turnServoManual(manualTurn);
    }
  }
}


//void printWifiStatus()
//{
//  // print the SSID of the network you're attached to
//  Serial.print(F("SSID: "));
//  Serial.println(WiFi.SSID());
//
//  // print your WiFi shield's IP address
//  IPAddress ip = WiFi.localIP();
//  Serial.print(F("IP Address: "));
//  Serial.println(ip);
//}

void updateVariables() {
  if (header.indexOf("GET /auto/off") >= 0) {
    windowStateAuto = true;
    shouldServoTurn = false;
  } else if (header.indexOf("GET /auto/on") >= 0) {
    windowStateAuto = false;
  } else if (header.indexOf("GET /makkari") >= 0) {
    room = 0;
  } else if (header.indexOf("GET /olohuone") >= 0) {
    room = 1;
  } else if (header.indexOf("GET /keittio") >= 0) {
    room = 2;
  } else if (header.indexOf("GET /manual") >= 0) {
    if ( header.indexOf("GET /manual/") >= 0 )  {
      manualTurn = header.substring(header.indexOf("GET /manual/") + 12, header.indexOf("GET /manual") + 15).toInt();
      shouldServoTurn = (manualTurn == myservo.read()) ? false : true;
    } else {
      manualTurn = 90;
      shouldServoTurn = (manualTurn == myservo.read()) ? false : true;
    }
  }

}

// Currently just a sweep for poc, going to include ldr-based automation
void loopServoAuto()
{
  for (pos = 0; pos <= 180; pos += 1) { // goes from 0 degrees to 180 degrees
    // in steps of 1 degree
    myservo.write(pos);              // tell servo to go to position in variable 'pos'
    delay(15);                       // waits 15ms for the servo to reach the position
  }
  for (pos = 180; pos >= 0; pos -= 1) { // goes from 180 degrees to 0 degrees
    myservo.write(pos);              // tell servo to go to position in variable 'pos'
    delay(15);                       // waits 15ms for the servo to reach the position
  }
}

// Moving with potentiometer
void turnServoManual(int turnTo)
{
  if ( turnTo < myservo.read() ) {
    for (pos = myservo.read(); pos >= turnTo; pos -= 1) {
      myservo.write(pos);
      delay(15);
    }
  } else if ( turnTo > myservo.read() ) {
    for (pos = myservo.read(); pos <= turnTo; pos += 1) {
      myservo.write(pos);
      delay(15);
    }
  } else {}
}
