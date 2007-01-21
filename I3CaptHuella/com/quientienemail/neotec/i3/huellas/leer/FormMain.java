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
// GUI routines: main form
// -----------------------------------------------------------------------------------
package com.quientienemail.neotec.i3.huellas.leer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageReaderWriterSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import com.griaule.grfingerjava.GrFingerJava;




public class FormMain extends JApplet {

	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;

	private JMenuBar jJMenuBar = null;

	private JMenu jMenu = null;

	private JMenuItem jMenu1 = null;

	private JMenuItem jMenu2 = null;

	private JMenuItem jMenuItem = null;

	private JMenuItem jMenuItem1 = null;

	private JSplitPane jSplitPane = null;

	private JPanel jPanel1 = null;

	private JPanel jPanel2 = null;

	private JButton jButton = null;

	private JButton jButton1 = null;

	private JButton jButton2 = null;

	private JButton jButton3 = null;

	private JButton jButton4 = null;

	private JButton jButton5 = null;

	private JCheckBox jCheckBox = null;

	private JCheckBox jCheckBox1 = null;

	private JTextArea jTextArea = null;

	private Util util;

	private JScrollPane jScrollPane = null;

	private FormOptions pane = null;

	public Image image = null;

	public void init() {
		initialize();
		this.util = new Util(this);
	}

	public void start() {

		try {
			util.start();
		} catch (IOException e) {
			writeLog(e.getMessage());
			writeLog("Applet will be disabled.");
		}
	}

	public void stop() {
		util.stop();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {

		// creating this applet's GUI.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		// Initialize interface
		setJMenuBar(getJJMenuBar());
		setSize(515, 560);
		setContentPane(getJContentPane());

	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getJScrollPane(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getJSplitPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getJMenu());
			jJMenuBar.add(getJMenu1());
			jJMenuBar.add(getJMenu2());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenu() {
		if (jMenu == null) {
			jMenu = new JMenu();
			jMenu.setText("Imagen...");
			jMenu.add(getJMenuItem());
			jMenu.add(getJMenuItem1());
		}
		return jMenu;
	}

	/**
	 * This method initializes jMenu1
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenuItem getJMenu1() {
		if (jMenu1 == null) {
			jMenu1 = new JMenuItem();
			jMenu1.setText("Options...");
			jMenu1.setMaximumSize(new Dimension(70, 100));
			jMenu1.setSize(70, 0);
			jMenu1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (pane == null)
						pane = new FormOptions();

					pane.saveState();

					pane.setIdentifyThreshold(util.getIdentifyThreshold());
					pane.setIdentifyRotationTolerance(util
							.getIdentifyRotationTolerance());
					pane.setVerifyThreshold(util.getVerifyThreshold());
					pane.setVerifyRotationTolorance(util
							.getVerifyRotationTolerance());

					JDialog dialog = pane.createDialog(rootPane, "Options");

					dialog.setVisible(true);
					dialog.dispose();

					if ((pane.getValue() != null)
							&& ((Integer) pane.getValue()).intValue() == JOptionPane.OK_OPTION) {
						try {
							util.setParemeters(pane.getIdentifyThreshold(),
									pane.getIdentifyRotationTolerance(), pane
											.getVerifyThreshold(), pane
											.getVerifyRotationTolorance());

							Color minutiaeColor;
							Color minutiaeMatchColor;
							Color segmentColor;
							Color segmentMatchColor;
							Color directionColor;
							Color directionMatchColor;

							if (pane.mustShowMinutiae())
								minutiaeColor = pane.getMinutiaeColor();
							else
								minutiaeColor = null;

							if (pane.mustShowMinutiaeMatch())
								minutiaeMatchColor = pane
										.getMinutiaeMatchColor();
							else
								minutiaeMatchColor = null;

							if (pane.mustShowSegment())
								segmentColor = pane.getSegmentColor();
							else
								segmentColor = null;

							if (pane.mustShowSegmentMatch())
								segmentMatchColor = pane.getSegmentMatchColor();
							else
								segmentMatchColor = null;

							if (pane.mustShowDirection())
								directionColor = pane.getDirectionColor();
							else
								directionColor = null;

							if (pane.mustShowDirectionMatch())
								directionMatchColor = pane
										.getDirectionMatchColor();
							else
								directionMatchColor = null;

							util.setBiometricDisplayColors(minutiaeColor,
									minutiaeMatchColor, segmentColor,
									segmentMatchColor, directionColor,
									directionMatchColor);

						} catch (NumberFormatException e1) {
							JOptionPane.showMessageDialog(rootPane,
									"Invalid values.", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						pane.restoreState();
					}

				}
			});
		}
		return jMenu1;
	}

	private JMenuItem getJMenu2() {
		if (jMenu2 == null) {
			jMenu2 = new JMenuItem();
			jMenu2.setText("Version");
			jMenu2.setMaximumSize(new Dimension(70, 100));
			jMenu2.setSize(70, 0);
			jMenu2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JOptionPane
							.showMessageDialog(
									rootPane,
									"GrFinger Java version "
											+ util.getMajorVersion()
											+ "."
											+ util.getMinorVersion()
											+ "\n"
											+ "License type is "
											+ ((util.getLicenseType() == GrFingerJava.GRFINGER_JAVA_FULL) ? "FULL"
													: "LIGHT") + ".",
									"GrFinger Java Version",
									JOptionPane.PLAIN_MESSAGE);

				}
			});
		}
		return jMenu2;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem() {
		if (jMenuItem == null) {
			jMenuItem = new JMenuItem();
			jMenuItem.setText("Save");
			jMenuItem.setEnabled(false);
			jMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setAcceptAllFileFilterUsed(false);
					Iterator iter = IIORegistry.getDefaultInstance()
							.getServiceProviders(ImageWriterSpi.class, false);
					while (iter.hasNext()) {
						ImageWriterSpi spi = (ImageWriterSpi) iter.next();
						if (spi.canEncodeImage(util.getFingerprint()))
							fileChooser.setFileFilter(new ImageFileFilter(spi));
					}
					if (fileChooser.showSaveDialog(rootPane) == JFileChooser.APPROVE_OPTION) {
						ImageWriterSpi spi = (ImageWriterSpi) ((ImageFileFilter) fileChooser
								.getFileFilter()).getSpi();
						util.saveToFile(fileChooser.getSelectedFile(), spi);
					}
				}
			});
		}
		return jMenuItem;
	}

	/**
	 * This method initializes jMenuItem1
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem1() {
		if (jMenuItem1 == null) {
			jMenuItem1 = new JMenuItem();
			jMenuItem1.setText("Load From File");
			jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setAcceptAllFileFilterUsed(false);
					Iterator iter = IIORegistry.getDefaultInstance()
							.getServiceProviders(ImageReaderSpi.class, false);
					while (iter.hasNext()) {
						fileChooser.setFileFilter(new ImageFileFilter(
								(ImageReaderSpi) iter.next()));
					}
					if (fileChooser.showOpenDialog(rootPane) == JFileChooser.APPROVE_OPTION) {
						String resolution = JOptionPane.showInputDialog(
								rootPane, "What is the image resolution?",
								"Resolution", JOptionPane.QUESTION_MESSAGE);
						if (resolution != null) {
							try {
								ImageReaderSpi spi = (ImageReaderSpi) ((ImageFileFilter) fileChooser
										.getFileFilter()).getSpi();
								util.loadFile(fileChooser.getSelectedFile(),
										Integer.parseInt(resolution), spi);
							} catch (NumberFormatException e1) {
								writeLog("Invalid resolution.");
							}
						}
					}

				}
			});
		}
		return jMenuItem1;
	}

	/**
	 * This method initializes jSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setRightComponent(getJPanel1());
			jSplitPane.setDividerSize(0);
			jSplitPane.setDividerLocation(380);
			jSplitPane.setLeftComponent(getJPanel2());
			jSplitPane.setResizeWeight(1.0D);

		}
		return jSplitPane;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			GridLayout gridLayout1 = new GridLayout();
			jPanel1 = new JPanel();
			jPanel1.setLayout(gridLayout1);
			gridLayout1.setColumns(1);
			gridLayout1.setRows(0);
			gridLayout1.setHgap(0);
			gridLayout1.setVgap(5);

			jPanel1.setBorder(BorderFactory.createEmptyBorder(60, 5, 5, 5));

			jPanel1.add(new JLabel());
			jPanel1.add(getJButton(), null);
			jPanel1.add(getJButton1(), null);
			jPanel1.add(getJButton2(), null);
			jPanel1.add(getJButton3(), null);
			jPanel1.add(new JLabel());
			jPanel1.add(getJButton4(), null);
			jPanel1.add(getJButton5(), null);
			jPanel1.add(getJCheckBox(), null);
			jPanel1.add(getJCheckBox1(), null);
		}
		return jPanel1;
	}

	private JPanel getJPanel2() {
		if (jPanel2 == null) {

			jPanel2 = new JPanel() {

				private static final long serialVersionUID = 1L;

				public void paint(Graphics arg0) {
					super.paint(arg0);
					if (image != null)
						arg0.drawImage(image, 0, 0, jPanel2.getWidth(), jPanel2
								.getHeight(), null);

				}

			};

		}
		return jPanel2;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Enroll");
			jButton.setName("jButton");
			jButton.setEnabled(false);
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					util.enroll();
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setName("jButton1");
			jButton1.setText("Verify");
			jButton1.setEnabled(false);
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String id = JOptionPane.showInputDialog(rootPane,
							"Enter the ID to verify.", "Verify",
							JOptionPane.QUESTION_MESSAGE);
					if (id != null) {
						try {
							util.verify(Integer.parseInt(id));
						} catch (NumberFormatException e1) {
							writeLog("Invalid ID.");
						}
					}
				}
			});

		}
		return jButton1;
	}

	/**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setText("Identify");
			jButton2.setEnabled(false);
			jButton2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					util.identify();
				}
			});
		}
		return jButton2;
	}

	/**
	 * This method initializes jButton3
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton3() {
		if (jButton3 == null) {
			jButton3 = new JButton();
			jButton3.setText("Extract template");
			jButton3.setEnabled(false);
			jButton3.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					util.extract();
				}
			});
		}
		return jButton3;
	}

	/**
	 * This method initializes jButton4
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton4() {
		if (jButton4 == null) {
			jButton4 = new JButton();
			jButton4.setText("Clear database");
			jButton4.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					util.clearDB();
				}
			});
		}
		return jButton4;
	}

	/**
	 * This method initializes jButton5
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton5() {
		if (jButton5 == null) {
			jButton5 = new JButton();
			jButton5.setText("Clear log");
			jButton5.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jTextArea.setText("");
				}
			});
		}
		return jButton5;
	}

	/**
	 * This method initializes jCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBox() {
		if (jCheckBox == null) {
			jCheckBox = new JCheckBox();
			jCheckBox.setText("Auto identify");
			jCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			jCheckBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					util
							.setAutoIdentify((e.getStateChange() == ItemEvent.SELECTED) ? true
									: false);
				}
			});
		}
		return jCheckBox;
	}

	/**
	 * This method initializes jCheckBox1
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBox1() {
		if (jCheckBox1 == null) {
			jCheckBox1 = new JCheckBox();
			jCheckBox1.setText("Auto Extract");
			jCheckBox1
					.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			jCheckBox1.setSelected(true);
			jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					util
							.setAutoExtract((e.getStateChange() == ItemEvent.SELECTED) ? true
									: false);
				}
			});
		}
		return jCheckBox1;
	}

	/**
	 * @param producer
	 */
	public void showImage(Image image) {
		this.image = image;
		jPanel2.repaint();

	}

	public void writeLog(String text) {
		jTextArea.append(text + "\n");

		Runnable autoscroll = new Runnable() {
			public void run() {
				JScrollBar vbar = jScrollPane.getVerticalScrollBar();
				vbar.setValue(vbar.getMaximum());
			}
		};
		SwingUtilities.invokeLater(autoscroll);

	}

	/**
	 * This method initializes jTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setEditable(false);
			jTextArea.setLineWrap(true);
			jTextArea.setAutoscrolls(true);
			jTextArea.setFont(Font.decode("arial-11"));
		}
		return jTextArea;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTextArea());
			jScrollPane
					.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPane.setBorder(new javax.swing.border.SoftBevelBorder(
					javax.swing.border.SoftBevelBorder.LOWERED));
			jScrollPane.setPreferredSize(new java.awt.Dimension(0, 130));
			jScrollPane.setAutoscrolls(true);
		}
		return jScrollPane;
	}

	class ImageFileFilter extends FileFilter {

		private ImageReaderWriterSpi spi;

		private String[] suffixes;

		ImageFileFilter(ImageReaderWriterSpi spi) {
			this.spi = spi;
			suffixes = spi.getFileSuffixes();
		}

		public boolean accept(File f) {
			if (f.isDirectory())
				return true;
			String fileName = f.getName();
			int i = fileName.lastIndexOf('.');
			String suffix = fileName.substring(i + 1);
			for (i = 0; i < suffixes.length; i++)
				if (suffix.equalsIgnoreCase(suffixes[i]))
					return true;
			return false;

		}

		public String getDescription() {
			StringBuffer buffer = new StringBuffer();
			int i;
			buffer.append(spi.getDescription(Locale.getDefault()));
			buffer.append(" (");
			for (i = 0; i < suffixes.length; i++) {
				if (i != 0)
					buffer.append(", ");
				buffer.append("*.");
				buffer.append(suffixes[i]);
			}
			buffer.append(")");
			return buffer.toString();
		}

		/**
		 * @return Returns the spi.
		 */
		public ImageReaderWriterSpi getSpi() {
			return spi;
		}
	}

	/**
	 * 
	 */
	public void enableImage() {
		jButton3.setEnabled(true);
		jMenuItem.setEnabled(true);
		jButton.setEnabled(false);
		jButton1.setEnabled(false);
		jButton2.setEnabled(false);

	}

	/**
	 * 
	 */
	public void enableTemplate() {
		jButton.setEnabled(true);
		jButton1.setEnabled(true);
		jButton2.setEnabled(true);

	}

	public String getAppletInfo() {
		return "GrFinger Java Applet Sample";
	}

} // @jve:decl-index=0:visual-constraint="236,-58"
