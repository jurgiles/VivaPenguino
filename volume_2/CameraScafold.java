import peasy.*;
import processing.core.*;

public class CameraScafold implements PConstants{
	PApplet parent; 
	PeasyCam cam;
	String[] CAMERA_STATE_FILE = {"camera_positions.txt"};
	CameraState[] cameraStates = {};
	ConfigFileHandler configs;
	SettingKnob[] knobs;

	float lastPan;
	float timerDelay;
	long  panDelay;
	
	CameraScafold(PApplet parent){	
   		this.parent = parent;

		cam 	= new PeasyCam(parent,0,0,0,1000);
		configs = new ConfigFileHandler(parent, CAMERA_STATE_FILE);
	
		lastPan 	 = parent.minute() + parent.second()/60.0f;
		timerDelay   = 15.0f/60; //min
		panDelay 	 = 16000; //ms
	
		String[] rawCamStates = configs.getContents(CAMERA_STATE_FILE[0]);
		setupCameraStates(rawCamStates);

		knobs = new SettingKnob[2];
		knobs[SettingKnob.TIMER_DELAY] = new SettingKnob("Timer Delay", timerDelay, .5f/60, 0f, 70000f, '%', (int)'5', '5', (int)'5');
		knobs[SettingKnob.PAN_DELAY]   = new SettingKnob("Pan Delay", panDelay, 50f, 0f, 60000f, '^', (int)'6', '6', (int)'6');
	}

	void setupCameraStates(String[] states){
	   for(int i=0; i<states.length; ++i){
	     String[] d = parent.split(states[i], ' ');
	       cam.lookAt(new Float(d[0]), new Float(d[1]), new Float(d[2]), new Float(d[3]), 0L);
	       cam.setRotations(new Double(d[4]), new Double(d[5]), new Double(d[6]));
	       cameraStates = (CameraState[]) parent.append(cameraStates, cam.getState());
	   }
	}

	void update(){
		float now = parent.minute() + parent.second()/60.0f; 

		if(now > timerDelay + lastPan){
			int i = parent.floor(parent.random(cameraStates.length));
			cam.setState(cameraStates[i],panDelay);
			lastPan = now;
			printCameraState(cam);
		}
	}

	void updateKeys(char key, int keyCode){
		for(int i=0; i<knobs.length; ++i){
   			knobs[i].update(key, keyCode);
  		}
	}
		
	void saveCameraStates(){
		//fill
	}

	void printCameraState(PeasyCam cam){
	    float[] r = cam.getRotations();
	    float[] p = cam.getLookAt();
	    parent.println(String.format("cam x: %f y: %f z: %f d: %f", p[0], p[1], p[2], cam.getDistance()));
	    parent.println(String.format("cam p: %f y: %f r: %f", r[0], r[1], r[2]));
	}
	void printCameraState(){
	    float[] r = cam.getRotations();
	    float[] p = cam.getLookAt();
	    parent.println(String.format("%f %f %f %f %f %f %f", p[0], p[1], p[2], cam.getDistance(), r[0], r[1], r[2]));
	}
}