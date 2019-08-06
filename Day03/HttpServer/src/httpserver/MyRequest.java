package httpserver;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class MyRequest {
	private String method;
	private String path;
	private String protocolVersion;
	private Map<String, String> headers = new HashMap<>();

	public MyRequest(BufferedReader bufReader) {
		try {
			String line = bufReader.readLine();
			if(null != line) {
				String[] wordList = line.split(" ");
				if(wordList.length == 3) {
					this.method = wordList[0];
					this.path = wordList[1];
					this.protocolVersion = wordList[2];
				}
				while (true) {
					line = bufReader.readLine();
					if (null != line) {
						int pos = line.indexOf(":");
						if(pos != -1) {
							String key = line.substring(0, pos);
							String val = line.substring(pos + 2);
							headers.put(key, val);
							continue;
						}
					}
					break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
