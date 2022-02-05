# Lab49 Take-Home - Tap To Snap

Created by Edwin S. Cowart

Take photos of items mentioned on each tile to win the game!

[Design](Design.pdf)
[Instructions](Instructions.pdf)


Recommended IDE: [Android Studio](https://developer.android.com/studio)


## [Import Images](scripts/import-images.py)

Import images with multiple in the Android project's resource.
Script expects images as camcelCased PNGs with a size multiplier.

### Examples

    - imageName.png
    - imageName@2x.png
    - imageName@3x.png


### Usage

```
    >   python3 ./scripts/input-images.py [Source Directory]
```

### Additional Information

Recommended Python IDE: [Pycharm](https://www.jetbrains.com/pycharm/download/#section=mac)
Open ./scripts as a project in Pycharm to avoid collisions with . Android Studio project

