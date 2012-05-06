class SettingKnob{

	final static int MIN_DEPTH = 0;
	final static int MAX_DEPTH = 1;
	final static int MIN_COLOR = 2;
	final static int MAX_COLOR = 3;
	
	final static int TIMER_DELAY = 0;
	final static int PAN_DELAY   = 1;

	String name;
	
	float value;
	float min;
	float max;
	float increment; 

	char turnDownKey;
	int  turnDownCode;
	
	char turnUpKey;
	int  turnUpCode;

	SettingKnob(String name,
				float defaultVal, float increment,
				float min, float max, 
			   	char turnDownKey, int turnDownCode,
			    char turnUpKey, int turnUpCode){
		this.name = name;
		this.value = defaultVal;
		this.min = min;
		this.max = max;
		this.increment = increment;
		this.turnDownKey = turnDownKey;
		this.turnDownCode = turnDownCode;
		this.turnUpKey = turnUpKey;
		this.turnUpCode = turnUpCode;
		//append(names, name);
	}

	void update(char key, int keyCode){		
		if(key == turnUpKey && keyCode == turnUpCode){
			value = value + increment > max ? max : value + increment;
			printVal();
		}else if(key == turnDownKey && keyCode == turnDownCode){
			value = value - increment < min ? min : value - increment;
			printVal();
		}		
	}

	void printVal(){
		System.out.println(String.format("%s = %s ", name, value));
	}

	float getVal(){
		return value;
	}

	String getName(){
		return null;
	}
}