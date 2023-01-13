module tju.ds.map {
    requires javafx.controls;
    requires javafx.fxml;
    requires AnimateFX;

    requires java.desktop;
    requires lombok;
    requires mongo.java.driver;
    requires org.slf4j;
    requires org.slf4j.simple;

    opens tju.ds.map to javafx.fxml;
    exports tju.ds.map;
}