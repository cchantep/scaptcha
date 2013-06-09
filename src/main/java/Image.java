package scaptcha;

import java.io.PipedOutputStream;
import java.io.PipedInputStream;
import java.io.OutputStream;
import java.io.InputStream;

import java.util.Iterator;

import javax.imageio.ImageWriter;
import javax.imageio.ImageIO;

import javax.imageio.stream.ImageOutputStream;

import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

import java.awt.image.BufferedImage;

/**
 * Image utility.
 *
 * @author Cedric Chantepie
 */
public final class Image {

    /**
     * Returns default image writer.
     *
     * @param format Image format, or null if none
     */
    public static ImageWriter writer(final String format) {
	final Iterator<ImageWriter> writers = ImageIO.
	    getImageWritersByFormatName("png");
	
	if (!writers.hasNext()) {
            return null;
	} // end of if

        return writers.next();
    } // end of writer

    /**
     * Returns stream (from buffer).
     *
     * @param width Image width
     * @param height Image height
     * @param text Text to be drawn
     * @param foreground Foreground color
     * @param background Background color
     * @see #bufferedImage
     */
    public static InputStream stream(final int width,
                                     final int height,
                                     final String text,
                                     final Color foreground,
                                     final Color background) {

        return stream(width, height, -1, -1, 
                      text, foreground, background, null);

    } // end of stream

    /**
     * Returns stream (from buffer).
     *
     * @param width Image width
     * @param height Image height
     * @param x Text X coordinate (or -1)
     * @param y Text Y coordinate (or -1)
     * @param text Text to be drawn
     * @param foreground Foreground color
     * @param background Background color
     * @param font Text font (or null for system font)
     * @see #bufferedImage
     */
    public static InputStream stream(final int width,
                                     final int height,
                                     final int x,
                                     final int y,
                                     final String text,
                                     final Color foreground,
                                     final Color background,
                                     final Font font) {
        
        final ImageWriter writer = writer("png");

        if (writer == null) {
            throw new UnsupportedOperationException("PNG format unsupported");
        } // end of if

        // ---

        final BufferedImage image = 
            bufferedImage(width, height, x, y, 
                          text, foreground, background, font);

        PipedInputStream in = null;
        PipedOutputStream out = null;

        try {
            in = new PipedInputStream();
            out = new PipedOutputStream(in);

            final ImageRenderer renderer = 
                new ImageRenderer(image, writer, out);

            (new Thread(renderer)).start();

            return in;
        } catch (Exception e) {
            e.printStackTrace();

            if (out != null) {
                try {
                    out.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } // end of if
            } // end of if

            if (in != null) {
                try {
                    in.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } // end of if
            } // end of if

            throw new RuntimeException(e.getMessage());
        } // end of finally        
    } // end of stream

    /**
     * Generate image with random ascii |text|.
     *
     * @param width Image width (px)
     * @param height Image height (px)
     * @param x Text X coordinate (or -1)
     * @param y Text Y coordinate (or -1)
     * @param text ASCII text
     * @param foreground Foreground color
     * @param background Background color
     * @param font Text font (or null for system one)
     * @return Image rendering |text| with some transformations.
     */
    public static BufferedImage bufferedImage(final int width,
                                              final int height,
                                              final int x,
                                              final int y,
                                              final String text,
                                              final Color foreground,
                                              final Color background,
                                              final Font font) {

	final BufferedImage img = 
	    new BufferedImage(width, height, 
			      BufferedImage.TYPE_BYTE_INDEXED);
	final Graphics2D g2d = (Graphics2D) img.getGraphics();

	g2d.setColor(background);

        if (font != null) {
            g2d.setFont(font);
        } // end of if

	g2d.fillRect(0, 0, width, height);

	g2d.setColor(background.darker());

	for (int i = 0; i < width; i += 15) {
	    g2d.drawLine(i, 0, i, height);
	} // end of for

	g2d.drawLine(width-1, 0, width-1, height);
	
	for (int i = 0; i < height; i += 10) {
	    g2d.drawLine(0, i, width, i);
	} // end of for

	g2d.drawLine(0, height-1, width, height-1);

	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
			     RenderingHints.VALUE_ANTIALIAS_ON);

	g2d.setColor(foreground);

        final int textx = (x != -1) ? x : 5;
        final int texty = (y != -1) ? y
            : (height/2) + (g2d.getFontMetrics(g2d.getFont()).getHeight() / 2);

        g2d.drawString(text, textx, texty);

	return img;
    } // end of bufferedImage

    // --- Inner classes ---

    /**
     * Render image to output stream.
     */
    private final static class ImageRenderer implements Runnable {
        final BufferedImage image;
        final ImageWriter writer;
        final OutputStream output;

        // --- Constructors ---

        /**
         * Bulk constructor.
         */
        ImageRenderer(final BufferedImage image, 
                      final ImageWriter writer,
                      final OutputStream output) {

            this.image = image;
            this.writer = writer;
            this.output = output;
        } // end of <init>

        // --- 

        /**
         * Renderer process.
         */
        public void run() {
            ImageOutputStream out = null;

            try {
                out = ImageIO.createImageOutputStream(output);

                writer.setOutput(out);
                writer.write(image);

                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } // end of catch
                } // end of if

                close();
            } // end of finally
        } // end of run

        /**
         * {@inheritDoc}
         */
        protected void finalize() throws Throwable {
            close();
        } // end of finalize

        /**
         * Close underlying resources.
         */
        public void close() {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            } // end of catch
        } // end of close
    } // end of class ImageRenderer
} // end of class Image
