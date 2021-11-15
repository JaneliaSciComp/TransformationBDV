package com.preibisch.bdvtransform.panels;

import com.preibisch.bdvtransform.panels.utils.MatrixOperation;
import com.preibisch.bdvtransform.panels.utils.TransformationUpdater;
import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UndoPanel extends BDVCardPanel implements ActionListener {
    private final TransformationUpdater updater;
    private int currentSource;
    private final JButton undoButton;
    private final JButton redoButton;
    private final JButton resetButton;
    private final List<AffineTransform3D> initialTransformations;
    private List<List<AffineTransform3D>> allTransforms;
    List<Integer> positions;


    public UndoPanel(List<AffineTransform3D> transforms, int currentSource,TransformationUpdater updater) {
        super("UndoPanel", "Export Transformation", new GridLayout(0, 1));
        initTransforms(transforms);
        initialTransformations = transforms;
        this.currentSource = currentSource;
        this.updater = updater;
        this.undoButton = new JButton("Undo");
        this.redoButton = new JButton("Redo");
        this.resetButton = new JButton("Reset");
        undoButton.addActionListener(this);
        resetButton.addActionListener(this);
        redoButton.addActionListener(this);
        this.add(undoButton);
        this.add(redoButton);
        this.add(resetButton);
    }

    private void initTransforms(List<AffineTransform3D> transforms) {
        this.allTransforms =  new ArrayList<>();
        this.positions =  new ArrayList<>();
        for (AffineTransform3D transformation :
                transforms) {
            List<AffineTransform3D> currentTransformations = new ArrayList<>();
            currentTransformations.add(transformation);
            allTransforms.add(currentTransformations);
            positions.add(0);
        }
    }

    @Override
    public void setSource(int sourceId) {
        this.currentSource = sourceId;
    }

    @Override
    public void onNotify(AffineTransform3D transform) {

    }

    public void addTransformation(AffineTransform3D transform, int sourceID) {
        this.allTransforms.get(sourceID).add(transform);
        this.positions.set(sourceID,this.positions.get(sourceID)+1);
        System.out.println("Add transfrmation to source: "+sourceID);
        MatrixOperation.print(MatrixOperation.toMatrix(transform.getRowPackedCopy(),4));
        System.out.println("Source: "+sourceID+ " has "+positions.get(sourceID)+ "matrix");
        for(AffineTransform3D t : allTransforms.get(sourceID)){
            MatrixOperation.print(MatrixOperation.toMatrix(t.getRowPackedCopy(),4));
            System.out.println();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
       if (e.getSource().equals(undoButton)){
           if(positions.get(currentSource)>0)
           {
               positions.set(currentSource,positions.get(currentSource)-1);
               AffineTransform3D currentTransformation = allTransforms.get(currentSource).get(positions.get(currentSource));
               updater.setTransformation(currentTransformation,this);
           }
       }else if(e.getSource().equals(redoButton)){
           if(positions.get(currentSource)<allTransforms.get(currentSource).size()){
               positions.set(currentSource,positions.get(currentSource)+1);
               AffineTransform3D currentTransformation = allTransforms.get(currentSource).get(positions.get(currentSource));
               updater.setTransformation(currentTransformation,this);
           }

       }else if(e.getSource().equals(resetButton)){
           initTransforms(initialTransformations);
           updater.setAllTransformation(initialTransformations,this);
       }else{
           System.out.println("Listener not implemented !");
       }
    }
}