/*
 * Created by Eliran Suisa & Eliran Hasin 17_02_18
 */
package CliectHandlerPACK;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ClientHandler {
	void handleClient(InputStream inputClient, OutputStream outputClient) throws IOException;
}
