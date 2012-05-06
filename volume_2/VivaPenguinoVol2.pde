import ddf.minim.*;
import peasy.*;
import processing.opengl.*;
import SimpleOpenNI.*;

Minim minim;
AudioPlayer song;
AudioInput lineIn;
SimpleOpenNI kinect;

SettingKnob[] knobs;
CameraScafold cams;

PFont font;
int hueVal = 0;

float startPxlColorRange;
float endPxlColorRange;
float startColorRange;
float endColorRange;

void setupKnobs(){
  knobs[SettingKnob.MIN_DEPTH]   = new SettingKnob("Near Depth Limit", 800f, 10f, -10000f, 10000f, '!', int('1'), '1', int('1'));
  knobs[SettingKnob.MAX_DEPTH]   = new SettingKnob("Far Depth Limit", 1400, 10f, -10000f, 10000f, '@', int('2'), '2', int('2'));
  knobs[SettingKnob.MIN_COLOR]   = new SettingKnob("Near Color", 255f, 3f, 0f, 255f, '#', int('3'), '3', int('3'));
  knobs[SettingKnob.MAX_COLOR]   = new SettingKnob("Far Color", 125f, 3f, 0f, 255f, '$', int('4'), '4', int('4'));
}

void setup() {
  size(1024, 768, P3D);

  knobs = new SettingKnob[4];
  cams  = new CameraScafold(this);

  setupKnobs();
  
  startPxlColorRange = knobs[SettingKnob.MIN_DEPTH].getVal();
  endPxlColorRange   = knobs[SettingKnob.MAX_DEPTH].getVal();
  startColorRange    = knobs[SettingKnob.MIN_COLOR].getVal();
  endColorRange      = knobs[SettingKnob.MAX_COLOR].getVal();

  textMode(SCREEN);
  font = createFont("LucidaConsole", 12, false);   // Settings font to smooth makes it look fuzzy

  kinect = new SimpleOpenNI(this);
  kinect.enableDepth(); 

  minim = new Minim(this);
  lineIn = minim.getLineIn(Minim.MONO, 640);
}

void draw() {
  background(0);
  colorMode(HSB);

  cams.update();
  kinect.update();

  setupKinectAxes();
  
  pushMatrix();
  translate(0,0,-1000);
  
  PVector[] depthPoints = kinect.depthMapRealWorld();
  float v;

  for(int r=0; r<depthPoints.length-640; r+=640*8){
    for(int i=0; i<640; ++i){
      PVector p = depthPoints[r+i];
      
      color c = color(hueVal, 255, map(p.z,startPxlColorRange,endPxlColorRange,startColorRange,endColorRange));
      
      if(brightness(c) > 20 && p.z != 0){
        stroke(c);
        v = lineIn.mix.get( (r+i) % 480 ) * 35;
        point(p.x, p.y + v, p.z + v);  
      }
    }
  }
 
  popMatrix();

  if(second() == 59){
    hueVal = hueVal > 255 ? hueVal = 0: hueVal + 1;
  }

  debugDrawInfo("");
  //debugDrawAxis();
}

void setupKinectAxes() {
  translate(width/2, height/2, 0);
  rotateX(radians(180));
}

void keyPressed(){

  for(int i=0; i<knobs.length; ++i){
    knobs[i].update(key, keyCode);
  }
  
  cams.updateKeys(key, keyCode);

  startPxlColorRange = knobs[SettingKnob.MIN_DEPTH].getVal();
  endPxlColorRange   = knobs[SettingKnob.MAX_DEPTH].getVal();
  startColorRange    = knobs[SettingKnob.MIN_COLOR].getVal();
  endColorRange      = knobs[SettingKnob.MAX_COLOR].getVal();
     
  if(key==' '){
    cams.printCameraState();
    printPxlColorDetails();
  }
}


void printPxlColorDetails(){
  println(String.format("pxRange[%d, %d] colorRange[%d, %d]", int(startPxlColorRange), int(endPxlColorRange), int(startColorRange), int(endColorRange)));
}

void debugDrawInfo(String info) {
  textFont(font, 12);
  fill(255, 255, 255);
  String line = int(frameRate) +" fps " + info; 
  text(line, width-(line.length() * 8), height-14);
}

void debugDrawAxis(){
  colorMode(RGB);
  stroke(255, 0, 0);
  line(0, -height, 0, 0, height, 0);
  stroke(0, 255, 0);
  line(-width, 0, 0, width, 0, 0);
  stroke(0,0,255);
  line(0,0,-1000, 0,0,1000);
  colorMode(HSB);
}

void stop(){
  lineIn.close();
  minim.stop();
  super.stop(); 
}

