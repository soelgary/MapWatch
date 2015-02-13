package com.gsoeller.personalization.maps.image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.im4java.core.CompareCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.DisplayCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

/*
 * Wrapper class for the ImageMagic command line tool
 * 
 */
public class ImageMagic {
	IMOperation op = new IMOperation();
	
	public ImageMagic() throws IOException, InterruptedException, IM4JavaException {
		String iImageDir = "/Users/garysoeller/dev/src/MapsPersonalization/MapsService/src/main/resources/test/img/";
		String img1 = iImageDir + "fe41928c-ab90-48c3-a6ff-725caa7ecc17.png";
		String img2 = iImageDir + "a9302e0a-cbf7-48e5-a150-9acc247c3eb9.png";
		// create command
		ConvertCmd cmd = new ConvertCmd();

		// create the operation, add images and operators/options
		IMOperation op = new IMOperation();
		op.addImage(iImageDir+img1);
		op.resize(800,600);
		//op.addImage(iImageDir+"vneripbnpr.png");

		// execute the operation
		//cmd.run(op);
		String outputImg = iImageDir + "yeppers.png";
		
		String cmd1 = String.format("compare -compose src %s %s %s", img1, img2, outputImg);
		final Process p = Runtime.getRuntime().exec(cmd1);
		
		new Thread(new Runnable() {
		    public void run() {
		        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        String line = null; 

		        try {
		            while ((line = input.readLine()) != null) {
		                System.out.println(line);
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}).start();

		p.waitFor();
		/*
		Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n",
                              envName,
                              env.get(envName));
        }
		System.out.println(EnvironmentUtils.getProcEnvironment());
		String line = "compare";
		CommandLine commandLine = CommandLine.parse(line);
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(0);
		int exitValue = executor.execute(commandLine);
		System.out.println(exitValue);
		*/
	}
}
