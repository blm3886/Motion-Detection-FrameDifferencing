Features:

Java-based Pixel Manipulation: Handled pixel-level data in 8-bit per channel RGB format (24 bits per pixel) without using image processing libraries.
Foreground Extraction:Identifying and isolating moving objects from the stationary background using frame differencing.
Composite Creation:Overlaying the extracted foreground onto a static background to create a composite image.

The project aims to extract the foreground from a video with a stationary camera by performing frame differencing on consecutive frames and overlaying the extracted foreground onto a static background to create a composite result.The key concept here is that the camera is stationary, providing a stationary background. This allows us to detect moving objects in the foreground using background subtraction. I have processed the sequential frames from the video and detected the pixels that moved with respect to the stationary background, classifying them as foreground.The results are not entirely accurate because the video I used contains many shadows and moving elements in the background, even though the camera is stationary.

Project Details: Image Processing with Background Green Screen Pixel Detection and Foreground Edge Detection for Smooth Edges

Foreground Extraction: In this project, I worked with .rgb frames in a video file. For each frame, I converted RGB values to HSV for accurate color-based extraction. The program compares consecutive frames to detect and extract the foreground by identifying pixels that have changed. This Java-based program handles pixel-level data without using any image processing libraries.
Background Overlay: After detecting the foreground in each frame using frame differencing, I replaced the background pixels with a flag value. I then used a static background file to overlay the flagged background pixels and create a composite video.
