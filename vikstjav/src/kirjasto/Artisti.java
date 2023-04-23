package kirjasto;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Tietue;
/**
    * Kirjaston artisti joka osaa mm. itse huolehtia aid:staan.
    *
   * @author Jaakko Vikström
   * @version 1.0, 20.04.2019
   */
  public class Artisti implements Cloneable, Tietue, Comparable<Artisti> {
      private int        aid;
      private String     nimi           = "";
      private String     kotimaa           = "";
      private String        biisienmaara = "";
      private int        aloitusvuosi = 0;
  
      private static int seuraavaNro    = 1;
      
      
   
      /**
       * @return Artistin nimi
       * @example
       * <pre name="test">
       *   Artisti sefa = new Artisti();
       *   sefa.vastaaAkuAnkka();
       *   sefa.getNimi() =R= "Angerfist.*";
       * </pre>
       */
      public String getNimi() {
          return nimi;
      }
      
      
      /**
       * Palauttaa artistin kenttien lukumäärän
       * @return kenttien lukumäärä
       */
      @Override
    public int getKenttia() {
          return 5;
      }
      
      
      /**
       * Eka kenttä joka on mielekäs kysyttäväksi
       * @return ekan kentän indeksi
       */
      @Override
    public int ekaKentta() {
          return 1;
      }
  
  
      /**
       * Apumetodi, jolla saadaan täytettyä testiarvot artistille.
       */
      public void vastaaAkuAnkka() {
          nimi = "Angerfist";
          kotimaa = "Hollanti";
          biisienmaara = "137";
          aloitusvuosi = 1996;
      }
      
      
      /**
       * Antaa k:n kentän sisällön merkkijonona
       * @param k monenenko kentän sisältö palautetaan
       * @return kentän sisältö merkkijonona
       */
      @Override
    public String anna(int k) {
          switch ( k ) {
          case 0: return "" + aid;
          case 1: return "" + nimi;
          case 2: return "" + biisienmaara;
          case 3: return "" + kotimaa;
          case 4: return "" + aloitusvuosi;
          default: return "Väärä";
          }
      }
      
      
      /**
       * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon
       * @param k kuinka monennen kentän arvo asetetaan
       * @param jono jonoa joka asetetaan kentän arvoksi
       * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
       * @example
       * <pre name="test">
       *   Artisti artisti = new Artisti();
       *   artisti.aseta(1,"Angerfist") === null;
       *   artisti.aseta(2,"137") === null; 
       *   artisti.aseta(4,"1996") === null;
       * </pre>
       */
      @Override
    public String aseta(int k, String jono) {
          String tjono = jono.trim();
          StringBuffer sb = new StringBuffer(tjono);
          switch ( k ) {
          case 0:
              setTunnusNro(Mjonot.erota(sb, '§', getTunnusNro()));
              return null;
          case 1:
              try {
              nimi = tjono;
              return null;
              } catch (NullPointerException ex) {
                  return "Nimi ei voi olla tyhjä" + ex.getMessage();
              }
          case 2:
              biisienmaara = tjono;
              return null;
          case 3:
              kotimaa = tjono;
              return null;
          case 4:
              try {
                  aloitusvuosi = Mjonot.erotaEx(sb, '§', aloitusvuosi);
              } catch ( NumberFormatException ex ) {
                  return "aloitusvuosi väärin " + ex.getMessage();
              }
              return null;
          default:
              return "Väärä";
          }
      }
      
      
      /**
       * Palauttaa k:tta artistin kenttää vastaavan kysymyksen
       * @param k kuinka monennen kentän kysymys palautetaan (0-alkuinen)
       * @return k:netta kenttää vastaava kysymys
       */
      @Override
    public String getKysymys(int k) {
          switch ( k ) {
          case 0: return "Tunnus nro";
          case 1: return "nimi";
          case 2: return "biisienmaara";
          case 3: return "kotimaa";
          case 4: return "aloitusvuosi";
          default: return "Äääliö";
          }
      }


   /**
    * Antaa artistille seuraavan rekisterinumeron.
    * @return artistin uusi tunnusNro
    * @example
    * <pre name="test">
    *   Artisti sefa1 = new Artisti();
    *   sefa1.getTunnusNro() === 0;
    *   sefa1.rekisteroi();
    *   Artisti sefa2 = new Artisti();
    *   sefa2.rekisteroi();
    *   int n1 = sefa1.getTunnusNro();
    *   int n2 = sefa2.getTunnusNro();
    *   n1 === n2-1;
    * </pre>
    */
   public int rekisteroi() {
       aid = seuraavaNro;
       seuraavaNro++;
       return aid;
   }


   /**
    * Palauttaa artistin tunnusnumeron.
    * @return artistin aid
    */
   public int getTunnusNro() {
       return aid;
   }

   
   /**
    * Asettaa tunnusnumeron ja samalla varmistaa että
    * seuraava numero on aina suurempi kuin tähän mennessä suurin.
    * @param nr asetettava tunnusnumero
    */
    private void setTunnusNro(int nr) {
         aid = nr;
         if (aid >= seuraavaNro) seuraavaNro = aid + 1;
    }

    
    /**
    * Palauttaa artistin tiedot merkkijonona jonka voi tallentaa tiedostoon.
    * @return artisti tolppaeroteltuna merkkijonona 
    * @example
    * <pre name="test">
    *   Artisti artisti = new Artisti();
    *   artisti.parse("   3  |  Angerfist   | 137");
    *   artisti.toString().startsWith("3|Angerfist|137|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
    * </pre>  
    */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        String erotin = "";
        for (int k = 0; k < getKenttia(); k++) {
            sb.append(erotin);
            sb.append(anna(k));
            erotin = "|";
        }
        return sb.toString();
    }
             
        
     /**
      * Selvitää artistin tiedot | erotellusta merkkijonosta
      * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusNro.
      * @param rivi josta artistin tiedot otetaan
      * 
      * @example
      * <pre name="test">
      *   Artisti artisti = new Artisti();
      *   artisti.parse("   3  |  Angerfist   | 137");
      *   artisti.getTunnusNro() === 3;
      *   artisti.toString().startsWith("3|Angerfist|137|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
      *
      *   artisti.rekisteroi();
      *   int n = artisti.getTunnusNro();
      *   artisti.parse(""+(n+20));       // Otetaan merkkijonosta vain tunnusnumero
      *   artisti.rekisteroi();           // ja tarkistetaan että seuraavalla kertaa tulee yhtä isompi
      *   artisti.getTunnusNro() === n+20+1;
      *     
      * </pre>
      */
      public void parse(String rivi) {
          StringBuffer sb = new StringBuffer(rivi);
          for (int k = 0; k < getKenttia(); k++)
              aseta(k, Mjonot.erota(sb, '|'));
      }
      
      
      /**
       * Tehdään identtinen klooni artistista
       * @return Object kloonattu artisti
       * @example
       * <pre name="test">
       * #THROWS CloneNotSupportedException 
       *   Artisti artisti = new Artisti();
       *   artisti.parse("   3  |  Angerfist  | 137");
       *   Artisti kopio = artisti.clone();
       *   kopio.toString() === artisti.toString();
       *   artisti.parse("   4  |  Ankka Tupu   | 123");
       *   kopio.toString().equals(artisti.toString()) === false;
       * </pre>
       */
      @Override
      public Artisti clone() throws CloneNotSupportedException {
          Artisti uusi;
          uusi = (Artisti) super.clone();
          return uusi;
      }
                 
                 
      /**
       * Tutkii onko jäsenen tiedot samat kuin parametrina tuodun artistin tiedot
       * @param artisti artisti johon verrataan
       * @return true jos kaikki tiedot samat, false muuten
       * @example
       * <pre name="test">
       *   Artisti artisti1 = new Artisti();
       *   artisti1.parse("   3  |  Angerfist   | 137");
       *   Artisti artisti2 = new Artisti();
       *   artisti2.parse("   3  |  Angerfist   | 137");
       *   Artisti artisti3 = new Artisti();
       *   artisti3.parse("   3  |  Happyface   | 15");
       *   
       *   artisti1.equals(artisti2) === true;
       *   artisti2.equals(artisti1) === true;
       *   artisti1.equals(artisti3) === false;
       *   artisti3.equals(artisti2) === false;
       * </pre>
       */
      public boolean equals(Artisti artisti) {
          if ( artisti == null ) return false;
          for (int k = 0; k < getKenttia(); k++)
              if ( !anna(k).equals(artisti.anna(k)) ) return false;
          return true;
      }
        
      
      @Override
      public boolean equals(Object artisti) {
          if ( artisti instanceof Artisti ) return equals((Artisti)artisti);
          return false;
      }
      
             
      @Override
      public int hashCode() {
             return aid;
      }
      
      
      /**
       * vertaa nimeä
       */
      @Override
      public int compareTo(Artisti artisti) {
          return nimi.compareTo(artisti.nimi);
      }
             
   
   /**
    * Testiohjelma artistille.
    * @param args ei käytössä
    */
   public static void main(String args[]) {
       Artisti sefa = new Artisti(), sefa2 = new Artisti();
       sefa.rekisteroi();
       sefa2.rekisteroi();
       sefa.vastaaAkuAnkka();

         sefa2.vastaaAkuAnkka();
 
         sefa2.vastaaAkuAnkka();
     }
 }