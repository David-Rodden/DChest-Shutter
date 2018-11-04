import javax.swing.*;
import java.awt.*;

public class FrequencyForm extends JFrame {
    private JPanel panel1;
    private JSlider tauntFrequencySlider;
    private JLabel frequencyLabel;
    private JCheckBox enableTauntsCheckBox;
    private JButton startButton;
    private boolean commenced, tauntEnabled;

    FrequencyForm(final Shutter shutter) {
        tauntFrequencySlider.addChangeListener(e -> {
            final int frequency = tauntFrequencySlider.getValue();
            frequencyLabel.setText("Message frequency: " + (frequency == 10 ? "Very Quick" : frequency < 40 ? "Quick" : frequency == 100 ? "Very Slow" : frequency > 70 ? "Slow" : "Normal"));
            shutter.setFrequency(frequency);
        });
        enableTauntsCheckBox.addChangeListener(e -> {
            tauntEnabled = enableTauntsCheckBox.isSelected();
            tauntFrequencySlider.setEnabled(tauntEnabled);
            frequencyLabel.setEnabled(tauntEnabled);
        });
        startButton.addActionListener(e -> {
            commenced = true;
            setVisible(false);
        });
        add(panel1);
        panel1.setBackground(Color.WHITE);
        shutter.setFrequency(tauntFrequencySlider.getValue());
        commenced = false;
        setSize(300, 300);
        setVisible(true);
    }

    boolean isTauntEnabled() {
        return tauntEnabled;
    }

    boolean hasCommenced() {
        return commenced;
    }
}
