package com.preibisch.bdvtransform.panels.utils.tansformation;

import net.imglib2.realtransform.AffineTransform3D;

import java.util.Map;
import java.util.TreeMap;


public class ImageTransformations {

    TreeMap<Integer, ImageTransformation> transformations = new TreeMap<>();
    AffineTransform3D currentTransformation;
    int currentPosition = 0;

    public ImageTransformations() {
        this(new AffineTransform3D());
    }

    public ImageTransformations(AffineTransform3D transform) {
        currentTransformation = transform.copy();
        transformations.put(0, new ImageTransformation(transform, TransformationType.Automatic));
    }

    public AffineTransform3D add(AffineTransform3D transform) {
        return add(transform, TransformationType.Automatic);
    }

    public AffineTransform3D addManual(AffineTransform3D transform) {
        return add(transform, TransformationType.Manual);
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    private AffineTransform3D add(AffineTransform3D transform, TransformationType type) {
        removeAfterPosition(currentPosition);
        Integer position = transformations.lastKey() + 1;
        transformations.put(position, new ImageTransformation(transform, type));
        currentPosition++;
//        if (type.equals(TransformationType.Automatic))
            currentTransformation.preConcatenate(transform);
//        else
//            currentTransformation = transform.copy();
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

    public Map.Entry<Integer, ImageTransformation> getLast(){
       return transformations.lastEntry();
    }

    private AffineTransform3D recalculateTransformation() {
        int i = 1;
        currentTransformation = transformations.firstEntry().getValue().getTransformation().copy();
        print();
        for (Integer key : transformations.keySet()){
            if (i>currentPosition) break;
            if(key.equals(transformations.firstKey())){
                continue;
            }else{
//                add(transformations.get(key));
                ImageTransformation transform = transformations.get(key);
////                if (transform.getType().equals(TransformationType.Automatic))
                    currentTransformation.concatenate(transform.getTransformation());
//                else
//                    currentTransformation = transform.getTransformation().copy();
            }
            i++;
        }
        print();
        return currentTransformation;
    }

    private void add(ImageTransformation imageTransformation) {
        add(imageTransformation.getTransformation(),imageTransformation.getType());
    }

    private void removeAfterPosition(int position) {
        if (position < transformations.size() - 1)
            while (position < transformations.size() - 1)
                transformations.remove(transformations.lastKey());
    }

    private void print() {
        System.out.println("Transformation: ");
        MatrixOperation.print(MatrixOperation.toMatrix(currentTransformation.getRowPackedCopy(), 4));
    }

    public boolean canUndo() {
        return currentPosition > 0;
    }

    public TreeMap<Integer, ImageTransformation> getAll() {
        return transformations;
    }

    public AffineTransform3D removeAt(int i) {
        if (!transformations.containsKey(i))
            return null;
        transformations.remove(i);
        currentPosition--;
        return recalculateTransformation();
    }

    public int size() {
        return transformations.size();
    }

    public boolean canRedo() {
        return (currentPosition < transformations.size() - 2);
    }
}
