# Lab49 Take-Home - Tap To Snap

Created by Edwin S. Cowart

Take photos of items mentioned on each tile to win the game!

[Design](Design.pdf)
[Instructions](Instructions.pdf)

## Setup

1. [Android Studio](https://developer.android.com/studio)


## Scripts

### Setup

1. Install [Python3](https://www.python.org/downloads/)
2. Install [Pycharm](https://www.jetbrains.com/pycharm/download/#section=mac)
3. Open [scripts](scripts) as a project in Pycharm to avoid collisions with Android Studio


### [Import Images](scripts/import-images.py)

Import images with multiple in the Android project's resource.
Script expects images as camcelCased PNGs with a size multiplier.

Usage:
```
    >   python3 ./scripts/input-images.py [Source Directory]
```


Examples:

    - imageName.png
    - imageName@2x.png
    - imageName@3x.png
