package kirjasto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Kirjaston biisit, joka osaa mm. lisätä uuden biisin
 *
 * @author Jaakko Vikström
 * @version 1.0, 24.04.2019
 */
public class Biisit implements Iterable<Biisi> {
    private boolean muutettu = false;
    private String tiedostonPerusNimi = "";

    /** Taulukko biiseistä */
    private final Collection<Biisi> alkiot = new ArrayList<Biisi>();


    /**
     * Biisien alustaminen
     */
    public Biisit() {
        // toistaiseksi ei tarvitse tehdä mitään
    }


    /**
     * Lisää uuden biisin tietorakenteeseen.  Ottaa biisin omistukseensa.
     * @param song liisaa biisi
     */
    public void lisaa(Biisi song) {
        alkiot.add(song);
        muutettu = true;
    }


    /**
     * Lukee biisit tiedostosta.
     * @param tied tiedoston nimen alkuosa
     * @throws SailoException jos lukeminen epäonnistuu
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Biisit kappaleet = new Biisit();
     *  Biisi calling21 = new Biisi(); calling21.vastaaCalling(2);
     *  Biisi calling11 = new Biisi(); calling11.vastaaCalling(1);
     *  Biisi calling22 = new Biisi(); calling22.vastaaCalling(2); 
     *  Biisi calling12 = new Biisi(); calling12.vastaaCalling(1); 
     *  Biisi calling23 = new Biisi(); calling23.vastaaCalling(2); 
     *  String tiedNimi = "testihardstyle";
     *  File ftied = new File(tiedNimi+".dat");
     *  ftied.delete();
     *  kappaleet.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  kappaleet.lisaa(calling21);
     *  kappaleet.lisaa(calling11);
     *  kappaleet.lisaa(calling22);
     *  kappaleet.lisaa(calling12);
     *  kappaleet.lisaa(calling23);
     *  kappaleet.tallenna();
     *  kappaleet = new Biisit();
     *  kappaleet.lueTiedostosta(tiedNimi);
     *  Iterator<Biisi> i = kappaleet.iterator();
     *  i.next().toString() === calling21.toString();
     *  i.next().toString() === calling11.toString();
     *  i.next().toString() === calling22.toString();
     *  i.next().toString() === calling12.toString();
     *  i.next().toString() === calling23.toString();
     *  i.hasNext() === false;
     *  kappaleet.lisaa(calling23);
     *  kappaleet.tallenna();
     * </pre>
     */
     public void lueTiedostosta(String tied) throws SailoException {
         setTiedostonPerusNimi(tied);
         try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
             String rivi;
             while ( (rivi = fi.readLine()) != null ) {
                 rivi = rivi.trim();
                 if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                 Biisi song = new Biisi();
                 song.parse(rivi); // voisi olla virhekäsittely
                 lisaa(song);
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
      * Tallentaa biisit tiedostoon.  
      * Tiedoston muoto:
      * <pre>
      * Hardstyle kirjasto
      * 20
      * ; kommenttirivi
      * 2|10|The Paradox|3:23|2017
      * 3|11|Calling|2:43|2018
      * </pre>
      * @throws SailoException jos talletus epäonnistuu
      */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;
        
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); //  if ... System.err.println("Ei voi tuhota");
        
        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Biisi song : this) {
                fo.println(song.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        }   catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        muutettu = false;
    }
    
    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta
     * @param tied tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
        
    }

    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    
    /**
     * @return tallennustiedoston nimi
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }
    
    
    /**
     * @return kopion nimi
     * 
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }


    /**
     * Palauttaa kirjaston biisien lukumäärän
     * @return biisien lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }


    /**
     * Iteraattori kaikkien Biisien läpikäymiseen
     * @return biisiiteraattori
     * 
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     *  Biisit kappaleet = new Biisit();
     *  Biisi calling21 = new Biisi(2); kappaleet.lisaa(calling21);
     *  Biisi calling11 = new Biisi(1); kappaleet.lisaa(calling11);
     *  Biisi calling22 = new Biisi(2); kappaleet.lisaa(calling22);
     *  Biisi calling12 = new Biisi(1); kappaleet.lisaa(calling12);
     *  Biisi calling23 = new Biisi(2); kappaleet.lisaa(calling23);
     * 
     *  Iterator<Biisi> i2=kappaleet.iterator();
     *  i2.next() === calling21;
     *  i2.next() === calling11;
     *  i2.next() === calling22;
     *  i2.next() === calling12;
     *  i2.next() === calling23;
     *  i2.next() === calling12;  #THROWS NoSuchElementException  
     *  
     *  int n = 0;
     *  int jnrot[] = {2,1,2,1,2};
     *  
     *  for ( Biisi song:kappaleet ) { 
     *    song.getJasenNro() === jnrot[n]; n++;  
     *  }
     *  
     *  n === 5;
     *  
     * </pre>
     */
    @Override
    public Iterator<Biisi> iterator() {
        return alkiot.iterator();
    }


    /**
     * Haetaan kaikki jäsen Biisit
     * @param aid artistin tunnusnumero jolle biisejä haetaan
     * @return tietorakenne jossa viiteet löydetteyihin biiseihin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Biisit kappaleet = new Biisit();
     *  Biisi calling21 = new Biisi(2); kappaleet.lisaa(calling21);
     *  Biisi calling11 = new Biisi(1); kappaleet.lisaa(calling11);
     *  Biisi calling22 = new Biisi(2); kappaleet.lisaa(calling22);
     *  Biisi calling12 = new Biisi(1); kappaleet.lisaa(calling12);
     *  Biisi calling23 = new Biisi(2); kappaleet.lisaa(calling23);
     *  Biisi calling51 = new Biisi(5); kappaleet.lisaa(calling51);
     *  
     *  List<Biisi> loytyneet;
     *  loytyneet = kappaleet.annaBiisit(3);
     *  loytyneet.size() === 0; 
     *  loytyneet = kappaleet.annaBiisit(1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == calling11 === true;
     *  loytyneet.get(1) == calling12 === true;
     *  loytyneet = kappaleet.annaBiisit(5);
     *  loytyneet.size() === 1; 
     *  loytyneet.get(0) == calling51 === true;
     * </pre> 
     */
    public List<Biisi> annaBiisit(int aid) {
        List<Biisi> loydetyt = new ArrayList<Biisi>();
        for (Biisi song : alkiot)
            if (song.getJasenNro() == aid) loydetyt.add(song);
        return loydetyt;
    }
    
     
    /**
     * Korvaa biisin tietorakenteessa.  Ottaa biisin omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva Biisi.  Jos ei löydy,
     * niin lisätään uutena biisinä.
     * @param biisi lisättävän biisin viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Biisit songs = new Biisit();
     * Biisi song1 = new Biisi(), song2 = new Biisi();
     * song1.rekisteroi(); song2.rekisteroi();
     * songs.getLkm() === 0;
     * songs.korvaaTaiLisaa(song1); songs.getLkm() === 1;
     * songs.korvaaTaiLisaa(song2); songs.getLkm() === 2;
     * Biisi song3 = song1.clone();
     * song3.aseta(2,"kkk");
     * Iterator<Biisi> i2=songs.iterator();
     * i2.next() === song1;
     * songs.korvaaTaiLisaa(song3); songs.getLkm() === 2;
     * i2=songs.iterator();
     * Biisi h = i2.next();
     * h === song3;
     * h == song3 === true;
     * h == song1 === false;
     * </pre>
     */ 
    public void korvaaTaiLisaa(Biisi biisi) throws SailoException {
        int id = biisi.getTunnusNro();
        for (int i = 0; i < getLkm(); i++) {
            if (((ArrayList<Biisi>) alkiot).get(i).getTunnusNro() == id) {
                ((ArrayList<Biisi>) alkiot).set(i, biisi);
                muutettu = true;
                return;
            }
        }
        lisaa(biisi);
    }
    
    
    /**
     * Poistaa valitun biisin
     * @param song poistettava biisi
     * @return tosi jos löytyi poistettava tietue 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Biisit kappaleet = new Biisit();
     *  Biisi calling21 = new Biisi(); calling21.vastaaCalling(2);
     *  Biisi calling11 = new Biisi(); calling11.vastaaCalling(1);
     *  Biisi calling22 = new Biisi(); calling22.vastaaCalling(2); 
     *  Biisi calling12 = new Biisi(); calling12.vastaaCalling(1); 
     *  Biisi calling23 = new Biisi(); calling23.vastaaCalling(2); 
     *  kappaleet.lisaa(calling21);
     *  kappaleet.lisaa(calling11);
     *  kappaleet.lisaa(calling22);
     *  kappaleet.lisaa(calling12);
     *  kappaleet.poista(calling23) === true ; kappaleet.getLkm() === 3;
     *  kappaleet.poista(calling11) === true;   kappaleet.getLkm() === 2;
     *  List<Biisi> h = kappaleet.annaBiisit(1);
     *  h.size() === 1; 
     *  h.get(0) === calling12;
     * </pre>
     */
    public boolean poista(Biisi song) {
        boolean ret = alkiot.remove(song);
        if (ret) muutettu = true;
        return ret;
    }
    
    
    /**
     * Poistaa kaikki tietyn tietyn artistin biisit
     * @param tunnusNro viite siihen, mihin liittyvät tietueet poistetaan
     * @return montako poistettiin 
     * @example
     * <pre name="test">
     *  Biisit kappaleet = new Biisit();
     *  Biisi calling21 = new Biisi(); calling21.vastaaCalling(2);
     *  Biisi calling11 = new Biisi(); calling11.vastaaCalling(1);
     *  Biisi calling22 = new Biisi(); calling22.vastaaCalling(2); 
     *  Biisi calling12 = new Biisi(); calling12.vastaaCalling(1); 
     *  Biisi calling23 = new Biisi(); calling23.vastaaCalling(2); 
     *  kappaleet.lisaa(calling21);
     *  kappaleet.lisaa(calling11);
     *  kappaleet.lisaa(calling22);
     *  kappaleet.lisaa(calling12);
     *  kappaleet.lisaa(calling23);
     *  kappaleet.poistaArtistinBiisit(2) === 3;  kappaleet.getLkm() === 2;
     *  kappaleet.poistaArtistinBiisit(3) === 0;  kappaleet.getLkm() === 2;
     *  List<Biisi> h = kappaleet.annaBiisit(2);
     *  h.size() === 0; 
     *  h = kappaleet.annaBiisit(1);
     *  h.get(0) === calling11;
     *  h.get(1) === calling12;
     * </pre>
     */
    public int poistaArtistinBiisit(int tunnusNro) {
        int n = 0;
        for (Iterator<Biisi> it = alkiot.iterator(); it.hasNext();) {
            Biisi har = it.next();
            if ( har.getJasenNro() == tunnusNro ) {
                it.remove();
                n++;
            }
        }
        if (n > 0) muutettu = true;
        return n;
    }


    /**
     * Testiohjelma biiseille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Biisit songs = new Biisit();
        Biisi song1 = new Biisi();
        song1.vastaaCalling(2);
        Biisi song2 = new Biisi();
        song2.vastaaCalling(1);
        Biisi song3 = new Biisi();
        song3.vastaaCalling(2);
        Biisi song4 = new Biisi();
        song4.vastaaCalling(2);

        songs.lisaa(song1);
        songs.lisaa(song2);
        songs.lisaa(song3);
        songs.lisaa(song4);

        System.out.println("============= Biisit testi =================");

        List<Biisi> kappaleet = songs.annaBiisit(2);

        for (Biisi song : kappaleet) {
            System.out.print(song.getJasenNro() + " ");
            song.tulosta(System.out);
        }

    }


   
}
