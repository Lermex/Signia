package uk.ac.uel.signia.ui;

public abstract class ParentAware {
    MainViewController parentController;

    public void setParentController(MainViewController parentController) {
        this.parentController = parentController;
    }
}
