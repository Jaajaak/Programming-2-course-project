package kirjasto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;

/**
 * Kirjaston artistit joka osaa mm. lisätä uuden artistin
 *
 * @author Jaakko Vikström 
 * @version 1.0, 20.04.2019 
 */
public class Artistit implements Iterable<Artisti> {
    private static final int MAX_ARTISTEJA   = 10;
    private int              lkm           = 0;
    private Artisti            alkiot[]      = new Artisti[MAX_ARTISTEJA];
    private String tiedostonPerusNimi = "artistiid";
    private boolean muutettu = false;

    /**
     * Oletusmuodostaja
     */
    public Artistit() {
        // Attribuuttien oma alustus riittää
    }


    /**
     * Lisää uuden artistin tietorakenteeseen.  Ottaa artistin omistukseensa.
     * @param artisti lisätäävän artistin viite.  Huom tietorakenne muuttuu omistajaksi
     * @example
     * <pre name="test"> 
     * Artistit artistit = new Artistit();
     * Artisti sefa1 = new Artisti(), sefa2 = new Artisti();
     * artistit.getLkm() === 0;
     * artistit.lisaa(sefa1); artistit.getLkm() === 1;
     * artistit.lisaa(sefa2); artistit.getLkm() === 2;
     * artistit.lisaa(sefa1); artistit.getLkm() === 3;
     * artistit.anna(0) === sefa1;
     * artistit.anna(1) === sefa2;
     * artistit.anna(2) === sefa1;
     * artistit.anna(1) == sefa1 === false;
     * artistit.anna(1) == sefa2 === true;
     * artistit.anna(3) === sefa1; #THROWS IndexOutOfBoundsException 
     * artistit.lisaa(sefa1); artistit.getLkm() === 4;
     * artistit.lisaa(sefa1); artistit.getLkm() === 5;
     * artistit.lisaa(sefa1);
     * </pre>
     */
    public void lisaa(Artisti artisti) {
        if (lkm >= alkiot.length) {
            Artisti alkiot1[] = new Artisti[MAX_ARTISTEJA*2];
            System.arraycopy(alkiot, 0, alkiot1, 0, alkiot.length);
            alkiot = alkiot1;
        }
        alkiot[lkm] = artisti;
        lkm++;
        muutettu = true;
    }
    
    
    /**
     * Korvaa artistin tietorakenteessa.  Ottaa artistin omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva artisti.  Jos ei löydy,
     * niin lisätään uutena artistina.
     * @param artisti lisätäävän artistin viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Artistit artistit = new Artistit();
     * Artisti sefa1 = new Artisti(), sefa2 = new Artisti();
     * sefa1.rekisteroi(); sefa2.rekisteroi();
     * artistit.getLkm() === 0;
     * artistit.korvaaTaiLisaa(sefa1); artistit.getLkm() === 1;
     * artistit.korvaaTaiLisaa(sefa2); artistit.getLkm() === 2;
     * Artisti sefa3 = sefa1.clone();
     * Iterator<Artisti> it = artistit.iterator();
     * it.next() == sefa1 === true;
     * artistit.korvaaTaiLisaa(sefa3); artistit.getLkm() === 2;
     * it = artistit.iterator();
     * Artisti j0 = it.next();
     * j0 === sefa3;
     * j0 == sefa3 === true;
     * j0 == sefa1 === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Artisti artisti) throws SailoException {
        int id = artisti.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if ( alkiot[i].getTunnusNro() == id ) {
                alkiot[i] = artisti;
                muutettu = true;
                return;
            }
        }
        lisaa(artisti);
        
    }


    /**
     * Palauttaa viitteen i:teen artistiin.
     * @param i monennenko artistin viite halutaan
     * @return viite artistiin, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    public Artisti anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }


    /**
     * Lukee artistit tiedostosta. 
     * @param tied tiedoston perusnimi
     * @throws SailoException jos lukeminen epäonnistuu
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * 
     *  Artistit artistit = new Artistit();
     *  Artisti sefa1 = new Artisti(), sefa2 = new Artisti();
     *  sefa1.vastaaAkuAnkka();
     *  sefa2.vastaaAkuAnkka();
     *  String hakemisto = "testihardstyle";
     *  String tiedNimi = hakemisto+"/artistiid";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  artistit.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  artistit.lisaa(sefa1);
     *  artistit.lisaa(sefa2);
     *  artistit.tallenna();
     *  artistit = new Artistit();            // Poistetaan vanhat luomalla uusi
     *  artistit.lueTiedostosta(tiedNimi);  // johon ladataan tiedot tiedostosta.
     *  Iterator<Artisti> i = artistit.iterator();
     *  i.next() === sefa1;
     *  i.next() === sefa2;
     *  i.hasNext() === false;
     *  artistit.lisaa(sefa2);
     *  artistit.tallenna();
     *  ftied.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            String rivi = fi.readLine();
            if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");
            // int maxKoko = Mjonot.erotaInt(rivi,10); // tehdään jotakin

            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Artisti artisti = new Artisti();
                artisti.parse(rivi); // voisi olla virhekäsittely
                lisaa(artisti);
            }
            muutettu = false;
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }
    
    
    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallenustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tiedoston nimen
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }

    /**
     * perusnimi ilman tarkenninta
     * @param nimi hakemisto
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
        
    }
    
    
    /** 
     * Poistaa artistin jolla on valittu tunnusnumero  
     * @param id poistettavan artistin tunnusnumero 
     * @return 1 jos poistettiin, 0 jos ei löydy 
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Artistit artistit = new Artistit(); 
     * Artisti sefa1 = new Artisti(), sefa2 = new Artisti(), sefa3 = new Artisti(); 
     * sefa1.rekisteroi(); sefa2.rekisteroi(); sefa3.rekisteroi(); 
     * int id1 = sefa1.getTunnusNro(); 
     * artistit.lisaa(sefa1); artistit.lisaa(sefa2); artistit.lisaa(sefa3); 
     * artistit.poista(id1+1) === 1; 
     * artistit.annaId(id1+1) === null; artistit.getLkm() === 2; 
     * artistit.poista(id1) === 1; artistit.getLkm() === 1; 
     * artistit.poista(id1+3) === 0; artistit.getLkm() === 1; 
     * </pre> 
     *  
     */ 
    public int poista(int id) { 
        int ind = etsiId(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    } 
    
    
    /** 
     * Etsii artistin id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitään 
     * @return artisti jolla etsittävä id tai null 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Artistit artistit = new Artistit(); 
     * Artisti sefa1 = new Artisti(), sefa2 = new Artisti(), sefa3 = new Artisti(); 
     * sefa1.rekisteroi(); sefa2.rekisteroi(); sefa3.rekisteroi(); 
     * int id1 = sefa1.getTunnusNro(); 
     * artistit.lisaa(sefa1); artistit.lisaa(sefa2); artistit.lisaa(sefa3); 
     * artistit.annaId(id1  ) == sefa1 === true; 
     * artistit.annaId(id1+1) == sefa2 === true; 
     * artistit.annaId(id1+2) == sefa3 === true; 
     * </pre> 
     */ 
    public Artisti annaId(int id) { 
        for (Artisti artisti : this) { 
            if (id == artisti.getTunnusNro()) return artisti; 
        } 
        return null; 
    } 
    
    
    /** 
     * Etsii artistin id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitään 
     * @return löytyneen artistin indeksi tai -1 jos ei löydy 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Artistit artistit = new Artistit(); 
     * Artisti sefa1 = new Artisti(), sefa2 = new Artisti(), sefa3 = new Artisti(); 
     * sefa1.rekisteroi(); sefa2.rekisteroi(); sefa3.rekisteroi(); 
     * int id1 = sefa1.getTunnusNro(); 
     * artistit.lisaa(sefa1); artistit.lisaa(sefa2); artistit.lisaa(sefa3); 
     * artistit.etsiId(id1+1) === 1; 
     * artistit.etsiId(id1+2) === 2; 
     * </pre> 
     */ 
    public int etsiId(int id) { 
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getTunnusNro()) return i; 
        return -1; 
    } 

    
    
    /**
     * @return bak nimi
     * 
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }


    /**
     * Palauttaa kerhon jäsenten lukumäärän
     * @return jäsenten lukumäärä
     */
    public int getLkm() {
        return lkm;
    }


    /**
     * Tallentaa artistit tiedostoon.  
     * Tiedoston muoto:
     * <pre>
     * Hardstyle kirjasto
     * 20
     * ; kommenttirivi
     * 2|Angerfist|137|Hollanti|1996
     * 3|Sefa|57|Hollanti|2013
     * </pre>
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;
        
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        ftied.renameTo(fbak); // if .. System.err.println("Ei voi nimetä");
        fbak.delete(); // if .. System.err.println("Ei voi tuhota");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(alkiot.length);
            for (Artisti artisti : this) {
                fo.println(artisti.toString());
            }
            //} catch ( IOException e ) { // ei heitä poikkeusta
        //  throw new SailoException("Tallettamisessa ongelmia: " + e.getMessage());
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        muutettu = false;       
    }
    
    
    /**
    * Luokka artistien iteroimiseksi.
    * @example
    * <pre name="test">
    * #THROWS SailoException 
    * #PACKAGEIMPORT
    * #import java.util.*;
    * 
    * Artistit artistit = new Artistit();
    * Artisti sefa1 = new Artisti(), sefa2 = new Artisti();
    * sefa1.rekisteroi(); sefa2.rekisteroi();
    *
    * artistit.lisaa(sefa1); 
    * artistit.lisaa(sefa2); 
    * artistit.lisaa(sefa1); 
    * 
    * StringBuffer ids = new StringBuffer(30);
    * for (Artisti artisti:artistit)   // Kokeillaan for-silmukan toimintaa
    *   ids.append(" "+artisti.getTunnusNro());           
    * 
    * String tulos = " " + sefa1.getTunnusNro() + " " + sefa2.getTunnusNro() + " " + sefa1.getTunnusNro();
    * 
    * ids.toString() === tulos; 
    * 
    * ids = new StringBuffer(30);
    * for (Iterator<Artisti>  i=artistit.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
    *   Artisti artisti = i.next();
    *   ids.append(" "+artisti.getTunnusNro());           
    * }
    * 
    * ids.toString() === tulos;
    * 
    * Iterator<Artisti>  i=artistit.iterator();
    * i.next() == sefa1  === true;
    * i.next() == sefa2  === true;
    * i.next() == sefa1  === true;
    * 
    * i.next();  #THROWS NoSuchElementException
    *  
    * </pre>
    */
   public class ArtistitIterator implements Iterator<Artisti> {
       private int kohdalla = 0;


       /**
        * Onko olemassa vielä seuraavaa artistia
        * @see java.util.Iterator#hasNext()
        * @return true jos on vielä artisteja
        */
       @Override
       public boolean hasNext() {
           return kohdalla < getLkm();
       }


       /**
        * Annetaan seuraava artisti
        * @return seuraava artisti
        * @throws NoSuchElementException jos seuraava alkiota ei enää ole
        * @see java.util.Iterator#next()
        */
       @Override
       public Artisti next() throws NoSuchElementException {
           if ( !hasNext() ) throw new NoSuchElementException("Ei ole");
           return anna(kohdalla++);
       }


       /**
        * Tuhoamista ei ole toteutettu
        * @throws UnsupportedOperationException aina
        * @see java.util.Iterator#remove()
        */
       @Override
       public void remove() throws UnsupportedOperationException {
           throw new UnsupportedOperationException("Me ei poisteta");
       }
   }


   /**
    * Palautetaan iteraattori artisteistaan.
    * @return artisti iteraattori
    */
   @Override
   public Iterator<Artisti> iterator() {
       return new ArtistitIterator();
   }


   /** 
    * Palauttaa "taulukossa" hakuehtoon vastaavien artistien viitteet 
    * @param hakuehto hakuehto 
    * @param k etsittävän kentän indeksi  
    * @return tietorakenteen löytyneistä artisteista 
    * @example 
    * <pre name="test"> 
    * #THROWS SailoException  
    *   Artistit artistit = new Artistit(); 
    *   Artisti artisti1 = new Artisti(); artisti1.parse("1|Angerfist|137|Hollanti|"); 
    *   Artisti artisti2 = new Artisti(); artisti2.parse("2|Sefa|40|Hollanti|"); 
    *   Artisti artisti3 = new Artisti(); artisti3.parse("3|Da Tweekaz|77|Norja|2006"); 
    *   Artisti artisti4 = new Artisti(); artisti4.parse("4|B-Front|35|Hollanti"); 
    *   Artisti artisti5 = new Artisti(); artisti5.parse("5|Phuture Noize|60|Hollanti"); 
    *   artistit.lisaa(artisti1); artistit.lisaa(artisti2); artistit.lisaa(artisti3); artistit.lisaa(artisti4); artistit.lisaa(artisti5);
    * </pre> 
    */ 
   @SuppressWarnings("unused")
   public Collection<Artisti> etsi(String hakuehto, int k) { 
       String ehto = "*"; 
       if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto; 
       int hk = k; 
       if ( hk < 0 ) hk = 1;
       List<Artisti> loytyneet = new ArrayList<Artisti>(); 
       for (Artisti artisti : this) { 
           if (WildChars.onkoSamat(artisti.anna(hk), ehto)) loytyneet.add(artisti);   
       } 
       Collections.sort(loytyneet);
       return loytyneet; 
   }
   
   
   /**
    * Testiohjelma artisteille
    * @param args ei käytössä
    */
   public static void main(String args[]) {
       Artistit artistit = new Artistit();

       Artisti sefa = new Artisti(), sefa2 = new Artisti();
       sefa.rekisteroi();
       sefa.vastaaAkuAnkka();
       sefa2.rekisteroi();
       sefa2.vastaaAkuAnkka();

       artistit.lisaa(sefa);
       artistit.lisaa(sefa2);

       System.out.println("============= Artistin testi =================");
   }
}