# TestEnvironmentManageRow

To run this code, you need the javafx.swing package as well
``` 
javac --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.swing ManageRow.java  
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.swing ManageRow 
```

### Bug
The current bug in this code is that when ``` saveImg(Canvas c, String name) ```  is called, the image saves, but is not captured. I would assume it is an image with how I am using the 
``` .snapshot``` call , or something like that.
