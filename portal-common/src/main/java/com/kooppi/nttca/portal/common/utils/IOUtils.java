package com.kooppi.nttca.portal.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Base64;

import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;

public class IOUtils implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();
	
	public static void createFile(String basePath, String base64data, String fileNameWithExtension) {
		byte[] data = BASE64_DECODER.decode(base64data);
		String fileFullpath = basePath + "/" + fileNameWithExtension;
		OutputStream os = null;
		try {
			createParentFoldersIfNotExist(fileFullpath);
			File file = new File(fileFullpath);
			if(!file.exists()) {
				os = new FileOutputStream(file);
				os.write(data, 0, data.length);
			} else {
				PortalExceptionUtils.throwNow(PortalErrorCode.FILE_NAME_DEPLICATE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void createParentFoldersIfNotExist(String path) {
		File parent = new File(path).getParentFile();
		if (!parent.exists())
			parent.mkdirs();
		
	}
	
}
