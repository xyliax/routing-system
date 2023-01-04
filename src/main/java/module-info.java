module tju.ds.map {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens tju.ds.map to javafx.fxml;
    exports tju.ds.map;
}