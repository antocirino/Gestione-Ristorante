package utility;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.swing.ImageIcon;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;

/**
 * Classe di utilità per la gestione delle icone SVG.
 * Fornisce metodi per caricare e gestire le icone SVG in modo uniforme
 * in tutta l'applicazione.
 */
public class SvgIconManager {

    // Colori predefiniti
    private static final Color DEFAULT_COLOR = Color.WHITE;

    /**
     * Carica un'icona SVG con dimensioni specifiche e colore di sfondo predefinito
     * (bianco)
     * 
     * @param filename Il nome del file SVG
     * @param width    La larghezza dell'icona
     * @param height   L'altezza dell'icona
     * @return Un ImageIcon con l'icona SVG caricata, o un'icona di fallback se non
     *         trovata
     */
    public static ImageIcon loadSVGIcon(String filename, int width, int height) {
        return loadSVGIcon(filename, width, height, DEFAULT_COLOR);
    }

    /**
     * Carica un'icona SVG con dimensioni specifiche e colore di sfondo
     * personalizzato
     * 
     * @param filename Il nome del file SVG
     * @param width    La larghezza dell'icona
     * @param height   L'altezza dell'icona
     * @param color    Il colore di sfondo dell'icona
     * @return Un ImageIcon con l'icona SVG caricata, o un'icona di fallback se non
     *         trovata
     */
    public static ImageIcon loadSVGIcon(String filename, int width, int height, Color color) {
        try {
            // Percorsi possibili per le icone SVG (nell'ordine di priorità)
            String[] possiblePaths = {
                    "bin/resources/icons/" + filename, // Nel container/dopo compilazione
                    "resources/icons/" + filename, // Percorso relativo nel container
                    "GestioneRistorante/bin/resources/icons/" + filename, // Dalla root progetto
                    "GestioneRistorante/src/resources/icons/" + filename, // Sorgente originale
                    "src/resources/icons/" + filename // Durante sviluppo
            };

            File svgFile = null;

            for (String path : possiblePaths) {
                File testFile = new File(path);
                if (testFile.exists()) {
                    svgFile = testFile;
                    break;
                }
            }

            // Se non trovato con percorsi diretti, prova con il class loader
            if (svgFile == null || !svgFile.exists()) {
                try {
                    URL resourceUrl = SvgIconManager.class.getClassLoader().getResource("icons/" + filename);
                    if (resourceUrl == null) {
                        resourceUrl = SvgIconManager.class.getClassLoader().getResource("resources/icons/" + filename);
                    }
                    if (resourceUrl != null) {
                        svgFile = new File(resourceUrl.toURI());
                    }
                } catch (Exception e) {
                    // Ignora e usa fallback
                }
            }

            if (svgFile == null || !svgFile.exists()) {
                return createFallbackIcon(filename, width, height, color);
            }

            SVGUniverse svgUniverse = new SVGUniverse();
            java.net.URI svgUri = svgFile.toURI();
            SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(svgUri.toURL()));

            if (diagram == null) {
                return createFallbackIcon(filename, width, height, color);
            }

            // Imposta dimensioni
            diagram.setIgnoringClipHeuristic(true);

            // Renderizza SVG come BufferedImage con sfondo trasparente
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // Pulisci lo sfondo
            g2.setComposite(AlphaComposite.Clear);
            g2.fillRect(0, 0, width, height);
            g2.setComposite(AlphaComposite.SrcOver);

            // Imposta il colore dell'SVG
            g2.setColor(color);

            // Scala e centra l'SVG
            java.awt.geom.Rectangle2D bounds = diagram.getViewRect();
            double scaleX = (double) width / bounds.getWidth();
            double scaleY = (double) height / bounds.getHeight();
            double scale = Math.min(scaleX, scaleY);

            int scaledWidth = (int) (bounds.getWidth() * scale);
            int scaledHeight = (int) (bounds.getHeight() * scale);
            int x = (width - scaledWidth) / 2;
            int y = (height - scaledHeight) / 2;

            g2.translate(x, y);
            g2.scale(scale, scale);

            diagram.render(g2);
            g2.dispose();

            return new ImageIcon(image);

        } catch (Exception e) {
            e.printStackTrace();
            return createFallbackIcon(filename, width, height, color);
        }
    }

    /**
     * Crea un'icona di fallback con un testo Unicode
     * 
     * @param filename Il nome del file (usato per determinare l'icona Unicode)
     * @param width    La larghezza dell'icona
     * @param height   L'altezza dell'icona
     * @param color    Il colore del testo dell'icona
     * @return Un ImageIcon con l'icona di fallback
     */
    private static ImageIcon createFallbackIcon(String filename, int width, int height, Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Imposta colore e font
        g2.setColor(color);
        g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, Math.min(width, height) - 4));

        String icon = getUnicodeIcon(filename);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(icon);
        int textHeight = fm.getHeight();
        int x = (width - textWidth) / 2;
        int y = (height - textHeight) / 2 + fm.getAscent();

        g2.drawString(icon, x, y);
        g2.dispose();

        return new ImageIcon(image);
    }

    /**
     * Restituisce un'icona Unicode basata sul nome del file
     * 
     * @param filename Il nome del file dell'icona
     * @return Una stringa con l'icona Unicode corrispondente
     */
    private static String getUnicodeIcon(String filename) {
        if (filename.contains("pietanza")) {
            return "🍝";
        } else if (filename.contains("menu")) {
            return "📋";
        } else if (filename.contains("order")) {
            return "🛒";
        } else if (filename.contains("person")) {
            return "👤";
        } else if (filename.contains("restaurant_menu")) {
            return "🍽️";
        } else if (filename.contains("payment")) {
            return "💳";
        } else if (filename.contains("admin_panel_settings")) {
            return "⚙️";
        } else {
            return "📄";
        }
    }
}