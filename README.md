# CrashReport
This is a android library that you can monitor your application crashes immediately.


How To Use:
Add this code in your app gradle:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  
  And after that add the dependencie
  
  	dependencies {
	        compile 'com.github.swat13:CrashReport:0.1'
	}




In your application class for initializing the crash report just add this line

        ExceptionHandler.register(this, "URL", "TITLE", "MOBILE");

You can put the url that crash will be sent.
