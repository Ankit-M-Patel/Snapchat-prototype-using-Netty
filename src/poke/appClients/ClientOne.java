package poke.appClients;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.google.protobuf.ByteString;

import poke.client.ClientCommand;
import poke.client.ClientPrintListener;
import poke.client.comm.CommListener;

public class ClientOne {

    int clientID;
	public ClientOne(int clientID) {
		this.clientID = clientID;
	}

	public void run(ClientCommand cc , String imageName) {

		try {

			byte[] myByeImage;
			BufferedImage originalImage = ImageIO.read(new File(
					"../../resources/clientSendImages/"+imageName));

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "png", baos);
			baos.flush();
			myByeImage = baos.toByteArray();
			baos.close();
			
			String uniqueRequestID = UUID.randomUUID().toString().replaceAll("-", "");			
			ByteString bs = ByteString.copyFrom(myByeImage);
			cc.sendImage(uniqueRequestID, "My Image" + imageName, bs , clientID);
			
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		try {
			ClientCommand cc = new ClientCommand("localhost", 5570);
			CommListener listener = new ClientPrintListener("First Client");
			cc.addListener(listener);

			ClientOne cone = new ClientOne(1);
			
			for(int i=1 ; i < 5 ; i++)
			{
			System.out.println("Sending Image " + i);
			cone.run(cc ,  Integer.toString(i)+".png");
			}
			// we are running asynchronously
			System.out.println("\nExiting in 150 seconds");
			Thread.sleep(150000000);
			System.exit(0);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
