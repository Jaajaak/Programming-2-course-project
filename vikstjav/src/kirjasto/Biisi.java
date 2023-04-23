package kirjasto;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Tietue;

/**
    * Biisi joka osaa mm. itse huolehtia tunnus_nro:staan.
    *    
    *     @author Jaakko Vikström
    *      @version 1.0, 23.4.2019
    */
  public class Biisi implements Cloneable, Tietue {
      private int aid;
      private int jasenNro;
      private String nimi = "";
      private int julkaisuvuosi = 0;
      private String  kesto = "";
  
      private static int seuraavaNro = 1;
  
  
      /**
       * Alustetaan harrastus.  Toistaiseksi ei tarvitse tehdä mitään
       */
      public Biisi() {
          // Vielä ei tarvita mitään
      }
      
      
      /**
       * @return harrastukse kenttien lukumäärä
       */
      @Override
    public int getKenttia() {
          return 5;
      }
      
      
      /**
       * @return ensimmäinen käyttäjän syötettävän kentän indeksi
       */
      @Override
    public int ekaKentta() {
          return 2;
      }
  
  
      /**
       * Alustetaan tietyn artistin biisi.  
       * @param jasenNro artistin viitenumero 
       */
      public Biisi(int jasenNro) {
          this.jasenNro = jasenNro;
      }
      
      
      /**
       * @param k minkä kentän kysymys halutaan
       * @return valitun kentän kysymysteksti
       */
      @Override
    public String getKysymys(int k) {
          switch (k) {
              case 0:
                  return "id";
              case 1:
                  return "jäsenId";
              case 2:
                  return "nimi";
              case 3:
                  return "kesto";
              case 4:
                  return "julkaisuvuosi";
              default:
                  return "???";
          }
      }
      
      
      /**
       * @param k Minkä kentän sisältö halutaan
       * @return valitun kentän sisältö
       * @example
       * <pre name="test">
       *   Biisi song = new Biisi();
       *   song.parse("   2   | 10 | The Paradox  |  3:23 | 2017 ");
       *   song.anna(0) === "2";   
       *   song.anna(1) === "10";   
       *   song.anna(2) === "The Paradox";   
       *   song.anna(3) === "3:23";   
       *   song.anna(4) === "2017";   
       *   
       * </pre>
       */
      @Override
    public String anna(int k) {
          switch (k) {
              case 0:
                  return "" + aid;
              case 1:
                  return "" + jasenNro;
              case 2:
                  return nimi;
              case 3:
                  return "" + kesto;
              case 4:
                  return "" + julkaisuvuosi;
              default:
                  return "???";
          }
      }
      
      
      /**
       * Asetetaan valitun kentän sisältö.  Mikäli asettaminen onnistuu,
       * palautetaan null, muutoin virheteksti.
       * @param k minkä kentän sisältö asetetaan
       * @param s asetettava sisältö merkkijonona
       * @return null jos ok, muuten virheteksti
       * @example
       * <pre name="test">
       *   Biisi song = new Biisi();
       *   song.aseta(4,"kissa") === "julkaisuvuosi väärin jono = \"kissa\"";
       *   song.aseta(4,"1940")  === null;
       *   
       * </pre>
       */
      @Override
    public String aseta(int k, String s) {
          String st = s.trim();
          StringBuffer sb = new StringBuffer(st);
          switch (k) {
              case 0:
                  setTunnusNro(Mjonot.erota(sb, '$', getTunnusNro()));
                  return null;
              case 1:
                  jasenNro = Mjonot.erota(sb, '$', jasenNro);
                  return null;
              case 2:
                  nimi = st;
                  return null;
              case 3:
                  kesto = st;
                  return null;

              case 4:
                  try {
                      julkaisuvuosi = Mjonot.erotaEx(sb, '§', julkaisuvuosi);
                  } catch ( NumberFormatException ex ) {
                      return "julkaisuvuosi väärin " + ex.getMessage();
                  }
                  return null;
              default:
                  return "Väärä kentän indeksi";
          }
      }
      
      
      /**
       * Apumetodi, jolla saadaan täytettyä testiarvot biisille.
       * @param nro viite artistiin, jonka biisistä on kyse
       */
      public void vastaaCalling(int nro) {
          jasenNro = nro;
          nimi = "The Paradox";
          kesto = "3:23";
          julkaisuvuosi = 2017;
      }
  
  
      /**
       * Tulostetaan Biisin tiedot
       * @param out tietovirta johon tulostetaan
       */
      public void tulosta(PrintStream out) {
          out.println(nimi);
      }
  
  
      /**
       * Tulostetaan henkilön tiedot
       * @param os tietovirta johon tulostetaan
       */
      public void tulosta(OutputStream os) {
          tulosta(new PrintStream(os));
      }
  
  
      /**
       * Antaa biisille seuraavan rekisterinumeron.
       * @return biisin uusi tunnus_nro
       * @example
       * <pre name="test">
       *   Biisi Calling1 = new Biisi();
       *   Calling1.getTunnusNro() === 0;
       *   Calling1.rekisteroi();
       *   Biisi Calling2 = new Biisi();
       *   Calling2.rekisteroi();
       *   int n1 = Calling1.getTunnusNro();
       *   int n2 = Calling2.getTunnusNro();
       *   n1 === n2-1;
       * </pre>
       */
      public int rekisteroi() {
          aid = seuraavaNro;
          seuraavaNro++;
          return aid;
      }
  
  
      /**
       * Palautetaan Biisin oma id
       * @return biisin id
       */
      public int getTunnusNro() {
          return aid;
      }
 
 
     /**
      * Palautetaan mille artistille biisi kuuluu
      * @return artistin id
      */
     public int getJasenNro() {
         return jasenNro;
     }
     
     
     /**
      * Asettaa tunnusnumeron ja samalla varmistaa että
      * seuraava numero on aina suurempi kuin tähän mennessä suurin.
      * @param nr asetettava tunnusnumero
      */
     private void setTunnusNro(int nr) {
         aid = nr;
         if ( aid >= seuraavaNro ) seuraavaNro = aid + 1;
     }
     
     
     /**
      * Palauttaa biisin tiedot merkkijonona jonka voi tallentaa tiedostoon.
      * @return biisi tolppaeroteltuna merkkijonona 
      * @example
      * <pre name="test">
      *   Biisi song = new Biisi();
      *   song.parse("   2   |  10  |   The Paradox  | 3:23 | 2017 ");
      *   song.toString()    === "2|10|The Paradox|3:23|2017";
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
      * Selvitää biisin tiedot | erotellusta merkkijonosta.
      * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusnro.
      * @param rivi josta biisin tiedot otetaan
      * @example
      * <pre name="test">
      *   Biisi song = new Biisi();
      *   song.parse("   2   |  10  |   The Paradox  | 3:23 | 2017 ");
      *   song.getJasenNro() === 10;
      *   song.toString()    === "2|10|The Paradox|3:23|2017";
      * </pre>
      */
     public void parse(String rivi) {
         StringBuffer sb = new StringBuffer(rivi);
         for (int k = 0; k < getKenttia(); k++)
             aseta(k, Mjonot.erota(sb, '|'));
     }
    
    
    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) return false;
        return this.toString().equals(obj.toString());
    }
 
    
    @Override
    public int hashCode() {
        return aid;
    }
    
    
    /**
     * Tehdään identtinen klooni biisistä
     * @return Object kloonattu biisi
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Biisi song = new Biisi();
     *   song.parse("   2   |  10  |   The Paradox  | 3:23 | 2017 ");
     *   Biisi kopio = song.clone();
     *   kopio.toString() === song.toString();
     *   song.parse("   1   |  11  |   Calling  | 2:43 | 2018 ");
     *   kopio.toString().equals(song.toString()) === false;
     * </pre>
     */
    @Override
    public Biisi clone() throws CloneNotSupportedException { 
        return (Biisi)super.clone();
    }
    
    
    /**
     * @return biisin nimi
     * @example
     * <pre name="test">
     *   Biisi song = new Biisi();
     *   song.vastaaCalling(2);
     *   song.getNimi() =R= "The Paradox.*";
     * </pre>
     */
    public String getNimi() {
        return nimi;
    }
    
    
    /**
   * @return julkaisuvuosi
   * @example
   * pre name="test">
   *   Biisi song = new Biisi();
   *   sefa.vastaaCalling();
   *   sefa.getVuosi() =R= "137 .*";
   * </pre>
   */
  public int getVuosi() {
        return julkaisuvuosi;
    }


    /**
   * @return kesto
   * @example
   * pre name="test">
   *   Biisi song = new Biisi();
   *   sefa.vastaaCalling();
   *   sefa.getKesto() =R= "3:23 .*";
   * </pre>
   */
  public String getKesto() {
       return kesto;
    }
  
  
  /**
   * @param s biisille laitettava nimi
   * @return null jos ok, muuten virhe
   */
  public String setNimi(String s) {
      nimi = s;
      return null;
  }
  
  
  /**
   * @param s biisille laitettava vuosi
   * @return null jos ok, muuten virhe
   */
  public int setVuosi(int s) {
      julkaisuvuosi = s;
      return julkaisuvuosi;
  }
  
  
  /**
   * @param s biisille laitettava kesto
   * @return null jos ok, muuten virhe
   */
  public String setKesto(String s) {
      kesto = s;
      return kesto;
  }

  
     /**
      * Testiohjelma biisille.
      * @param args ei käytössä
      */
     public static void main(String[] args) {
         Biisi song = new Biisi();
         song.vastaaCalling(2);
         song.tulosta(System.out);
     }
  
 }
 