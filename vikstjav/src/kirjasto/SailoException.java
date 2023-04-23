package kirjasto;

    /**
    * Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille.
    * @author Jaakko Vikström  
    * @version 1.0, 21.04.2019
    */
   public class SailoException extends Exception {
       private static final long serialVersionUID = 1L;
  
  
      /**
       * Poikkeuksen muodostaja jolle tuodaan poikkeuksessa
       * käytettävä viesti
       * @param viesti Poikkeuksen viesti
       */
      public SailoException(String viesti) {
          super(viesti);
    }
  }