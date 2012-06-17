android-fly-in-app-navigation
=============================

This project serves as a way to implement the fly-in app navigation seen in apps like Facebook, Evernote, and Prixing.

Adding this project to your libraries will give you access to a new class, FanView. FanView contains two user-defined layouts that it uses to make this UI pattern. You can define the two layouts seperately just like you would define a regular layout. You then just specifiy the id's of the layouts to FanView and it will handle the animating.

You even define what elements will trigger the navigation to pop up. A simple call to FanView.showMenu() will toggle the display to pop up or close.