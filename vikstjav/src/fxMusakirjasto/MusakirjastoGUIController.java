package fxMusakirjasto;

import javafx.application.Platform;
import static fxMusakirjasto.ArtistiDialogController.getFieldId;

import java.awt.Desktop;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.StringGrid;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import kirjasto.Artisti;
import kirjasto.Kirjasto;
import kirjasto.SailoException;
import kirjasto.Biisi;

/**
 * Luokka kirjaston käyttöliittymän tapahtumien hoitamiseksi.
 * @author vikstjav
 * @version 15.2.2019
 */
public class MusakirjastoGUIController implements Initializable {

    @FXML private ScrollPane panelArtisti;
    @FXML private ListChooser<Artisti> chooserArtistit;
    @FXML private TextField hakuehto;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private Label labelVirhe;
    @FXML private Label aTitle;
    @FXML private ListChooser<Biisi> ChooserBiisit;
    @FXML private ScrollPane panelBiisit;
    @FXML private GridPane gridArtisti;
    @FXML private StringGrid<Biisi> tableBiisit;  

    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }

    
    /**
     * Käsitellään uuden artistin lisääminen
     */
    @FXML private void handleUusiArtisti() {
         uusiArtisti();
    }
    
    
    @FXML private void handleUusiBiisi() {
        uusiBiisi(); 
    }

    
    @FXML private void handleHakuehto() {
        if ( artistiKohdalla != null )
            hae(artistiKohdalla.getTunnusNro());
    }
   
    
    /**
     * Käsitellään tallennuskäsky
     */
    @FXML private void handleTallenna() {
        tallenna();
    }
    
    
    /**
     * Käsitellään lopetuskäsky
     */
    @FXML private void handleLopeta() {
        Platform.exit();
    }

    /**
     * artistin muokkaus
     */
    @FXML private void handleMuokkaaArtisti() {
        muokkaa(1);
    }
    
    /**
     * Biisin muokkaus
     */
    @FXML private void handleMuokkaaBiisia() {
    muokkaaBiisia();
    }
    
    
    /**
     * artistin poisto
     */
    @FXML private void handlePoistaArtisti() {
        poistaArtisti();
    }
    
    
    /**
     * biisin poisto
     */
    @FXML private void handlePoistaBiisi() {
        poistaBiisi();
    }
    
    
    /**
     * Laittaa biisin soimaan
     */
    @FXML private void handleMusiikkia() {
        URL resource = getClass().getResource("/biisi/GoT.mp3");
        Media audio = new Media(resource.toString());
        MediaPlayer audioPlayer = new MediaPlayer(audio);
        audioPlayer.setAutoPlay(true);
    }
    
    /**
     * linkki suunnitelmaan
     */
    @FXML private void handleVersio() {
        versio();
    }


    private String kirjastonnimi = "hardstyle";
    private Kirjasto kirjasto;
    private Artisti artistiKohdalla;
    private TextField edits[];
    private int kentta = 0;
    private static Artisti apuartisti = new Artisti(); 
    private static Biisi apukappale = new Biisi(); 
    
    
    /**
     * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle
     * yksi iso tekstikenttä, johon voidaan tulostaa artistien tiedot.
     * Alustetaan myös artistilistan kuuntelija 
     */
    protected void alusta() {
        chooserArtistit.clear();
        chooserArtistit.addSelectionListener(e -> naytaArtisti());
        
        cbKentat.clear(); 
        for (int k = apuartisti.ekaKentta(); k < apuartisti.getKenttia(); k++) 
            cbKentat.add(apuartisti.getKysymys(k), null); 
        cbKentat.getSelectionModel().select(0); 
        
        edits = ArtistiDialogController.luoKentat(gridArtisti); 
        for (TextField edit: edits)
            if ( edit != null ) {   
                edit.setEditable(false);
                edit.setOnMouseClicked(e -> { if ( e.getClickCount() > 1 ) muokkaa(getFieldId(e.getSource(),0)); });
                edit.focusedProperty().addListener((a,o,n) -> kentta = getFieldId(edit,kentta));   
            }
     // alustetaan biisitaulukon otsikot 
        int eka = apukappale.ekaKentta(); 
        int lkm = apukappale.getKenttia(); 
        String[] headings = new String[lkm-eka]; 
        for (int i=0, k=eka; k<lkm; i++, k++) headings[i] = apukappale.getKysymys(k); 
        tableBiisit.initTable(headings); 
        tableBiisit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
        tableBiisit.setEditable(false); 
        tableBiisit.setPlaceholder(new Label("Ei vielä biisejä")); 
         
        tableBiisit.setColumnSortOrderNumber(1); 
        tableBiisit.setColumnSortOrderNumber(2); 
        tableBiisit.setColumnWidth(1, 60); 
    }
    
    /**
     * virheteksti
     * @param virhe virhe
     */
    @SuppressWarnings("unused")
    private void naytaVirhe(String virhe) {
                if ( virhe == null || virhe.isEmpty() ) {
                    labelVirhe.setText("");
                   labelVirhe.getStyleClass().removeAll("virhe");
                    return;
                }
                labelVirhe.setText(virhe);
                labelVirhe.getStyleClass().add("virhe");
            }
 
    
    /**
     * Näyttää listasta valitun artistin tiedot tekstikenttiin.
     */
    protected void naytaArtisti() {
        artistiKohdalla = chooserArtistit.getSelectedObject();
        
        if (artistiKohdalla == null) return;
        
        TietueDialogController.naytaTietue(edits, artistiKohdalla); 
        naytaBiisit(artistiKohdalla);
        }   
    

    /**
     * alustaa kirjaston lukemalla sen valitun nimisestä tiedostosta
     * @param nimi tiedoston nimi
     * @return null jos onnistuu, muuten virhe tekstinä
     * @throws FileNotFoundException asd
     * 
     */
    protected String lueTiedosto(String nimi) throws FileNotFoundException {
        kirjastonnimi = nimi;
        try {
            kirjasto.lueTiedostosta(nimi);
            hae(0);
            return null;
        } catch (SailoException e) {
            hae(0);
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
    }
    
    
    /**
     * Poistetaan biisitaulukosta valitulla kohdalla oleva biisi. 
     */
    private void poistaBiisi() {
        int rivi = tableBiisit.getRowNr();
        if ( rivi < 0 ) return;
        Biisi song = tableBiisit.getObject();
        if ( song == null ) return;
        kirjasto.poistaBiisi(song);
        naytaBiisit(artistiKohdalla);
        int biisejä = tableBiisit.getItems().size(); 
        if ( rivi >= biisejä ) rivi = biisejä -1;
        tableBiisit.getFocusModel().focus(rivi);
        tableBiisit.getSelectionModel().select(rivi);
    }


    /**
     * Poistetaan listalta valittu artisti
     */
    private void poistaArtisti() {
        Artisti artisti = artistiKohdalla;
        if ( artisti == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko jäsen: " + artisti.getNimi(), "Kyllä", "Ei") )
            return;
        kirjasto.poista(artisti);
        int index = chooserArtistit.getSelectedIndex();
        hae(0);
        chooserArtistit.setSelectedIndex(index);
    }
    
    
     /** 
      * Kysytään tiedoston nimi ja luetaan se 
      * @return true jos onnistui, false jos ei 
     * @throws FileNotFoundException  jos ei löydy
      */ 
     public boolean avaa() throws FileNotFoundException { 
         String uusinimi = kirjastonnimi; 
         if (uusinimi == null) return false; 
         lueTiedosto(uusinimi); 
         return true; 
     } 
    


    /**
     * Tietojen tallennus
     */
    private String tallenna() {
        try {
            kirjasto.tallenna();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }
    }
    
    
    /**
     * Hakee artistien tiedot listaan
     * @param jnr jäsennumero
     */
    protected void hae(int jnr) {
        int jnro = jnr; // jnro jäsenen numero, joka aktivoidaan haun jälkeen 
        if ( jnro <= 0 ) { 
            Artisti kohdalla = artistiKohdalla; 
            if ( kohdalla != null ) jnro = kohdalla.getTunnusNro(); 
        }
        
        int k = cbKentat.getSelectionModel().getSelectedIndex() + apuartisti.ekaKentta(); 
        String ehto = hakuehto.getText(); 
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*"; 
        
        chooserArtistit.clear();

        int index = 0;
        Collection<Artisti> artistit;
        try {
            artistit = kirjasto.etsi(ehto, k);
            int i = 0;
            for (Artisti artisti:artistit) {
                if (artisti.getTunnusNro() == jnro) index = i;
                chooserArtistit.add(artisti.getNimi(), artisti);
                i++;
            }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Jäsenen hakemisessa ongelmia! " + ex.getMessage());
        }
        chooserArtistit.setSelectedIndex(index); // tästä tulee muutosviesti joka näyttää jäsenen
    }
    
    
    /**
     * Luo uuden artistin
     */
    private void uusiArtisti() {
        try {
            Artisti uusi = new Artisti();
            uusi = ArtistiDialogController.kysyArtisti(null, uusi, 1); 
            if ( uusi == null ) return; 
            uusi.rekisteroi(); 
            kirjasto.lisaa(uusi);
            hae(uusi.getTunnusNro()); 
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
            return;
        }
    }
    
    /**
     * Näyttää valitun artistin biisit
     * @param artisti artisti kohdalla
     */
    private void naytaBiisit(Artisti artisti) {
        tableBiisit.clear();
        if ( artisti == null ) return;
        
        try {
            List<Biisi> kappaleet = kirjasto.annaBiisit(artisti);
            if ( kappaleet.size() == 0 ) return;
            for (Biisi song: kappaleet)
                naytaBiisi(song);
        } catch (SailoException e) {
            // naytaVirhe(e.getMessage());
        } 
    }
    
    /**
     * näyttää biisin kentän kohdalla
     * @param song biisi
     */
    private void naytaBiisi(Biisi song) {
        int kenttia = song.getKenttia(); 
        String[] rivi = new String[kenttia-song.ekaKentta()]; 
        for (int i=0, k=song.ekaKentta(); k < kenttia; i++, k++) 
            rivi[i] = song.anna(k); 
        tableBiisit.add(song,rivi);
    }
    
    
     /**
      * Tekee uuden tyhjän biisin editointia varten
      */
private void uusiBiisi() {
    if ( artistiKohdalla == null ) return; 
    try {
        Biisi uusi = new Biisi(artistiKohdalla.getTunnusNro());
        uusi = TietueDialogController.kysyTietue(null, uusi, 0);
        if ( uusi == null ) return;
        uusi.rekisteroi();
        kirjasto.lisaa(uusi);
        naytaBiisit(artistiKohdalla); 
        tableBiisit.selectRow(1000);  // järjestetään viimeinen rivi valituksi
    } catch (SailoException e) {
        Dialogs.showMessageDialog("Lisääminen epäonnistui: " + e.getMessage());
    }
}

    /**
     * Artistin muokkaus
     * @param k kenttä
     */
    private void muokkaa(int k) {
        if ( artistiKohdalla == null ) return; 
        try { 
            Artisti artisti; 
            artisti = ArtistiDialogController.kysyArtisti(null, artistiKohdalla.clone(), k); 
            if ( artisti == null ) return; 
            kirjasto.korvaaTaiLisaa(artisti); 
            hae(artisti.getTunnusNro()); 
        } catch (CloneNotSupportedException e) { 
            // 
        } catch (SailoException e) { 
            Dialogs.showMessageDialog(e.getMessage()); 
        } 
    } 
    
    /**
     * biisin muokkaus
     */
    private void muokkaaBiisia() {
        int r = tableBiisit.getRowNr();
        if ( r < 0 ) return; // klikattu ehkä otsikkoriviä
        Biisi song = tableBiisit.getObject();
        if ( song == null ) return;
        int k = tableBiisit.getColumnNr()+song.ekaKentta();
        try {
            song = TietueDialogController.kysyTietue(null, song.clone(), k);
            if ( song == null ) return;
            kirjasto.korvaaTaiLisaa(song); 
            naytaBiisit(artistiKohdalla); 
            tableBiisit.selectRow(r);  // järjestetään sama rivi takaisin valituksi
        } catch (CloneNotSupportedException  e) { /* clone on tehty */  
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia lisäämisessä: " + e.getMessage());
        }
    }
    
    
    /**
     * Näytetään ohjelman suunnitelma erillisessä selaimessa.
     */
    private void versio() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2019k/ht/vikstjav");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }
     
    
    /**
     * @param kirjasto kirjasto mitä käytetään
     * 
     */
    public void setKirjasto(Kirjasto kirjasto) {
        this.kirjasto = kirjasto;
        naytaArtisti();
    }


    /**
     * Tarkistetaan onko tallennus tehty
     * @return true jos saa sulkea sovelluksen, false jos ei
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }
}