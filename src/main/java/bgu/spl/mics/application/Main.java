package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.passiveObjects.JsonInputReader;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) throws IOException, InterruptedException {
		Input TestInput = JsonInputReader.getInputFromJson(args[0]);
		Ewoks ewoks = Ewoks.getInstance();
		ewoks.initialize(TestInput.getEwoks());
		Diary diary = Diary.getInstance();
		CountDownLatch setUp = new CountDownLatch(4);
		Thread HanSolo = new Thread(new HanSoloMicroservice(setUp));
		Thread C3PO = new Thread(new C3POMicroservice(setUp));
		Thread R2D2 = new Thread(new R2D2Microservice(TestInput.getR2D2(),setUp));
		Thread Lando = new Thread(new LandoMicroservice(TestInput.getLando(),setUp));
		HanSolo.start();
		C3PO.start();
		R2D2.start();
		Lando.start();
		setUp.await();
		Thread Leia = new Thread(new LeiaMicroservice(TestInput.getAttacks()));
		Leia.start();
		HanSolo.join();
		C3PO.join();
		R2D2.join();
		Lando.join();
		Leia.join();
		Gson gsonBuilder = new GsonBuilder().create();
		try {
			FileWriter fileWriter = new FileWriter(args[1]);
			gsonBuilder.toJson(diary, fileWriter);
			fileWriter.flush();
			fileWriter.close();
		} catch (Exception fileWriteException) {

		}
	}
}
