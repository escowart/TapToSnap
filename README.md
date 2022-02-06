# Lab49 Take-Home - Tap To Snap

Created by Edwin S. Cowart

Take photos of items mentioned on each tile to win the game!

[Design](Design.pdf)

[Instructions](Instructions.pdf)

## Setup

1. [Install Android Studio](https://developer.android.com/studio)
2. [Open .](.) as an Android Studio project
3. Goto Android Studio > Preferences > Edit > File and Code Template > File Header (Java), then update your file header



## OpenAPI

### Setup

1. [Install Java](https://java.com/en/download/)
2. Run to script install dependencies & re-generate the client from OpenAPI Spec
```
    >   ./openapi/generate.sh
```

### Update the OpenAPI Spec & re-generate client api, models, & network infrastructure

1. Update OpenAPI [openapi/specification] or [openapi/templates]
2. Run OpenAPI Generate
```
    >   ./openapi/generate.sh
```


### Update OpenAPI generate + dependencies

Run to Update script dependencies
```
    >   ./openapi/generate.sh update
```


## Python Scripts

### Setup

1. [Install Python3](https://www.python.org/downloads/)
2. [Install Pycharm](https://www.jetbrains.com/pycharm/download/#section=mac)
3. [Open scripts](scripts) as a project in Pycharm to avoid collisions with Android Studio


### [Import Images](scripts/import-images.py)

Import images with multiple in the Android project's resource.
Script expects images as camcelCased PNGs with a size multiplier.

Examples:

* sourceDirectory
    * imageName.png
    * imageName@2x.png
    * imageName@3x.png


Usage:
```
    >   python3 ./scripts/input-images.py [Source Directory]
```

