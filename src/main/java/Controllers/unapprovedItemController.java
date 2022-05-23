package Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class unapprovedItemController extends ListView<String> implements Initializable
{
    private static Parent root;
    private static Stage stage;
    private static Scene scene;

    @FXML
    ListView<String> myListView4;

    String[] items=getNames();
    String[] split;
    String currentItem;

    public String[] getNames()
    {
        String[] names=new String[64];
        int i=0;

        try
        {
            File file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\unapproved_items.txt");
            Scanner reader = new Scanner(file);

            while(reader.hasNextLine()) {
                String data = reader.nextLine();

                String[] tok = data.split(",");
                names[i++]=tok[0]+","+tok[1]+","+tok[2];
            }
        }catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return names;
    }

    public void approveItem(javafx.event.ActionEvent event) throws IOException {
        if (currentItem!=null) {
            String toMove = split[0] + "," + split[1] + "," + split[2];
            String[] del = new String[64];

            File file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\unapproved_items.txt");
            Scanner reader = new Scanner(file);

            int i = 0;

            int ok = 1;
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                if (data.equals(toMove) == false || ok == 0) {
                    del[i++] = data;
                } else if (data.equals(toMove) == true) {
                    ok = 0;
                }
            }

            FileWriter writer = new FileWriter(file);
            for (String aux : del) {
                if (aux != null) {
                    writer.write(aux + "\n");
                }
            }
            writer.close();

            file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\all_items.txt");

            writer = new FileWriter(file, true);
            writer.write(toMove + "\n");
            writer.close();

            switchToUnapprovedItemScreen(event);
        }
    }

    public void rejectItem(javafx.event.ActionEvent event) throws IOException {
        if (currentItem!=null) {
            String toMove = split[0] + "," + split[1] + "," + split[2];
            String[] del = new String[64];

            File file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\unapproved_items.txt");
            Scanner reader = new Scanner(file);

            int ok = 1, i = 0;
            while (reader.hasNextLine()) {
                String data = reader.nextLine();

                if (data.equals(toMove) == false || ok == 0) {
                    del[i++] = data;
                } else if (data.equals(toMove) == true) {
                    ok = 0;
                }
            }

            FileWriter writer = new FileWriter(file);
            for (String aux : del) {
                if (aux != null) {
                    writer.write(aux + "\n");
                }
            }
            writer.close();

            String[] name = split[2].split("_");

            file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\" + name[0] + "_items.txt");
            reader = new Scanner(file);

            String[] del2 = new String[64];

            ok = 1;
            i = 0;

            while (reader.hasNextLine()) {
                String data = reader.nextLine();

                if (data.equals(toMove) == false || ok == 0) {
                    del2[i++] = data;
                } else if (data.equals(toMove) == true) {
                    ok = 0;
                }
            }

            writer = new FileWriter(file);
            for (String aux : del2) {
                if (aux != null) {
                    writer.write(aux + "\n");
                }
            }
            writer.close();
            Path imagesPath = Paths.get(System.getProperty("user.dir") + "\\photos\\" + split[2]);

            try {
                Files.delete(imagesPath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            switchToUnapprovedItemScreen(event);
        }
    }

    class Cell extends ListCell<String>
    {
        HBox hbox = new HBox();
        //Button btn = new Button("Hei");

        ImageView img = new ImageView();

        Label label = new Label("");
        Pane pane = new Pane();

        public Cell()
        {
            super();

            hbox.getChildren().addAll(img, label, pane);
            hbox.setHgrow(pane, Priority.ALWAYS);
            //btn.setOnAction(e -> switchScene(e));
        }

        /*public void switchScene(javafx.event.ActionEvent event)
        {
            try {
                switchToLogInScreen(event);
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }*/

        public void updateItem(String name, boolean empty)
        {
            URL url = null;

            super.updateItem(name, empty);
            setText(null);
            setGraphic(null);

            if(name != null && !empty)
            {
                //.out.println(name+" :)");
                String[] toks=name.split(",");

                label.setText(name);
                //System.out.println("."+toks[0]+".");

                try {
                    url= new File(System.getProperty("user.dir") + "\\photos\\"+toks[2]).toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                javafx.scene.image.Image profile = new Image(url.toString(), 40, 40, false, false);
                img.setImage(profile);

                label.setText(toks[0]+","+toks[1]);
                setGraphic(hbox);
            }
        }
    }

    public void initialize(URL arg0, ResourceBundle arg1)
    {
        myListView4.getItems().addAll(items);

        myListView4.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                currentItem = (myListView4.getSelectionModel().getSelectedItems()).toString();
                if(currentItem.equals("[null]")==false) {
                    split=currentItem.split(",");

                    split[0] = split[0].substring(1);
                    split[2] = split[2].substring(0, split[2].length() - 1);
                    //System.out.println(split[0]+"-"+split[1]+"-"+split[2]);
                }

                //System.out.println(currentItem);
            }
        });

        GridPane pane = new GridPane();
        Label name = new Label("h");
        //Button btn = new Button("Button");

        pane.add(name, 0, 0);
        //pane.add(btn, 0, 1);

        myListView4.setCellFactory(param -> new Cell());
    }

    public void switchToAddItemScreen(javafx.event.ActionEvent event) throws IOException
    {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/add-item-page.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLogInScreen(javafx.event.ActionEvent event) throws IOException
    {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/sample.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLoggedInScreen_admin(javafx.event.ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/logged-in_admin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void adminItems(javafx.event.ActionEvent event) throws IOException
    {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/admin-items.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToUnapprovedItemScreen(javafx.event.ActionEvent event) throws IOException
    {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/approveItems.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
