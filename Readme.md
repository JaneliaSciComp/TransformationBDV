# Goal:

Interactive tool for image transformation
![Application](img/app.png?raw=true "Application")

The goal of the application is help user to get the perfect transformation matrix in an interactive way.

## How to use:
### Shortcuts:
`Q` Auto Brightness

`R` Random colors

`ctrl Z` undo the last step, you can keep doing it, as all steps are recorded.

`ctrl U` Redo the steps you canceled.

### Automatic Transformations:
- **Translation:** Translate image in X,Y,Z
- **Rotation:** Rotate Image in X,Y,Z
- **Scale:** Scale image, you can select `same scale` to scale the same for 3 axis or just scale in one axis
- **Flip:** Flip the image in the selected axis X,Y,Z
- **Permute axis:** Change the order of axis default (X:0, Y:1 , Z:2 )

### Manual Transformation:
You can add manual transformation, and it will be recorded in a new transformation matrix.

Press `T` to start recording and `T` again to end manual transformation.


## History :
Each Transformation is recorded in a new Transformation matrix. 

All the history can be visualised in `History` panel. 

You can delete any step by `-` button

Using :
- BigDataViewer https://github.com/bigdataviewer/bigdataviewer-core
- Elastix https://github.com/SuperElastix/elastix
- Template-Building https://github.com/saalfeldlab/template-building

