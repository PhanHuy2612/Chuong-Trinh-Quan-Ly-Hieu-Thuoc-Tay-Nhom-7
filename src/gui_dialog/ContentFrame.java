package gui_dialog;

import javafx.scene.Node;
import javafx.scene.layout.*;


public class ContentFrame extends HBox {
    public ContentFrame(Node mainContent, VBox rightSidebar) {
        setStyle("-fx-background-color: #F8FAFC;");
        setSpacing(0);

        HBox.setHgrow(mainContent, Priority.ALWAYS);
        getChildren().add(mainContent);

        if (rightSidebar != null) {
            rightSidebar.setPrefWidth(380);
            rightSidebar.setStyle("-fx-background-color: #F8F9FA; -fx-padding: 20;");
            getChildren().add(rightSidebar);
        }
    }

    public ContentFrame(Node mainContent) {
        this(mainContent, null);
    }
}