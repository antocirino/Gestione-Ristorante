package control;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Color;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

import DTO.DTOOrdine;
import entity.EntityDettaglioOrdinePietanza;
import entity.EntityOrdine;
import entity.EntityPietanza;
import entity.EntityTavolo;
import database.DBOrdine;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe responsabile dell'esportazione di un conto in formato PDF.
 * Implementa la funzionalità di generazione di un documento PDF
 * che riporta i dettagli di un ordine e il conto totale.
 */
public class ContoPdfExporter {

    // Colori e stili
    private static final Color BLU_PRINCIPALE = new Color(41, 128, 185);
    private static final Color BLU_CHIARO = new Color(52, 152, 219);
    private static final Color GRIGIO_SCURO = new Color(44, 62, 80);
    private static final Color GRIGIO_CHIARO = new Color(236, 240, 241);
    private static final Color VERDE = new Color(39, 174, 96);

    /**
     * Genera un file PDF contenente il conto di un ordine.
     * 
     * @param ordine DTO contenente le informazioni dell'ordine
     * @return Path del file PDF generato
     * @throws Exception se si verificano errori durante la generazione del PDF
     */
    public static Path generaPdf(DTOOrdine ordine) throws Exception {
        // Usa la cartella dedicata per i PDF esportati in ambiente Docker
        File downloadDir;

        // Prova prima il percorso dell'app montato in Docker
        File appExportDir = new File("/app/exported-pdf");
        if (appExportDir.exists() && appExportDir.isDirectory() && appExportDir.canWrite()) {
            downloadDir = appExportDir;
            System.out.println("Usando la directory dedicata per i PDF: " + appExportDir.getAbsolutePath());
        } else {
            // Fallback alla cartella Download dell'utente
            String homeDir = System.getProperty("user.home");

            // Verifica se homeDir è valido (può essere "?" in Docker)
            if (homeDir == null || homeDir.equals("?") || homeDir.isEmpty()) {
                // Se siamo in Docker, ma non troviamo /app/exported-pdf
                // usa /tmp come ultima risorsa
                downloadDir = new File("/tmp");
                System.out.println("Fallback a directory temporanea: " + downloadDir.getAbsolutePath());
            } else {
                downloadDir = new File(homeDir, "Download");

                // Crea directory se non esiste
                if (!downloadDir.exists()) {
                    boolean created = downloadDir.mkdirs();
                    if (!created) {
                        System.out.println("Non è stato possibile creare la directory: " + downloadDir);
                        // Fallback a tmp
                        downloadDir = new File("/tmp");
                    }
                }
            }
        }

        // Crea nome file con timestamp per evitare sovrascritture
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());
        String fileName = "conto_tavolo_" + ordine.getIdTavolo() + "_" + timestamp + ".pdf";
        File outputFile = new File(downloadDir, fileName);

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            // Crea documento PDF in formato A5
            Document document = new Document(PageSize.A5);
            PdfWriter.getInstance(document, fos);
            document.open();

            // Aggiungi i contenuti al PDF
            aggiungiIntestazione(document, ordine);
            aggiungiDettagliOrdine(document, ordine);
            aggiungiTotale(document, ordine);
            aggiungiPieDiPagina(document);

            document.close();
            System.out.println("PDF del conto generato con successo in: " + outputFile.getAbsolutePath());

            return outputFile.toPath();
        } catch (Exception e) {
            System.err.println("Errore nella generazione del PDF: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Aggiunge l'intestazione al documento PDF.
     */
    private static void aggiungiIntestazione(Document document, DTOOrdine ordine) throws Exception {
        // Titolo del ristorante
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BLU_PRINCIPALE);
        Paragraph title = new Paragraph("RISTORANTE La Dolce Vita", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(5);
        document.add(title);

        // Sottotitolo con indirizzo
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 10, GRIGIO_SCURO);
        Paragraph subtitle = new Paragraph("Via Claudio, 21 - Napoli", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(5);
        document.add(subtitle);

        // Linea separatrice
        LineSeparator line = new LineSeparator();
        line.setLineColor(BLU_CHIARO);
        document.add(line);

        // Informazioni dell'ordine
        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        Paragraph infoParagraph = new Paragraph();
        infoParagraph.setSpacingBefore(10);
        infoParagraph.setSpacingAfter(10);
        infoParagraph.setAlignment(Element.ALIGN_CENTER);

        // Formatta la data
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dataOrdine = dateFormat.format(ordine.getDataOrdine());

        // Recupera informazioni del tavolo
        EntityTavolo tavolo = new EntityTavolo(ordine.getIdTavolo());

        infoParagraph.add(new Phrase("TAVOLO #" + ordine.getIdTavolo() + " - Coperti: " + ordine.getNumPersone() + "\n",
                infoFont));
        infoParagraph.add(new Phrase("Data: " + dataOrdine + "\n",
                FontFactory.getFont(FontFactory.HELVETICA, 10, GRIGIO_SCURO)));
        infoParagraph.add(new Phrase("Ordine #" + ordine.getIdOrdine(),
                FontFactory.getFont(FontFactory.HELVETICA, 10, GRIGIO_SCURO)));

        document.add(infoParagraph);
    }

    /**
     * Aggiunge i dettagli dell'ordine al documento PDF.
     */
    private static void aggiungiDettagliOrdine(Document document, DTOOrdine ordine) throws Exception {
        // Recupero l'entity ordine per accedere ai dettagli
        EntityOrdine entityOrdine = new EntityOrdine(ordine.getIdOrdine());

        // Crea tabella per i dettagli
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);

        // Larghezze relative delle colonne
        float[] columnWidths = { 10f, 50f, 20f, 20f };
        table.setWidths(columnWidths);

        // Intestazione tabella
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, GRIGIO_SCURO);

        PdfPCell cellQta = new PdfPCell(new Phrase("Qtà", headerFont));
        PdfPCell cellDescrizione = new PdfPCell(new Phrase("Descrizione", headerFont));
        PdfPCell cellPrezzo = new PdfPCell(new Phrase("Prezzo", headerFont));
        PdfPCell cellTotale = new PdfPCell(new Phrase("Totale", headerFont));

        // Stile delle celle intestazione
        for (PdfPCell cell : new PdfPCell[] { cellQta, cellDescrizione, cellPrezzo, cellTotale }) {
            cell.setBackgroundColor(GRIGIO_CHIARO);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        }

        table.addCell(cellQta);
        table.addCell(cellDescrizione);
        table.addCell(cellPrezzo);
        table.addCell(cellTotale);

        // Recupera i dettagli dell'ordine
        java.util.ArrayList<EntityDettaglioOrdinePietanza> dettagli = EntityDettaglioOrdinePietanza
                .getDettagliOrdine(ordine.getIdOrdine());

        // Font per le righe della tabella
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        Font pietanzaFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        // Mappa per raggruppare i dettagli dei menu fissi
        java.util.Map<Integer, java.util.List<EntityDettaglioOrdinePietanza>> menuItemsMap = new java.util.HashMap<>();

        // Raggruppa i dettagli per menu fissi
        for (EntityDettaglioOrdinePietanza dettaglio : dettagli) {
            if (dettaglio.isParteDiMenu()) {
                // Raggruppa gli elementi di menu fissi
                int menuId = dettaglio.getIdMenu();
                if (!menuItemsMap.containsKey(menuId)) {
                    menuItemsMap.put(menuId, new java.util.ArrayList<>());
                }
                menuItemsMap.get(menuId).add(dettaglio);
            }
        }

        // Aggiungi pietanze normali alla tabella
        for (EntityDettaglioOrdinePietanza dettaglio : dettagli) {
            if (!dettaglio.isParteDiMenu()) { // Se non fa parte di un menu fisso
                EntityPietanza pietanza = dettaglio.getPietanza();
                int quantita = dettaglio.getQuantita();
                double prezzoUnitario = pietanza.getPrezzo();
                double subtotale = prezzoUnitario * quantita;

                table.addCell(creaCella(String.valueOf(quantita), cellFont, Element.ALIGN_CENTER));
                table.addCell(creaCella(pietanza.getNome(), pietanzaFont, Element.ALIGN_LEFT));
                table.addCell(creaCella(String.format("€ %.2f", prezzoUnitario), cellFont, Element.ALIGN_RIGHT));
                table.addCell(creaCella(String.format("€ %.2f", subtotale), cellFont, Element.ALIGN_RIGHT));
            }
        }

        // Aggiungi menu fissi alla tabella
        Font menuFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        for (Map.Entry<Integer, List<EntityDettaglioOrdinePietanza>> entry : menuItemsMap
                .entrySet()) {
            int menuId = entry.getKey();
            List<EntityDettaglioOrdinePietanza> menuItems = entry.getValue();

            if (!menuItems.isEmpty()) {
                // Recupera informazioni sul menu dal database
                String nomeMenu = "Menu Fisso #" + menuId;
                double prezzoMenu = 0.0;

                // Recupera informazioni sul menu dal DAO
                DBOrdine dbOrdine = new DBOrdine();
                Map<String, Object> infoMenu = dbOrdine.getInfoMenuFisso(menuId);
                if (infoMenu != null) {
                    nomeMenu = (String) infoMenu.get("nome");
                    prezzoMenu = (Double) infoMenu.get("prezzo");
                }

                // Determina la quantità di menu dal primo elemento
                int quantitaMenu = menuItems.get(0).getQuantita();
                double subtotaleMenu = prezzoMenu * quantitaMenu;

                // Aggiungi il menu come una singola riga
                table.addCell(creaCella(String.valueOf(quantitaMenu), cellFont, Element.ALIGN_CENTER));
                table.addCell(creaCella(nomeMenu, menuFont, Element.ALIGN_LEFT));
                table.addCell(creaCella(String.format("€ %.2f", prezzoMenu), cellFont, Element.ALIGN_RIGHT));
                table.addCell(creaCella(String.format("€ %.2f", subtotaleMenu), cellFont, Element.ALIGN_RIGHT));
            }
        }

        document.add(table);
    }

    /**
     * Crea una cella formattata per la tabella.
     */
    private static PdfPCell creaCella(String testo, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(testo, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setBorderColor(GRIGIO_CHIARO);
        return cell;
    }

    /**
     * Aggiunge il totale del conto al documento PDF.
     */
    private static void aggiungiTotale(Document document, DTOOrdine ordine) throws Exception {
        // Crea tabella per il totale
        PdfPTable tableTotale = new PdfPTable(2);
        tableTotale.setWidthPercentage(80);
        tableTotale.setSpacingBefore(15);
        tableTotale.setSpacingAfter(15);
        tableTotale.setHorizontalAlignment(Element.ALIGN_RIGHT);

        // Larghezze relative delle colonne
        float[] columnWidths = { 60f, 40f };
        tableTotale.setWidths(columnWidths);

        // Font per il totale
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA, 11, GRIGIO_SCURO);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, GRIGIO_SCURO);
        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, VERDE);

        // Recupera costo coperto dal Controller
        double costoCoperto = Controller.getCostoCoperto();
        double totaleCoperto = costoCoperto * ordine.getNumPersone();

        // Il costo totale già include i coperti
        double totale = ordine.getCostoTotale();

        double totaleFinale = totale + totaleCoperto; // Aggiungi il costo del coperto al totale
        double iva = totaleFinale * 0.0909; // IVA al 10% (10/110 = 0.0909)

        // Riga per il netto
        PdfPCell cellNettoLabel = new PdfPCell(new Phrase("Subtotale:", labelFont));
        PdfPCell cellNettoValue = new PdfPCell(new Phrase(String.format("€ %.2f", totale), valueFont));
        styleRigaTotali(cellNettoLabel, cellNettoValue);
        tableTotale.addCell(cellNettoLabel);
        tableTotale.addCell(cellNettoValue);

        // Riga per l'IVA
        PdfPCell cellIvaLabel = new PdfPCell(new Phrase("Di cui IVA (10%):", labelFont));
        PdfPCell cellIvaValue = new PdfPCell(new Phrase(String.format("€ %.2f", iva), valueFont));
        styleRigaTotali(cellIvaLabel, cellIvaValue);
        tableTotale.addCell(cellIvaLabel);
        tableTotale.addCell(cellIvaValue);

        // Riga per il coperto
        PdfPCell cellCopertoLabel = new PdfPCell(new Phrase(
                String.format("Coperti (€ %.2f x %d):", costoCoperto, ordine.getNumPersone()), labelFont));
        PdfPCell cellCopertoValue = new PdfPCell(new Phrase(String.format("€ %.2f", totaleCoperto), valueFont));
        styleRigaTotali(cellCopertoLabel, cellCopertoValue);
        tableTotale.addCell(cellCopertoLabel);
        tableTotale.addCell(cellCopertoValue);

        // Riga per il totale
        PdfPCell cellTotaleLabel = new PdfPCell(
                new Phrase("TOTALE:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, GRIGIO_SCURO)));
        PdfPCell cellTotaleValue = new PdfPCell(new Phrase(String.format("€ %.2f", totaleFinale), totalFont));
        styleRigaTotali(cellTotaleLabel, cellTotaleValue);
        cellTotaleLabel.setBackgroundColor(GRIGIO_CHIARO);
        cellTotaleValue.setBackgroundColor(GRIGIO_CHIARO);
        tableTotale.addCell(cellTotaleLabel);
        tableTotale.addCell(cellTotaleValue);

        document.add(tableTotale);
    }

    /**
     * Stile comune per le righe della tabella dei totali.
     */
    private static void styleRigaTotali(PdfPCell labelCell, PdfPCell valueCell) {
        for (PdfPCell cell : new PdfPCell[] { labelCell, valueCell }) {
            cell.setPadding(6);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBorderColor(GRIGIO_CHIARO);
        }
        labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    }

    /**
     * Aggiunge il piè di pagina al documento PDF.
     */
    private static void aggiungiPieDiPagina(Document document) throws Exception {
        // Linea separatrice
        LineSeparator line = new LineSeparator();
        line.setLineColor(BLU_CHIARO);
        document.add(line);

        // Ringraziamenti
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 9, GRIGIO_SCURO);
        Paragraph thanks = new Paragraph("Grazie per averci scelto!", footerFont);
        thanks.setAlignment(Element.ALIGN_CENTER);
        thanks.setSpacingBefore(10);
        document.add(thanks);

        // Info fiscali
        Paragraph fiscal = new Paragraph("P.IVA: 12345678901 - C.F.: RSSMRA80A01F205X",
                FontFactory.getFont(FontFactory.HELVETICA, 8, GRIGIO_SCURO));
        fiscal.setAlignment(Element.ALIGN_CENTER);
        fiscal.setSpacingBefore(5);
        document.add(fiscal);

        // Data di stampa
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String now = dateFormat.format(new Date());
        Paragraph printDate = new Paragraph("Stampato il: " + now,
                FontFactory.getFont(FontFactory.HELVETICA, 8, GRIGIO_SCURO));
        printDate.setAlignment(Element.ALIGN_CENTER);
        printDate.setSpacingBefore(5);
        document.add(printDate);
    }
}
