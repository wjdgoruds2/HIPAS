#include <OneWire.h>
#include <DallasTemperature.h>
#include <Adafruit_NeoPixel.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>

const char* ssid = "CUVIC";
const char* pass = "heycuvic";
String host = "http://scv0319.cafe24.com/hipas";

const long interval = 5000;
unsigned long previousMillis = 0;

WiFiServer server(80);
WiFiClient client2;
HTTPClient http;

#ifdef __AVR__
#include <avr/power.h> // Required for 16 MHz Adafruit Trinket
#endif
#define PIN 2 // On Trinket or Gemma, suggest changing this to 1
#define NUMPIXELS 16 // Popular NeoPixel ring size
Adafruit_NeoPixel pixels(NUMPIXELS, PIN, NEO_GRB + NEO_KHZ800);
#define DELAYVAL 500 // Time (in milliseconds) to pause between pixels
#define ONE_WIRE_BUS 4
OneWire oneWire(ONE_WIRE_BUS);
DallasTemperature sensors(&oneWire);
DeviceAddress insideThermometer;
void LEDON();
void LEDON2();
void LEDON3();
void LEDOFF();
void LEDColor(int temp, int gas);

int GasPin = A0; 

void setup(void)
{
    Serial.begin(115200);
    WiFi.mode(WIFI_STA);
    WiFi.begin(ssid, pass);
    while(WiFi.status() != WL_CONNECTED){
      delay(500);
    }
   
    Serial.println(WiFi.localIP());
    server.begin();
    #if defined(__AVR_ATtiny85__) && (F_CPU == 16000000)
      clock_prescale_set(clock_div_1);  
    #endif
      pixels.begin(); // INITIALIZE NeoPixel strip object (REQUIRED)
    sensors.begin();
    
    if (!sensors.getAddress(insideThermometer, 0)) {
      Serial.println("Unable to find address for Device 0"); 
    }
    sensors.setResolution(insideThermometer, 9);
    pinMode(GasPin ,INPUT); 
    
}

int printGas(){
  int gas = analogRead(GasPin);
  return gas;  
}
int printTemperature(DeviceAddress deviceAddress)
{
    int tempC = sensors.getTempC(deviceAddress);
    return tempC;
}

void LEDColor(int temp, int gas){
    if(gas>=550) LEDON();
    else if((gas<550&&gas>250) || temp >=30) LEDON2();
    else if((gas<250&&gas>100) || (temp > 25 && temp < 30)) LEDON3();
    else if(gas<100) LEDOFF();
 }
void LEDON(){
    pixels.clear(); // Set all pixel colors to 'off'
    for(int i=0; i<NUMPIXELS; i++) { // For each pixel...
      pixels.setPixelColor(i, pixels.Color(180, 0, 0));//red
      pixels.show();   // Send the updated pixel colors to the hardware.
    }
}

void LEDON2(){
    pixels.clear(); // Set all pixel colors to 'off'
    for(int i=0; i<NUMPIXELS; i++) { // For each pixel...
      pixels.setPixelColor(i, pixels.Color(0, 180, 0));//green
      pixels.show();   // Send the updated pixel colors to the hardware.
    }
}

void LEDON3(){
    pixels.clear(); // Set all pixel colors to 'off'
    for(int i=0; i<NUMPIXELS; i++) { // For each pixel...
      pixels.setPixelColor(i, pixels.Color(0, 0, 180));//blue
      pixels.show();   // Send the updated pixel colors to the hardware.
    }
}

void LEDOFF(){
    pixels.clear(); // Set all pixel colors to 'off'
    for(int i=0; i<NUMPIXELS; i++) { // For each pixel...
    pixels.setPixelColor(i, pixels.Color(0, 0, 0));

    pixels.show();   // Send the updated pixel colors to the hardware.
    }
}

void loop(void)
{ 
  int temp = printTemperature(insideThermometer); // Use a simple function to print out the data
  int gas = printGas();
  int danger = 0;
  unsigned long currentMillis = millis();
  if(currentMillis - previousMillis >=interval){
    if((temp >28 && temp <30) || (gas >150 && gas<250)){
      danger = 0;
    }
    else if((temp>=30) || ( gas >= 250 && gas < 550)){
      danger = 0;
    }
    else if(gas >=550){
      danger = 0;
    }
    else{
      danger = 0;
    }
    sensors.requestTemperatures(); // Send the command to get temperatures
    String phpHost = host+"/sensor.php?temp="+temp+"&gas="+gas+"&danger="+danger;
    String phpHost2 = host+"/getLed.php";
    //Serial.println(phpHost);
    http.begin(client2, phpHost);
    http.setTimeout(1000);
    int httpCode = http.GET();
    if(httpCode > 0){
      if(httpCode == HTTP_CODE_OK){
        String payload = http.getString();
        Serial.println(payload);
        if( payload == " 1"){
          LEDON();
          delay(500);
          LEDON2();
          delay(500);
          LEDON3();
          delay(500);
          LEDOFF();
        }
        else if( payload == " 0"){
          LEDOFF();
        }
       }
     }
     else{
     }
     http.end();
     
  }
  LEDColor(temp,gas);
    if(gas>550){ 
      delay(600);
    }
    else{ 
      delay(6000);
    }
}
