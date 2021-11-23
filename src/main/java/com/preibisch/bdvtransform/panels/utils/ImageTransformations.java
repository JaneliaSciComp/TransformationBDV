package com.preibisch.bdvtransform.panels.utils;

import net.imglib2.realtransform.AffineTransform3D;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ImageTransformations {
    enum TransformationType {
        Manual, Automatic
    }

    List<Map.Entry<AffineTransform3D, TransformationType>> transformations = new ArrayList<>();
    AffineTransform3D currentTransformation;
    int currentPosition = 0;

    public ImageTransformations() {
        this(new AffineTransform3D());
    }

    public ImageTransformations(AffineTransform3D transform) {
        currentTransformation = transform;
        transformations.add(new AbstractMap.SimpleEntry<>(transform, TransformationType.Automatic));
    }

    public AffineTransform3D add(AffineTransform3D transform) {
        return add(transform, TransformationType.Automatic);
    }

    public AffineTransform3D addManual(AffineTransform3D transform) {
        return add(transform, TransformationType.Manual);
    }

    private AffineTransform3D add(AffineTransform3D transform, TransformationType type) {
        removeAfterPosition(currentPosition);
        transformations.add(new AbstractMap.SimpleEntry<>(transform, TransformationType.Manual));
        currentPosition++;
        if (type.equals(TransformationType.Automatic))
            currentTransformation.concatenate(transform);
        else
            currentTransformation = transform.copy();
        print();
        return get();
    }


    public AffineTransform3D undo() {
        if (currentPosition > 0)
            currentPosition--;
        return recalculateTransformation();
    }


    public AffineTransform3D redo() {
        if (currentPosition < transformations.size() - 2)
            currentPosition++;
        return recalculateTransformation();
    }

    public AffineTransform3D get() {
        return currentTransformation;
    }

    private AffineTransform3D recalculateTransformation() {
        currentTransformation = transformations.get(0).getKey().copy();
        for (int i = 1; i <= currentPosition; i++) {
            Map.Entry<AffineTransform3D, TransformationType> transform = transformations.get(i);
            if (transform.getValue().equals(TransformationType.Automatic))
                currentTransformation.concatenate(transform.getKey());
            else
                currentTransformation = transform.getKey().copy();
        }
        print();
        return currentTransformation;
    }

    private void removeAfterPosition(int position) {
        if (position < transformations.size() - 1)
            while (position < transformations.size() - 1)
                transformations.remove(transformations.size() - 1);
    }

    private void print() {
        System.out.println("Transformation: ");
        MatrixOperation.print(MatrixOperation.toMatrix(currentTransformation.getRowPackedCopy(), 4));
    }

    public boolean canUndo() {
        return currentPosition > 0;
    }

    public List<Map.Entry<AffineTransform3D, TransformationType>> getAll() {
        return transformations;
    }

    public AffineTransform3D removeAt(int i) {
        if (i >= transformations.size())
            return null;
        transformations.remove(i);
        return recalculateTransformation();
    }
}
