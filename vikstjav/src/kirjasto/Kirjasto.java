package kirjasto;
import java.io.File;
import java.util.Collection;
import java.util.List;


/**
 * kirjasto-luokka, joka huolehtii artisteista.  Pääosin kaikki metodit
 * ovat vain "välittäjämetodeja" artisteihin.
 *
 * @author Jaakko Vikström
 * @version 1.0, 20.04.2019
 * 
 * Testien alustus
 * @example
 * <pre name="testJAVA">
 * #import kirjasto.SailoException;
 *  private Kirjasto kirjasto;
 *  private Artisti sefa1;
 *  private Artisti sefa2;
 *  private int jid1;
 *  private int jid2;
 *  private Biisi calling21;
 *  private Biisi calling11;
 *  private Biisi calling22; 
 *  private Biisi calling12; 
 *  private Biisi calling23;
 *  
 *  @SuppressWarnings("javadoc")
 *  public void alustaKirjasto() {
 *    kirjasto = new Kirjasto();
 *    sefa1 = new Artisti(); sefa1.vastaaAkuAnkka(); sefa1.rekisteroi();
 *    sefa2 = new Artisti(); sefa2.vastaaAkuAnkka(); sefa2.rekisteroi();
 *    jid1 = sefa1.getTunnusNro();
 *    jid2 = sefa2.getTunnusNro();
 *    calling21 = new Biisi(jid2); calling21.vastaaCalling(jid2);
 *    calling11 = new Biisi(jid1); calling11.vastaaCalling(jid1);
 *    calling22 = new Biisi(jid2); calling22.vastaaCalling(jid2); 
 *    calling12 = new Biisi(jid1); calling12.vastaaCalling(jid1); 
 *    calling23 = new Biisi(jid2); calling23.vastaaCalling(jid2);
 *    try {
 *    kirjasto.lisaa(sefa1);
 *    kirjasto.lisaa(sefa2);
 *    kirjasto.lisaa(calling21);
 *    kirjasto.lisaa(calling11);
 *    kirjasto.lisaa(calling22);
 *    kirjasto.lisaa(calling12);
 *    kirjasto.lisaa(calling23);
 *    } catch ( Exception e) {
 *       System.err.println(e.getMessage());
 *    }
 *  }
 * </pre>
*/
public class Kirjasto {
    private Artistit artistit = new Artistit();
    private Biisit biisit = new Biisit(); 

    
    
    /**
     * @return biisien määrä
     */
    public int getBiiseja() {
        return biisit.getLkm();
    }

 
    /**
     * Poistaa artisteista ja biiseistä artistin tiedot 
     * @param artisti artisti jokapoistetaan
     * @return montako artistia poistettiin
     * @example
     * <pre name="test">
     * #THROWS Exception
     *   alustaKirjasto();
     *   kirjasto.etsi("*",0).size() === 2;
     *   kirjasto.annaBiisit(sefa1).size() === 2;
     *   kirjasto.poista(sefa1) === 1;
     *   kirjasto.etsi("*",0).size() === 1;
     *   kirjasto.annaBiisit(sefa1).size() === 0;
     *   kirjasto.annaBiisit(sefa2).size() === 3;
     * </pre>
     */
    public int poista(Artisti artisti) {
        if ( artisti == null ) return 0;
        int ret = artistit.poista(artisti.getTunnusNro()); 
        biisit.poistaArtistinBiisit(artisti.getTunnusNro()); 
        return ret; 
    }


    /** 
     * Poistaa tämän biisin 
     * @param song poistettava biisi 
     * @example
     * <pre name="test">
     * #THROWS Exception
     *   alustaKirjasto();
     *   kirjasto.annaBiisit(sefa1).size() === 2;
     *   kirjasto.poistaBiisi(calling11);
     *   kirjasto.annaBiisit(sefa1).size() === 1;
     */ 
    public void poistaBiisi(Biisi song) { 
        biisit.poista(song); 
    } 

    /**
     * Lisää kirjastoon uuden artistin
     * @param artisti lisättävä artisti
     * @throws SailoException jos lisäystä ei voida tehdä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Kirjasto kirjasto = new Kirjasto();
     * Artisti angerfist1 = new Artisti(), angerfist2 = new Artisti();
     * kirjasto.lisaa(angerfist1);
     * kirjasto.lisaa(angerfist2);
     * kirjasto.lisaa(angerfist1);
     * Collection<Artisti> loytyneet = kirjasto.etsi("",-1);
     * Iterator<Artisti> it = loytyneet.iterator();   
     * it.next() === angerfist1;
     * it.next() === angerfist2; 
     * it.next() === angerfist1; 
     * </pre>
     */
    public void lisaa(Artisti artisti) throws SailoException {
        artistit.lisaa(artisti);
    }
    
    
    /**
     * 
     * @param song biisi
     * @throws SailoException jos tulee ongelmii
     */
    public void lisaa(Biisi song) throws SailoException {
        biisit.lisaa(song);
        
    }
    
    
    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien artistien viitteet 
     * @param hakuehto hakuehto  
     * @param k etsittävän kentän indeksi  
     * @return tietorakenteen löytyneistä artisteista 
     * @throws SailoException Jos jotakin menee väärin
     */ 
    public Collection<Artisti> etsi(String hakuehto, int k) throws SailoException { 
        return artistit.etsi(hakuehto, k); 
    } 
    
    
    /** 
     * Korvaa artistin tietorakenteessa.  Ottaa artistin omistukseensa. 
     * Etsitään samalla tunnusnumerolla oleva artisti.  Jos ei löydy, 
     * niin lisätään uutena artistina. 
     * @param artisti lisätäävän artistin viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne on jo täynnä 
     */ 
    public void korvaaTaiLisaa(Artisti artisti) throws SailoException {
        artistit.korvaaTaiLisaa(artisti); 
        
    } 
    
    
    /** 
     * Korvaa biisin tietorakenteessa.  Ottaa biisin omistukseensa. 
     * Etsitään samalla tunnusnumerolla oleva Biisi.  Jos ei löydy, 
     * niin lisätään uutena biisinä. 
     * @param biisi lisärtävän kappaleen viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne on jo täynnä 
     */ 
    public void korvaaTaiLisaa(Biisi biisi) throws SailoException { 
        biisit.korvaaTaiLisaa(biisi); 
    } 
    
    
    /**
     * Haetaan kaikki artistin 
     * @param artisti artisti jolle biisejä haetaan
     * @return tietorakenne jossa viiteet löydetteyihin biiseihin
     * @throws SailoException jos tulee ongelmia
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.*;
     * 
     *  Kirjasto kirjasto = new Kirjasto();
     *  Artisti sefa1 = new Artisti(), sefa2 = new Artisti(), sefa3 = new Artisti();
     *  sefa1.rekisteroi(); sefa2.rekisteroi(); sefa3.rekisteroi();
     *  int id1 = sefa1.getTunnusNro();
     *  int id2 = sefa2.getTunnusNro();
     *  Biisi calling11 = new Biisi(id1); kirjasto.lisaa(calling11);
     *  Biisi calling12 = new Biisi(id1); kirjasto.lisaa(calling12);
     *  Biisi calling21 = new Biisi(id2); kirjasto.lisaa(calling21);
     *  Biisi calling22 = new Biisi(id2); kirjasto.lisaa(calling22);
     *  Biisi calling23 = new Biisi(id2); kirjasto.lisaa(calling23);
     *  
     *  List<Biisi> loytyneet;
     *  loytyneet = kirjasto.annaBiisit(sefa3);
     *  loytyneet.size() === 0; 
     *  loytyneet = kirjasto.annaBiisit(sefa1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == calling11 === true;
     *  loytyneet.get(1) == calling12 === true;
     *  loytyneet = kirjasto.annaBiisit(sefa2);
     *  loytyneet.size() === 3; 
     *  loytyneet.get(0) == calling21 === true;
     * </pre> 
     */
    public List<Biisi> annaBiisit(Artisti artisti) throws SailoException {
        return biisit.annaBiisit(artisti.getTunnusNro());
    }
    
    
    /**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
        artistit.setTiedostonPerusNimi(hakemistonNimi + "artistiid");
        biisit.setTiedostonPerusNimi(hakemistonNimi + "bid");
    }


    /**
     * Lukee kirjaston tiedot tiedostosta
     * @param nimi jota käyteään lukemisessa
     * @throws SailoException jos lukeminen epäonnistuu
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.*;
     * #import java.util.*;
     * 
     *  Kirjasto kirjasto = new Kirjasto();
     *  
     *  Artisti sefa1 = new Artisti(); sefa1.vastaaAkuAnkka(); sefa1.rekisteroi();
     *  Artisti sefa2 = new Artisti(); sefa2.vastaaAkuAnkka(); sefa2.rekisteroi();
     *  Biisi calling21 = new Biisi(); calling21.vastaaCalling(sefa2.getTunnusNro());
     *  Biisi calling11 = new Biisi(); calling11.vastaaCalling(sefa1.getTunnusNro());
     *  Biisi calling22 = new Biisi(); calling22.vastaaCalling(sefa2.getTunnusNro()); 
     *  Biisi calling12 = new Biisi(); calling12.vastaaCalling(sefa1.getTunnusNro()); 
     *  Biisi calling23 = new Biisi(); calling23.vastaaCalling(sefa2.getTunnusNro());
     *   
     *  String hakemisto = "testihardstyle";
     *  File dir = new File(hakemisto);
     *  File ftied  = new File(hakemisto+"/artistiid.dat");
     *  File fhtied = new File(hakemisto+"/bid.dat");
     *  dir.mkdir();  
     *  ftied.delete();
     *  fhtied.delete();
     *  kirjasto.lueTiedostosta(hakemisto); #THROWS SailoException
     *  kirjasto.lisaa(sefa1);
     *  kirjasto.lisaa(sefa2);
     *  kirjasto.lisaa(calling21);
     *  kirjasto.lisaa(calling11);
     *  kirjasto.lisaa(calling22);
     *  kirjasto.lisaa(calling12);
     *  kirjasto.lisaa(calling23);
     *  kirjasto.tallenna();
     *  kirjasto = new Kirjasto();
     *  kirjasto.lueTiedostosta(hakemisto);
     *  Collection<Artisti> kaikki = kirjasto.etsi("",-1); 
     *  Iterator<Artisti> it = kaikki.iterator();
     *  it.next() === sefa1;
     *  it.next() === sefa2;
     *  it.hasNext() === false;
     *  List<Biisi> loytyneet = kirjasto.annaBiisit(sefa1);
     *  Iterator<Biisi> ih = loytyneet.iterator();
     *  ih.next() === calling11;
     *  ih.next() === calling12;
     *  ih.hasNext() === false;
     *  loytyneet = kirjasto.annaBiisit(sefa2);
     *  ih = loytyneet.iterator();
     *  ih.next() === calling21;
     *  ih.next() === calling22;
     *  ih.next() === calling23;
     *  ih.hasNext() === false;
     *  kirjasto.lisaa(sefa2);
     *  kirjasto.lisaa(calling23);
     *  kirjasto.tallenna();
     *  ftied.delete()  === true;
     *  fhtied.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        artistit = new Artistit(); // jos luetaan olemassa olevaan niin helpoin tyhjentää näin
        biisit = new Biisit();
  
        setTiedosto(nimi);
        artistit.lueTiedostosta();
        biisit.lueTiedostosta();
    }


    /**
     * Tallentaa kirjaston tiedot tiedostoon.  
     * Tiedoston muoto:
     * <pre>
     * Hardstyle kirjasto
     * 20
     * ; kommenttirivi
     * 2|Angerfist|137|Hollanti|1996
     * 3|Sefa|57|Hollanti|2013
     * 2|10|The Paradox|3:23|2017
     * 3|11|Calling|2:43|2018
     * </pre>
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            artistit.tallenna();
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }
        try {
            biisit.tallenna();
        }  catch ( SailoException ex ) {
            virhe += ex.getMessage();
        }
        if ( !"".equals(virhe) ) throw new SailoException(virhe);
    }


    /**
     * Testiohjelma kirjastosta
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Kirjasto kirjasto = new Kirjasto();

        try {

            Artisti sefa1 = new Artisti(), sefa2 = new Artisti();
            sefa1.rekisteroi();
            sefa1.vastaaAkuAnkka();
            sefa2.rekisteroi();
            sefa2.vastaaAkuAnkka();

            kirjasto.lisaa(sefa1);
            kirjasto.lisaa(sefa2);

            System.out.println("============= Kirjaston testi =================");
 

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }  
}