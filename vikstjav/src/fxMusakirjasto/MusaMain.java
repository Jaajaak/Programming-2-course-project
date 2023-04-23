package fxMusakirjasto;
	
import javafx.application.Application;


import javafx.application.Platform;
import javafx.stage.Stage;
import kirjasto.Kirjasto;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author vikstjav
 * @version 7.2.2019
 */
public class MusaMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
		    final FXMLLoader ldr = new FXMLLoader(getClass().getResource("MusakirjastoGUIView.fxml"));
		    final Pane root = (Pane)ldr.load();
		    final MusakirjastoGUIController musaCtrl = (fxMusakirjasto.MusakirjastoGUIController)ldr.getController();
		    
		    final Scene scene = new Scene(root);
		    scene.getStylesheets().add(getClass().getResource("Musa.css").toExternalForm());
		    primaryStage.setScene(scene);
		    primaryStage.setTitle("Kirjasto");
			
		    primaryStage.setOnCloseRequest((event) -> {
		        if ( !musaCtrl.voikoSulkea() ) event.consume(); 
		    });
			Kirjasto kirjasto = new Kirjasto();
			musaCtrl.setKirjasto(kirjasto);
			
			primaryStage.show();
			
			Application.Parameters params = getParameters(); 
            if ( params.getRaw().size() > 0 ) 
                musaCtrl.lueTiedosto(params.getRaw().get(0));  
            else
                if ( !musaCtrl.avaa() ) Platform.exit();

  
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * käynnistetään käyttöliittymä
	 * @param args komentorivin parametrit
	 */
	public static void main(String[] args) {
		launch(args);
	}
}