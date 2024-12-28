package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.effect.ColorAdjust;
import javafx.stage.FileChooser;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.Initializable;

public class MainController implements Initializable {

    public boolean isBrightness = false;
    public boolean isContrast = false;
    public boolean isSaturated = false;
    public double percentage = 0;

    private Image originalImage;

    @FXML
    private ImageView imageHolder;
    @FXML
    private Button brightness;
    @FXML
    private Button contrast;
    @FXML
    private Button saturation;
    @FXML
    private Button save;
    @FXML
    private Slider slider;

    private ColorAdjust colorAdjust = new ColorAdjust();

    public void closeApplication(ActionEvent event) {
        System.exit(0); // Exits the application
    }

    public void openImage(ActionEvent event) {
        File imageFile = new FileChooser().showOpenDialog(null);
        if (imageFile != null) {
            originalImage = new Image(imageFile.toURI().toString());
            imageHolder.setImage(originalImage);  // Display original image
            imageHolder.setEffect(colorAdjust);   // Apply initial color effects
        }
    }

    public void increaseBrightNess(ActionEvent event) {
        isBrightness = true;
        isContrast = false;
        isSaturated = false;
        slider.setValue(percentage);
        updateImageEffect();
    }

    public void increaseContrast(ActionEvent event) {
        isBrightness = false;
        isContrast = true;
        isSaturated = false;
        slider.setValue(percentage);
        updateImageEffect();
    }

    public void increaseSaturation(ActionEvent event) {
        isBrightness = false;
        isContrast = false;
        isSaturated = true;
        slider.setValue(percentage);
        updateImageEffect();
    }

    private void updateImageEffect() {
        if (isBrightness) {
            colorAdjust.setBrightness(slider.getValue() / 100.0);
        } else if (isContrast) {
            colorAdjust.setContrast(slider.getValue() / 100.0);
        } else if (isSaturated) {
            colorAdjust.setSaturation(slider.getValue() / 100.0);
        }
        imageHolder.setEffect(colorAdjust);
    }

    public void increaseValue(DragEvent event) {
        System.out.println("Slider value changed");
        updateImageEffect();
    }

    // Save the modified image without resizing it
    public void saveImage(ActionEvent event) {
        // Ensure we are working with the original image dimensions, not the scaled view
        if (originalImage != null) {
            double width = 400.0;
            double height = 400.0;

            // Create a WritableImage using the exact dimensions of the original image
            WritableImage writableImage = new WritableImage((int) width, (int) height);

            // Snapshot the original image with the effects applied
            imageHolder.snapshot(null, writableImage);

            // Convert the WritableImage to BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);

            // Choose the file location to save the image
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                try {
                    // Save the image to the selected file path
                    ImageIO.write(bufferedImage, "PNG", file);
                    System.out.println("Image saved successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error saving image.");
                }
            }
        } else {
            System.out.println("No image loaded to save.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        slider.setValue(percentage);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Slider value is: " + newValue);
            updateImageEffect();
        });
    }
}
