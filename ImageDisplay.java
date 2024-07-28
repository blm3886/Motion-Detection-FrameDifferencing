
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner; // Import the Scanner class to read text files
import java.lang.Thread;

public class ImageDisplay {

	JFrame frame;
	JLabel lbIm1;
	BufferedImage imgOne;
	BufferedImage bckImag;
	
	static final int WIDTH = 640; // default image width and height
	static final int HEIGHT = 480;
	
	//Function to Convert RGB to HSV
	private boolean RGB_To_HSV(double r, double g, double b){
		   // R, G, B values are divided by 255
     // to change the range from 0..255 to 0..1
     r = r / 255.0;
     g = g / 255.0;
     b = b / 255.0;

     // h, s, v = hue, saturation, value
     double cmax = Math.max(r, Math.max(g, b)); // maximum of r, g, b
     double cmin = Math.min(r, Math.min(g, b)); // minimum of r, g, b
     double diff = cmax - cmin; // diff of cmax and cmin.
     double h = -1, s = -1;
      
     // if cmax and cmax are equal then h = 0
     if (cmax == cmin)
         h = 0;

     // if cmax equal r then compute h
     else if (cmax == r)
         h = (60 * ((g - b) / diff) + 360) % 360;

     // if cmax equal g then compute h
     else if (cmax == g)
         h = (60 * ((b - r) / diff) + 120) % 360;

     // if cmax equal b then compute h
     else if (cmax == b)
         h = (60 * ((r - g) / diff) + 240) % 360;

     // if cmax equal zero
     if (cmax == 0)
         s = 0;
     else
         s = (diff / cmax) * 100;

     // compute v
     double v = cmax * 100;
		if ((h >= 50 && h <= 180)&& (s >= 20 && s <= 100) && (v >= 40 && v <= 100)){
			return true;
		}	
	    return false;
	}
	
  
 private double[] RGB_To_HSV2(double r, double g, double b){
   // R, G, B values are divided by 255
  // to change the range from 0..255 to 0..1
  r = r / 255.0;
  g = g / 255.0;
  b = b / 255.0;

  // h, s, v = hue, saturation, value
  double cmax = Math.max(r, Math.max(g, b)); // maximum of r, g, b
  double cmin = Math.min(r, Math.min(g, b)); // minimum of r, g, b
  double diff = cmax - cmin; // diff of cmax and cmin.
  double h = -1, s = -1;
   
  // if cmax and cmax are equal then h = 0
  if (cmax == cmin)
      h = 0;

  // if cmax equal r then compute h
  else if (cmax == r)
      h = (60 * ((g - b) / diff) + 360) % 360;

  // if cmax equal g then compute h
  else if (cmax == g)
      h = (60 * ((b - r) / diff) + 120) % 360;

  // if cmax equal b then compute h
  else if (cmax == b)
      h = (60 * ((r - g) / diff) + 240) % 360;

  // if cmax equal zero
  if (cmax == 0)
      s = 0;
  else
      s = (diff / cmax) * 100;

  // compute v
  double v = cmax * 100;
  double[] res = new double[3];
  res[0] = h;
  res[1] = s;
  res[2] = v;
  return res;
}


	private void readImageRGB(String imgPath, BufferedImage img, String imgPath2, BufferedImage bckImg)
	{
		try
		{
			int frameLength = WIDTH*HEIGHT*3;
			File file = new File(imgPath);
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			raf.seek(0);
			long len = frameLength;
			byte[] bytes = new byte[(int) len];
			raf.read(bytes);
			
			//background Image
			int frameLength1 = WIDTH*HEIGHT*3;
			File file1 = new File(imgPath2);
			RandomAccessFile raf1 = new RandomAccessFile(file1, "r");
			raf1.seek(0);
			long len1 = frameLength1;
			byte[] bytes1 = new byte[(int) len1];
			raf1.read(bytes1);
			
			int ind = 0;
			for(int y = 0; y < HEIGHT; y++)
			{
				boolean edge = false;
				for(int x = 0; x < WIDTH; x++)
				{
					byte a = 0;
					byte r = bytes[ind];
					byte g = bytes[ind+HEIGHT*WIDTH];
					byte b = bytes[ind+HEIGHT*WIDTH*2]; 
					
					
					//background
					byte Ba = 0;
					byte Br = bytes1[ind];
					byte Bg = bytes1[ind+HEIGHT*WIDTH];
					byte Bb = bytes1[ind+HEIGHT*WIDTH*2];
					
					
					int pix; 
					
					if(RGB_To_HSV((r & 0xff),(g & 0xff),(b & 0xff))){
						// replace with baclground pixel.
						if(edge){
							edge = false;
							int nextInd = ind-1;
							int C = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
							int B1 = 0xff000000 | ((0 & 0xff) << 16) | ((g & 0xff) << 8) | (0 & 0xff);
							//Getting the next pixel
							byte mR = bytes[nextInd];
							byte mG = bytes[nextInd+HEIGHT*WIDTH];
							byte mB = bytes[ind+HEIGHT*WIDTH*2];
							int F = 0xff000000 | ((mR & 0xff) << 16) | ((mG & 0xff) << 8) | (mB & 0xff);
							int B2 = 0xff000000 | ((Br & 0xff) << 16) | ((Bg & 0xff) << 8) | (Bb & 0xff);
							
							int A = C-B1/F-B2;
							pix = A & 0xff000000 | ((Br & 0xff) << 16) | ((Bg & 0xff) << 8) | (Bb & 0xff);	
						}	
						else {
						pix = 0xff000000 | ((Br & 0xff) << 16) | ((Bg & 0xff) << 8) | (Bb & 0xff);
						}
						//pix = 0xff000000 | ((Br & 0xff) << 16) | ((Bg & 0xff) << 8) | (Bb & 0xff);
					}
					else {
						//Adding the Foreground
						if(!edge){
							int nextInd = ind+1;
							edge = true;
							int C = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
							int B1 = 0xff000000 | ((0 & 0xff) << 16) | ((g & 0xff) << 8) | (0 & 0xff);
							//Getting the previous pixel
							byte mR = bytes[nextInd];
							byte mG = bytes[nextInd+HEIGHT*WIDTH];
							byte mB = bytes[ind+HEIGHT*WIDTH*2];
							int F = 0xff000000 | ((mR & 0xff) << 16) | ((mG & 0xff) << 8) | (mB & 0xff);
							int B2 = 0xff000000 | ((Br & 0xff) << 16) | ((Bg & 0xff) << 8) | (Bb & 0xff);
							
							int A = C-B1/F-B2;
							pix = A & 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
						}
						else {
							pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
						}
						//pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					}
					img.setRGB(x,y,pix);
					ind++;
				}
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}



	public void showIms(ArrayList<String> FG_Files, String filePath1,ArrayList<String> BG_Files, String filePath2)throws InterruptedException{		
		
		// Use label to display the image
		frame = new JFrame();
		GridBagLayout gLayout = new GridBagLayout();
		frame.getContentPane().setLayout(gLayout);
		lbIm1 = new JLabel();

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		
		ArrayList<BufferedImage> imageArray = new ArrayList<>();
		for(int i =0; i < FG_Files.size(); i++) {
			imgOne  = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			bckImag = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			String fp_FG = filePath1+"/"+FG_Files.get(i);
			String fp_BG = filePath2+"/"+BG_Files.get(i);
			readImageRGB(fp_FG, imgOne, fp_BG, bckImag);
			imageArray.add(imgOne);
		}
		
		
		frame.getContentPane().add(lbIm1, c);
		frame.setVisible(true);
		
        for(BufferedImage img: imageArray) {
		lbIm1.setIcon(new ImageIcon(img));
		frame.pack();
		Thread.sleep(42);
        }
	}
	
	//Implementation 2 background using HSV
	private void setBackground2(String imgPath, String imgPath2,String imgPath3,BufferedImage img){
		try
		{
			int frameLength = WIDTH*HEIGHT*3;
			File file = new File(imgPath);
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			raf.seek(0);
			long len = frameLength;
			byte[] bytes = new byte[(int) len];
			raf.read(bytes);
			
			//Frame 2
			int frameLength1 = WIDTH*HEIGHT*3;
			File file1 = new File(imgPath2);
			RandomAccessFile raf1 = new RandomAccessFile(file1, "r");
			raf1.seek(0);
			long len1 = frameLength1;
			byte[] bytes1 = new byte[(int) len1];
			raf1.read(bytes1);
			
			//Background
			int frameLength3 = WIDTH*HEIGHT*3;
			File file3 = new File(imgPath3);
			RandomAccessFile raf3 = new RandomAccessFile(file3, "r");
			raf3.seek(0);
			long len3 = frameLength3;
			byte[] bytes3 = new byte[(int) len3];
			raf3.read(bytes3);
			
			int ind = 0;
			int count = 0;
			for(int y = 0; y < HEIGHT; y++)
			{
				for(int x = 0; x < WIDTH; x++)
				{
					byte a = 0;
					byte r = bytes[ind];
					byte g = bytes[ind+HEIGHT*WIDTH];
					byte b = bytes[ind+HEIGHT*WIDTH*2]; 
					
					//ImageTwo
					byte a2 = 0;
					byte r2 = bytes1[ind];
					byte g2 = bytes1[ind+HEIGHT*WIDTH];
					byte b2 = bytes1[ind+HEIGHT*WIDTH*2];
					
					//backGround
					byte B = 0;
					byte Br = bytes3[ind];
					byte Bg = bytes3[ind+HEIGHT*WIDTH];
					byte Bb = bytes3[ind+HEIGHT*WIDTH*2];
					
					//double[] frame1 = RGB_To_HSV2(r & 0xff,g & 0xff,b & 0xff);
					//double[] frame2 = RGB_To_HSV2(r2 & 0xff,g2 & 0xff,b2 & 0xff);
					
					//System.out.println(frame1[0]+"-"+frame1[1]+"_"+frame1[1]);
					//System.out.println(Math.abs(frame1[0] - frame2[0]));
					//System.out.println(Math.abs(frame1[0] - frame2[0]));
					//int Hdiff = (int) Math.abs(frame2[0] - frame1[0]);
					//int Sdiff = (int)Math.abs(frame2[1] - frame1[1]);
					//int Vdiff = (int)Math.abs(frame2[2] - frame1[2]);
					
					//System.out.println(Hdiff+" "+Sdiff+" "+Vdiff);
					
					
					int Rdiff = Math.abs((r & 0xff) - (r2 & 0xff));
					int Gdiff = Math.abs((g & 0xff) - (g2 & 0xff));	
					int Bdiff = Math.abs((b & 0xff) - (b2 & 0xff));	
					
					//System.out.println(diff);	
					int pix = 0;
					if(Rdiff<= 3 && Gdiff<=3 && Bdiff<=3){
						//&& (Sdiff == 0.0) && (Vdiff == 0.0)){//pix = 0xff000000 | ((0 & 0xff) << 16) | ((0 & 0xff) << 8) | (0 & 0xff);
						pix = 0xff000000 | ((Br & 0xff) << 16) | ((Bg & 0xff) << 8) | (Bb & 0xff);
						count++;
					}
					else{
						pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					}
					img.setRGB(x,y,pix);
					ind++;
				}
			}
			//System.out.println(count);
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/*
	private void setBackGround(String imgPath, String imgPath2,String imgPath3,BufferedImage img)
	{
		try
		{
			int frameLength = WIDTH*HEIGHT*3;
			File file = new File(imgPath);
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			raf.seek(0);
			long len = frameLength;
			byte[] bytes = new byte[(int) len];
			raf.read(bytes);
			
			//Frame 2
			int frameLength1 = WIDTH*HEIGHT*3;
			File file1 = new File(imgPath2);
			RandomAccessFile raf1 = new RandomAccessFile(file1, "r");
			raf1.seek(0);
			long len1 = frameLength1;
			byte[] bytes1 = new byte[(int) len1];
			raf1.read(bytes1);
			
			//Background
			int frameLength3 = WIDTH*HEIGHT*3;
			File file3 = new File(imgPath3);
			RandomAccessFile raf3 = new RandomAccessFile(file3, "r");
			raf3.seek(0);
			long len3 = frameLength3;
			byte[] bytes3 = new byte[(int) len3];
			raf3.read(bytes3);
			
			int ind = 0;
			for(int y = 0; y < HEIGHT; y++)
			{
				for(int x = 0; x < WIDTH; x++)
				{
					byte a = 0;
					byte r = bytes[ind];
					byte g = bytes[ind+HEIGHT*WIDTH];
					byte b = bytes[ind+HEIGHT*WIDTH*2]; 
					
					
					//ImageTwo
					byte a2 = 0;
					byte r2 = bytes1[ind];
					byte g2 = bytes1[ind+HEIGHT*WIDTH];
					byte b2 = bytes1[ind+HEIGHT*WIDTH*2];
					
					//backGround
					byte B = 0;
					byte Br = bytes3[ind];
					byte Bg = bytes3[ind+HEIGHT*WIDTH];
					byte Bb = bytes3[ind+HEIGHT*WIDTH*2];
					
					int Rdiff = Math.abs((r & 0xff) - (r2 & 0xff));
					int Gdiff = Math.abs((g & 0xff) - (g2 & 0xff));	
					int Bdiff = Math.abs((b & 0xff) - (b2 & 0xff));	
					
					//System.out.println(Rdiff+" "+Gdiff+" "+Bdiff);	
					int pix = 0;
					if((Rdiff >= 0 && Rdiff <= 30) && (Gdiff >= 0 && Gdiff <=30) && (Bdiff >= 0 && Bdiff <=30)){
						//pix = 0xff000000 | ((0 & 0xff) << 16) | ((0 & 0xff) << 8) | (0 & 0xff);
						pix = 0xff000000 | ((Br & 0xff) << 16) | ((Bg & 0xff) << 8) | (Bb & 0xff);
					}
					else{
						pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					}
					img.setRGB(x,y,pix);
					ind++;
				}
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}*/
	
    public void showImsI2(ArrayList<String> FG_Files, String filePath1,ArrayList<String> BG_Files, String filePath2)throws InterruptedException{		
		ArrayList<BufferedImage> imageArray = new ArrayList<>();
		for(int i =1; i < FG_Files.size(); i++){
			imgOne  = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			//bckImag = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			String fp_FG_1 = filePath1+"/"+FG_Files.get(i-1);
			String fp_FG_2 = filePath1+"/"+FG_Files.get(i);
			String fp_BG = filePath2+"/"+BG_Files.get(i);
			//readImageRGB(fp_FG, imgOne, fp_BG, bckImag);
			setBackground2(fp_FG_1,fp_FG_2,fp_BG, imgOne);
			//setBackGround(fp_FG_1,fp_FG_2,fp_BG, imgOne);
			imageArray.add(imgOne);
		}
		
		// Use label to display the image
    	frame = new JFrame();
    	GridBagLayout gLayout = new GridBagLayout();
    	frame.getContentPane().setLayout(gLayout);
    	lbIm1 = new JLabel();

    	GridBagConstraints c = new GridBagConstraints();
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.anchor = GridBagConstraints.CENTER;
    	c.weightx = 0.5;
    	c.gridx = 0;
    	c.gridy = 0;
         
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridx = 0;
    	c.gridy = 1;
    	frame.getContentPane().add(lbIm1, c);
    	frame.setVisible(true);
    	
        for(BufferedImage img: imageArray) {
        	lbIm1.setIcon(new ImageIcon(img));
        	frame.pack();
        	Thread.sleep(42);
        }
        
	}
	
    /*
    public void DisplayVideo(){
    	// Use label to display the image
    	frame = new JFrame();
    	GridBagLayout gLayout = new GridBagLayout();
    	frame.getContentPane().setLayout(gLayout);
    	lbIm1 = new JLabel();

    	GridBagConstraints c = new GridBagConstraints();
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.anchor = GridBagConstraints.CENTER;
    	c.weightx = 0.5;
    	c.gridx = 0;
    	c.gridy = 0;

    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridx = 0;
    	c.gridy = 1;
    }*/
	
	
	public static void FilesForFolder(File folder, ArrayList<String> fileStore){
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				FilesForFolder(fileEntry,fileStore);
			} else{
			fileStore.add(fileEntry.getName());	
        }
      }
	}

	public static void main(String[] args) {
		// Read the File Name 1
		String folder1 = args[0];
				
		// Read the File Name 2
		String folder2 = args[1];
		
		//String mode = args[2];
		int mode = Integer. valueOf(args[2]);
		
		//Opening File 1 and storing rgb files names in arrayList;
		File folder = new File(folder1);
		ArrayList<String> FG_files = new ArrayList<>(); // Foreground file names.
		FilesForFolder(folder,FG_files);
		Collections.sort(FG_files);

		
		//Background Files 
		File f2 = new File(folder2);
		ArrayList<String> BG_files = new ArrayList<>(); // Foreground file names.
		FilesForFolder(f2,BG_files);
		Collections.sort(BG_files);
	
		ImageDisplay ren = new ImageDisplay();
		try {
			if(mode == 1) {
			ren.showIms(FG_files,folder1,BG_files,folder2);
			}
			else
			ren.showImsI2(FG_files,folder1,BG_files,folder2);	
				
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		
	}

}
