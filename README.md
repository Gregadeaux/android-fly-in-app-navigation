Note
=============================
If you use this library, please let me know at Gregadeaux@gmail.com so I can see it in action. I will also feature you in this readme so others can see it.

android-fly-in-app-navigation
=============================

This project serves as a way to implement the fly-in app navigation seen in apps like Facebook, Evernote, and Prixing.

Adding this project to your list of libraries will give you access to a new class, FanView. FanView contains two user-defined layouts that it uses to make this UI pattern. You can define the two layouts seperately just like you would define a regular layout. You then just specifiy the id's of the layouts to FanView and it will handle the animating.

You even define what elements will trigger the navigation to pop up. A simple call to FanView.showMenu() will toggle the display to pop up or close.

Features
=============================
* Open and Closing Animations on the Main View
* Small Open and Closing Animations on the menu to add some reality to it
* Dropshadow on the Main View to make it appear to be over the menu
* Smooth animation transitions for a professional, quality animation
* Empty layouts that allow you to completely design the menu
* Simple usage
* Easy injection into your existing projects

![Example Image][2]

Simple Example
=============================
See this article for a more detailed explanation. http://mobilefanboy.blogspot.com/2012/06/introducing-android-fly-in-app.html
```java
public class SampleActivity extends Activity {
  /** Called when the activity is first created. */
	private FanView fan;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        fan = (FanView) findViewById(R.id.fan_view);
        fan.setViews(R.layout.main, R.layout.fan);
    }
    
    public void unclick(View v) {
    	System.out.println("CLOSE");
    	fan.showMenu();
    }
    
    public void click(View v) {
    	System.out.println("OPEN");
    	fan.showMenu();
    }
    
}
```
test.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.deaux.fan.FanView 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res/com.deaux.fansample"
    android:id="@+id/fan_view"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:orientation="vertical" 
    app:menuSize="200dp" />
```

Example using Fragments
=============================
This project now supports using Fragments (support library fragments) instead of specifying layouts
```java
public class SampleFragmentActivity extends FragmentActivity {
  /** Called when the activity is first created. */
	private FanView fan;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        fan = (FanView) findViewById(R.id.fan_view);
        Fragment fanFrag = new MyFanFragment();
        Fragment contentFrag = new MyContentFragment();
        fan.setFragments(contentFrag, fanFrag);
    }
    
    public void unclick(View v) {
    	System.out.println("CLOSE");
    	fan.showMenu();
    }
    
    public void click(View v) {
    	System.out.println("OPEN");
    	fan.showMenu();
    }
    
}
```


License
=============================
Copyright 2012 Greg Billetdeaux

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

[2]: http://4.bp.blogspot.com/-KiDXDp9khGM/T-EU1FTDpSI/AAAAAAAAArg/TVjTl5rAPCI/s1600/device-2012-06-19-185703.png