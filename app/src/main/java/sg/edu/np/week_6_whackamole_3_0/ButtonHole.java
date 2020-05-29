package sg.edu.np.week_6_whackamole_3_0;

import android.widget.Button;

public class ButtonHole {
    private Boolean hasMole;
    private Button linkedButton;

    public ButtonHole(Button b) {
        this.hasMole = false;
        this.linkedButton = b;
    }

    public Boolean getHasMole() {
        return hasMole;
    }

    public void setHasMole(Boolean value) {
        this.hasMole = value;
        UpdateLinkedButtonToReflectModel();
    }

    public void SpawnMole() {
        setHasMole(true);
    }

    public void UpdateLinkedButtonToReflectModel() {
        if (hasMole) {
            linkedButton.setText("*");
        } else {
            linkedButton.setText("O");
        }
    }
}
