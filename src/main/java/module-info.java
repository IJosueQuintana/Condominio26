module fis.dsw.sgc {
    // Módulos JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome;

    // Otros módulos comunes
    requires java.sql;
    requires java.desktop;
    requires javafx.base;
    requires jbcrypt;


    // Abre los paquetes con controladores para FXML
    opens fis.dsw.sgc.administracion.controller to javafx.fxml;
    opens fis.dsw.sgc.administracion.dashboard to javafx.fxml;
    opens fis.dsw.sgc.check_in.controller to javafx.fxml, javafx.base;
    opens fis.dsw.sgc.finanzas.controller to javafx.fxml, javafx.base;
    opens fis.dsw.sgc.comunicacion.controller to javafx.fxml, javafx.base;
    opens fis.dsw.sgc.inmuebles.controller to javafx.fxml;
    opens fis.dsw.sgc.inmuebles.dto to javafx.base;
    opens fis.dsw.sgc.reservas.controller to javafx.fxml, javafx.base;
    opens fis.dsw.sgc.reservas.model to javafx.base;
//    opens fis.dsw.sgc.administracion.dto to javafx.base;
    opens fis.dsw.sgc.check_in.dto to javafx.base;
    opens fis.dsw.sgc.finanzas.dto to javafx.base;
//    opens fis.dsw.sgc.comunicacion.dto to javafx.base;
    opens fis.dsw.sgc.reservas.dto to javafx.base;
    exports fis.dsw.sgc.app;


}
