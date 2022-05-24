package Controllers;

import java.net.MalformedURLException;
import java.nio.file.*;
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
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import static Controllers.Controller.getUsername;
import static Controllers.Controller.username;

public class userItemController implements Initializable
{
    @FXML
    ListView<String> myListView2;

    String[] split;

    String currentItem;

    private static Parent root;
    private static Stage stage;
    private static Scene scene;

    String[] items=getNames(System.getProperty("user.dir") + "\\src\\main\\resources\\"+username+"_items.txt");
    String[] allItems=getNames(System.getProperty("user.dir") + "\\src\\main\\resources\\all_items.txt");

    public String[] getNames(String path)
    {
        String[] names=new String[64];
        int i=0;

        String username=getUsername();

        try
        {
            File file = new File(path);
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

    static class Cell extends ListCell<String>
    {
        HBox hbox = new HBox();
        //Button btn = new Button("del");
        Label label = new Label("");

        //Image profile = new Image("photos/icon.jpg", 40, 40, false, false);
        ImageView img = new ImageView();
        //img.setFitHeight(10);
        Pane pane = new Pane();

        public Cell()
        {
            super();

            hbox.getChildren().addAll(img, label, pane);// ,btn);
            hbox.setHgrow(pane, Priority.ALWAYS);

        }

        public void updateItem(String name, boolean empty)
        {
            URL url = null;

            super.updateItem(name, empty);
            setText(null);
            setGraphic(null);

            if(name != null && !empty)
            {
                String[] toks=name.split(",");

                label.setText(name);
                //System.out.println("."+toks[0]+".");

                try {
                    url= new File(System.getProperty("user.dir") + "\\photos\\"+username+"_"+toks[0]+".jpg").toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                Image profile = new Image(url.toString(), 40, 40, false, false);
                img.setImage(profile);
                setGraphic(hbox);
            }
        }
    }

    public void initialize(URL arg0, ResourceBundle arg1) {

        int i=0;
        String[] listItems = new String[64];

        for(String aux : items)
        {
            if(aux!=null)
            {
                String[] tok=aux.split(",");
                listItems[i++]=tok[0]+","+tok[1];
            }
        }

        myListView2.getItems().addAll(listItems);

        myListView2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                currentItem = (myListView2.getSelectionModel().getSelectedItems()).toString();

                if(currentItem.equals("[null]")==false) {
                    //System.out.println(currentItem);
                    split = currentItem.split(",");
                    split[0] = split[0].substring(1);
                    split[1] = split[1].substring(0, split[1].length() - 1);
                }
            }
        });

        GridPane pane = new GridPane();
        Label name = new Label("h");
        //Button btn = new Button("Button");
        pane.add(name, 0, 0);
        //pane.add(btn, 0, 1);

        myListView2.setCellFactory(param -> new Cell());
    }

    public void deleteItem(javafx.event.ActionEvent event) {
        int i = 0;
        String[] newItems = new String[64];
        String[] newAllItems=new String[64];

        if (currentItem!=null) {
            String username = getUsername();
            for (String aux : items) {
                if (aux != null) {
                    String[] tok = aux.split(",");

                    if (tok[0].equals(split[0]) == false) {
                        newItems[i++] = tok[0] + "," + tok[1] + "," + tok[2]+"\n";//username + "_" + tok[0] + ".jpg\n";
                    } else {
                        Path imagesPath = Paths.get(System.getProperty("user.dir") + "\\photos\\"+tok[2]);// + username + "_" + tok[0] + ".jpg");

                        try {
                            Files.delete(imagesPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            i = 0;

            int ok=0;
            String toDel=split[0]+","+split[1];
            //de aici trebuie sa schimb
            for(String aux : allItems)
            {
                if(aux!=null)
                {
                    if(aux.contains(toDel))
                    {
                        ok=1;
                    }
                }
            }

            if(ok==1) {
                for (String aux : allItems) {
                    if (aux != null) {
                        String[] tok = aux.split(",");

                        if (tok[0].equals(split[0]) == false) {
                            newAllItems[i++] = tok[0] + "," + tok[1] + "," + tok[2] + "\n";
                        }
                    }
                }
            }

            else
            {
                allItems=getNames(System.getProperty("user.dir") + "\\src\\main\\resources\\unapproved_items.txt");
                for (String aux : allItems) {
                    if (aux != null) {
                        String[] tok = aux.split(",");

                        if (tok[0].equals(split[0]) == false) {
                            newAllItems[i++] = tok[0] + "," + tok[1] + "," + tok[2] + "\n";
                        }
                    }
                }
            }

            try {
                File file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\" + username + "_items.txt");
                FileWriter writer = new FileWriter(file);

                for (String aux : newItems) {
                    if (aux != null) {
                        writer.write(aux);
                    }
                }
                writer.close();

                File file2;

                if(ok==1) {
                    file2 = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\all_items.txt");
                }

                else
                {
                    file2 = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\unapproved_items.txt");
                }
                FileWriter writer2 = new FileWriter(file2);

                for (String aux : newAllItems) {
                    if (aux != null) {
                        writer2.write(aux);
                    }
                }
                writer2.close();
                userItems(event);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public void switchToLoggedInScreen_user(javafx.event.ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/logged-in_user.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void userItems(javafx.event.ActionEvent event) throws IOException
    {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/user-items.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}