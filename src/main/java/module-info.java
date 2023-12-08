module klemmr02.spelling_bee_honors_project {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;

    opens klemmr02.spelling_bee_honors_project to javafx.fxml;
    exports klemmr02.spelling_bee_honors_project;
}
