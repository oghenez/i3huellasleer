/*
 -------------------------------------------------------------------------------
 GrFinger Java Sample
 (c) 2006 Griaule Tecnologia Ltda.
 http://www.griaule.com
 -------------------------------------------------------------------------------

 This sample is provided with "GrFinger Java Fingerprint Recognition Library" and
 can't run without it. It's provided just as an example of using GrFinger Java
 Fingerprint Recognition Library and should not be used as basis for any
 commercial product.

 Griaule Tecnologia makes no representations concerning either the merchantability
 of this software or the suitability of this sample for any particular purpose.

 THIS SAMPLE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL GRIAULE BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 You can download the trial version of GrFinger Java from Griaule website.

 These notices must be retained in any copies of any part of this
 documentation and/or sample.

 -------------------------------------------------------------------------------
 */
package com.quientienemail.neotec.i3.huellas.leer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.griaule.grfingerjava.GrFingerJava;
import com.griaule.grfingerjava.GrFingerJavaException;

public class GrFingerJavaAppletInstaller {

	private int os;

	private File destDir;

	private boolean installed = false;

	private static final int WINDOWS_OS = 0;

	private static final int LINUX_OS = 1;

	private static final int UNKNOWN_OS = 2;

	private static GrFingerJavaAppletInstaller installer = null;

	public synchronized static GrFingerJavaAppletInstaller getInstance()
			throws IOException {
		if (installer == null) {
			installer = new GrFingerJavaAppletInstaller();
		}
		return installer;
	}

	private GrFingerJavaAppletInstaller() throws IOException {

		os = getOS();

		// create temporary folder
		destDir = File.createTempFile("GrFingerJavaApplet", "tmp");
		destDir.delete();
		destDir.mkdir();
		destDir.deleteOnExit();

		// set library path
		GrFingerJava.setNativeLibrariesDirectory(destDir);

	}

	public synchronized void install(URL filesDirectory,
			boolean installBioIFiles, boolean installSecugenFiles)
			throws IOException, UnsupportedOperationException,
			GrFingerJavaException {

		if (installed)
			return;

		// install core files
		if (os == WINDOWS_OS) {
			copyFile(filesDirectory, "grfingerjava.dll");
			copyFile(filesDirectory, "pthreadVC2.dll");

		} else if (os == LINUX_OS) {
			copyFile(filesDirectory, "libgrfingerjava.so");
		} else
			throw new UnsupportedOperationException(
					"Operating System not supported");

		// install the license
		copyFile(filesDirectory, "GrFingerJavaLicenseAgreement.txt");
		// set license path
		GrFingerJava.setLicenseDirectory(destDir);

		if (installBioIFiles)
			installBioIFiles(filesDirectory);
		if (installSecugenFiles)
			installSecugenFiles(filesDirectory);

		installed = true;

	}

	private void copyFile(URL filesDirectory, String fileName)
			throws IOException {
		InputStream src = new URL(filesDirectory, fileName).openStream();
		File destFile = new File(destDir, fileName);
		FileOutputStream dest = new FileOutputStream(destFile);

		byte[] buffer = new byte[4096];
		int read = src.read(buffer);
		while (read != -1) {
			dest.write(buffer, 0, read);
			read = src.read(buffer);
		}
		src.close();
		dest.close();
		destFile.deleteOnExit();

	}

	private int getOS() {
		String osName = System.getProperty("os.name");
		String arch = System.getProperty("os.arch");

		if ((arch.equals("x86")) || (arch.equals("i386"))
				|| (arch.equals("i486")) || (arch.equals("i586"))
				|| (arch.equals("i686"))) {
			if (osName.startsWith("Windows"))
				return WINDOWS_OS;
			else if (osName.startsWith("Linux"))
				return LINUX_OS;
		}

		return UNKNOWN_OS;
	}

	private void installSecugenFiles(URL filesDirectory) throws IOException {
		if (os == WINDOWS_OS) {
			copyFile(filesDirectory, "fplib.dll");
			copyFile(filesDirectory, "extdllR.dll");
			copyFile(filesDirectory, "vrfdllR.dll");
			load("fplib.dll");
			load("extdllR.dll");
			load("vrfdllR.dll");
		}
	}

	private void load(String lib) {
		System.load(destDir.getAbsolutePath() + File.separator + lib);

	}

	private void installBioIFiles(URL filesDirectory) throws IOException {
		if (os == WINDOWS_OS) {
			copyFile(filesDirectory, "CaptureSDK.dll");
			load("CaptureSDK.dll");
		}
	}

}