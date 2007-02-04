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

// -----------------------------------------------------------------------------------
// Support and fingerprint management routines
// -----------------------------------------------------------------------------------
package com.quientienemail.neotec.i3.huellas.leer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import com.griaule.grfingerjava.FingerprintImage;
import com.griaule.grfingerjava.IFingerEventListener;
import com.griaule.grfingerjava.IImageEventListener;
import com.griaule.grfingerjava.IStatusEventListener;
import com.griaule.grfingerjava.GrFingerJava;
import com.griaule.grfingerjava.GrFingerJavaException;
import com.griaule.grfingerjava.MatchingContext;
import com.griaule.grfingerjava.Template;

public class Util implements IStatusEventListener, IImageEventListener,
		IFingerEventListener {

	private static final long serialVersionUID = 1L;

	private MatchingContext matchContext = null;

	private FormMain ui;

	private boolean autoIdentify = false;

	private boolean autoExtract = true;

	private FingerprintImage fingerprint; // The last acquired image.

	private Template template; // The last extracted template

	private Vector templateVector;

	private boolean captureInitalized = false;
	
	public String idAsociado = "";
	
	public String idUrl = "";

	public Util(FormMain ui) {
		this.ui = ui;
		templateVector = new Vector();

	}

	public void start() throws IOException {
		try {
			// install grfinger java files in a temporary directory
			GrFingerJavaAppletInstaller installer = GrFingerJavaAppletInstaller
					.getInstance();
			// gets grfinger files url
			URL filesDirectory = new URL(ui.getParameter("filesDirectory"));
			installer.install(filesDirectory, true, true);
			
			
			idAsociado = ui.getParameter("idAsociado");
;
		
			idUrl = ui.getParameter("idUrl");
			
			if ((idAsociado == null) || (idUrl == null))  {
				captureInitalized = false;
			    ui.writeLog("ERROR:No se envió idAsociado/idUrl");
			} else {
				// Creates a match context to perform fingerprint minutiae
				// extraction and match.
				matchContext = new MatchingContext();
				// Initializing GrCapture.
				GrFingerJava.initializeCapture(this);
				captureInitalized = true;
				ui.writeLog("** I3 - Captura de huellas LISTO **");
				ui.writeLog("Esperando huellas para:" + idAsociado);
				
			}
			
		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.toString());
		}

	}

	// Finalizes library
	public void stop() {
		try {
			if (matchContext != null) {
				matchContext.destroy();
				matchContext = null;
			}
			if (captureInitalized) {
				GrFingerJava.finalizeCapture();
				captureInitalized = false;
			}
			ui.writeLog("** GrFinger Java Finalized Successfully **");
		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.toString());
		}
	}

	// This function is called every time a fingerprint reader is plugged.
	public void onSensorPlug(String idSensor) {
		ui.writeLog("Sensor: " + idSensor + ". Event: Plugged.");
		try {
			// Start capturing from plugged sensor.
			GrFingerJava.startCapture(idSensor, this, this);
		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.toString());
		}

	}

	// This function is called every time a fingerprint reader is unplugged.
	public void onSensorUnplug(String idSensor) {
		ui.writeLog("Sensor: " + idSensor + ". Event: Unplugged.");
		try {
			// Stop capturing from unplugged sensor.
			GrFingerJava.stopCapture(idSensor);
		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.toString());
		}

	}

	// This function is called every time a finger image is captured
	public void onImageAcquired(String idSensor, FingerprintImage fingerprint) {
		// Signaling that an Image Event occurred.
		ui.writeLog("Sensor: " + idSensor + ". Event: Image Captured.");
		processImage(fingerprint);

	}

	// This function processes a fingerprint image
	private void processImage(FingerprintImage fingerprint) {
		// Display fingerprint image
		ui.showImage(fingerprint);
		this.fingerprint = fingerprint;
		// now we have a fingerprint, so we can extract the template
		ui.enableImage();
		if (autoExtract) {
			// extracting template from image.
			extract();

		}
	}

	// This Function is called every time a finger is placed on sensor.
	public void onFingerDown(String idSensor) {
		// Just signals that a finger event ocurred.
		ui.writeLog("Sensor: " + idSensor + ". Event: Finger Placed.");

	}

	// This Function is called every time a finger is removed from sensor.
	public void onFingerUp(String idSensor) {
		// Just signals that a finger event ocurred.
		ui.writeLog("Sensor: " + idSensor + ". Event: Finger Removed.");

	}

	// Add a fingerprint template to database
	public void enroll() {
		// Adds template to database and gets the ID.
		int id = templateVector.size();
		templateVector.add(id, template);
		ui.writeLog("Fingerprint enrolled with id = " + id);
	}

	// Check current fingerprint against another one in our database
	public void verify(int id) {
		try {
			// Getting template with supplied ID from database.
			if (id < templateVector.size()) {
				// Create a new Template
				Template referenceTamplate = (Template) templateVector.get(id);
				// Comparing templates.
				boolean doesMatched = matchContext.verify(template,
						referenceTamplate);
				if (doesMatched) {
					ui.writeLog("Matched with score = "
							+ matchContext.getScore() + ".");
					// if they match, display matching
					// minutiae/segments/directions
					ui.showImage(GrFingerJava.getBiometricImage(template,
							fingerprint, matchContext));
					return;
				} else
					ui.writeLog("Did not match with score = "
							+ matchContext.getScore() + ".");
			} else
				ui.writeLog("The supplied ID does not exist");
		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.toString());
		}
	}

	// Identify current fingerprint on our database
	public void identify() {
		try {
			// Starting identification process and supplying query template.
			matchContext.prepareForIdentification(template);
			// Getting enrolled templates from database.
			// Iterate over all templates in database
			Iterator iter = templateVector.iterator();
			int i = 0;
			while (iter.hasNext()) {
				// Create a new Template
				Template referenceTamplate = (Template) iter.next();
				// Comparing current template.
				boolean doesMatched = matchContext.identify(referenceTamplate);
				// Checking if query template and reference template match.
				if (doesMatched) {
					ui.writeLog("Fingerprint identified. ID = " + i
							+ ". Score = " + matchContext.getScore() + ".");
					// if they match, display matching
					// minutiae/segments/directions
					ui.showImage(GrFingerJava.getBiometricImage(template,
							fingerprint, matchContext));
					return;
				}
				i++;
			}
			ui.writeLog("Fingerprint not found.");

		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.toString());
		}
	}

	// Extract a fingerprint template from current image
	public void extract() {

		try {
			template = matchContext.extract(fingerprint);
			String msg = "Template extracted successfully. ";
			// write template quality to log
			switch (template.getQuality()) {
			case Template.HIGH_QUALITY:
				msg += "High quality.";
				break;
			case Template.MEDIUM_QUALITY:
				msg += "Medium quality.";
				break;
			case Template.LOW_QUALITY:
				msg += "Low quality.";
				break;
			}
			ui.writeLog(msg);
			ui.enableTemplate();
			// display minutiae/segments/directions into image
			ui.showImage(GrFingerJava
					.getBiometricImage(template, fingerprint));

			// identify fingerprint
			if (autoIdentify)
				identify();
		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.toString());
		}

	}

	// This function clears the database
	public void clearDB() {
		if (templateVector != null) {
			templateVector.clear();
			ui.writeLog("Memory is clear...");
		}
	}

	public void setAutoIdentify(boolean state) {
		autoIdentify = state;
	}

	public void setAutoExtract(boolean state) {
		autoExtract = state;
	}

	// Saves the fingerprint image to a file using an ImageWriterSpi.
	// See ImageIO API.
	public void saveToFile(File file, ImageWriterSpi spi) {

		// Save image.
		try {
			ImageWriter writer = spi.createWriterInstance();
			ImageOutputStream output = ImageIO.createImageOutputStream(file);
			writer.setOutput(output);
			writer.write(fingerprint);
			output.close();
			writer.dispose();
		} catch (IOException e) {
			// write error to log
			ui.writeLog(e.toString());
		}

	}

	// Loads a fingerprint image from file using an ImageReaderSpi.
	// See ImageIO API.
	public void loadFile(File file, int resolution, ImageReaderSpi spi) {
		try {
			ImageReader reader = spi.createReaderInstance();
			ImageInputStream input = ImageIO.createImageInputStream(file);
			reader.setInput(input);
			BufferedImage img = reader.read(0);
			reader.dispose();
			input.close();
			// creates and processes the fingerprint image
			processImage(new FingerprintImage(img, resolution));
		} catch (IOException e) {
			// write error to log
			ui.writeLog(e.toString());
		}

	}

	// Sets match parameters
	public void setParemeters(int identifyThreshold,
			int identifyRotationTolerance, int verifyThreshold,
			int verifyRotationTolorance) {
		try {
			matchContext
					.setIdentificationRotationTolerance(identifyRotationTolerance);
			matchContext.setIdentificationThreshold(identifyRotationTolerance);
			matchContext.setVerificationRotationTolerance(verifyRotationTolorance);
			matchContext.setVerificationThreshold(verifyThreshold);

		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.toString());
		}

	}

	public int getVerifyRotationTolerance() {

		try {
			return matchContext.getVerificationRotationTolerance();
		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.toString());
		}
		return 0;
	}

	public int getVerifyThreshold() {

		try {
			return matchContext.getVerificationThreshold();
		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.toString());
		}
		return 0;
	}

	public int getIdentifyRotationTolerance() {

		try {
			return matchContext.getIdentificationRotationTolerance();
		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.toString());
		}
		return 0;
	}

	public int getIdentifyThreshold() {

		try {
			return matchContext.getIdentificationThreshold();
		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.toString());
		}
		return 0;
	}

	// Sets the colors of biometric display
	public void setBiometricDisplayColors(Color minutiaeColor,
			Color minutiaeMatchColor, Color segmentColor,
			Color segmentMatchColor, Color directionColor,
			Color directionMatchColor) {

		try {
			// set new colors for BiometricDisplay
			GrFingerJava.setBiometricImageColors(minutiaeColor,
					minutiaeMatchColor, segmentColor, segmentMatchColor,
					directionColor, directionMatchColor);
		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.toString());
		}

	}

	// Getting GrFinger Java major version
	public int getMajorVersion() {

		try {
			return GrFingerJava.getMajorVersion();
		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.toString());
		}
		return 0;
	}

	// Getting GrFinger Java minor version
	public int getMinorVersion() {

		try {
			return GrFingerJava.getMinorVersion();
		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.toString());
		}
		return 0;
	}

	// Getting GrFinger Java license type
	public int getLicenseType() {

		try {
			return GrFingerJava.getLicenseType();
		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.toString());
		}
		return 0;
	}

	public RenderedImage getFingerprint() {
		return fingerprint;
	}

}
