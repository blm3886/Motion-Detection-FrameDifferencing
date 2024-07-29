##Java-based Pixel Manipulation:
Handled pixel-level data in 8-bit per channel RGB format (24 bits per pixel) without using image processing libraries.
##Foreground Extraction: 
Identified and isolated moving objects from the stationary background using frame differencing.
##Composite Creation: 
Overlaid the extracted foreground onto a static background to create a composite image.


##Image Processing with Background Green Screen Pixel Detection and Foreground Edge Detection for Smooth Edges

###Foreground Extraction: 
Worked with .rgb frames in a video file. For each frame, RGB values were converted to HSV for accurate color-based extraction. The program compares consecutive frames to detect and extract the foreground by identifying pixels that have changed. This Java-based program handles pixel-level data without using any image processing libraries.
###Background Overlay:
After detecting the foreground in each frame using frame differencing, background pixels were replaced with a flag value. A static background file was then used to overlay the flagged background pixels and create a composite video.

###Input and Output
To run the program:

###Input 1: A
folder containing .rgb files corresponding to every frame in the video.
###Input 2: 
An .rgb file of a static background.

###Compilation and Execution
To compile and run the Java code:
javac ImageDisplay.java
java ImageDisplay subtraction/background_subtraction_1 input/background_static_1 0
