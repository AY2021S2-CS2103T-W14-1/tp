package seedu.address.ui;

import static seedu.address.model.group.GroupHashMap.DEFAULT_GROUP_NAME;

import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.group.Group;
import seedu.address.model.person.Name;

public class GroupListPanel extends UiPart<Region> {
    private static final String FXML = "GroupListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(GroupListPanel.class);

    @FXML
    private ListView<Name> groupListView;
    /**
     * Creates a {@code GroupListPanel} with the given {@code ObservableMap}.
     */
    public GroupListPanel(ObservableMap<Name, Group> groupMap) {
        super(FXML);

        Platform.runLater(() -> {
            groupListView.getSelectionModel().select(DEFAULT_GROUP_NAME);
        });

        //adapted from https://docs.oracle.com/javafx/2/events/filters.htm Example 3-1
        //Prevent mouse selection
        groupListView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
            }
        });

        //adapted from https://stackoverflow.com/a/29403453
        groupListView.getItems().addAll(groupMap.keySet());
        groupMap.addListener((MapChangeListener<Name, Group>) change -> {
            if (change.wasAdded()) {
                groupListView.getItems().removeAll(change.getKey());
                groupListView.getItems().add(change.getKey());
            }
            if (change.wasRemoved()) {
                groupListView.getItems().removeAll(change.getKey());
            }
            Platform.runLater(() -> {
                groupListView.getSelectionModel().select(change.getKey());
                groupListView.scrollTo(change.getKey());
            });
        });

        groupListView.setCellFactory(listView -> new GroupListPanel.GroupListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a Group {@code Name}.
     */
    class GroupListViewCell extends ListCell<Name> {
        @Override
        protected void updateItem(Name groupName, boolean empty) {
            super.updateItem(groupName, empty);
            this.setMaxWidth(groupListView.getWidth());
            if (empty || groupName == null) {
                setGraphic(null);
                setText(null);
            } else {
                Label label = new Label(groupName.fullName);
                label.setPadding(new Insets(10));
                label.setMaxWidth(groupListView.getWidth());
                label.setPrefWidth(groupListView.getWidth());
                label.setWrapText(true);
                setGraphic(label);
            }
        }
    }
}
