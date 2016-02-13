package xyz.pongsakorn.policeeye.model;

import xyz.pongsakorn.policeeye.activity.IdentikitActivity;

/**
 * Created by Porpeeranut on 13/2/2559.
 */
public class FacialCompositeListModel {

    public IdentikitActivity.FacialComposite facialComposite;
    public int selectedStylePos = 0;

    public FacialCompositeListModel(IdentikitActivity.FacialComposite facialComposite) {
        this.facialComposite = facialComposite;
    }

    public void setSelectedStylePos(int pos) {
        selectedStylePos = pos;
    }
}
