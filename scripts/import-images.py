#!/usr/bin/env python3
"""
Created by Edwin S. Cowart on 05 February, 2022
Lab49 Take-Home
Tap To Snap
"""

import sys
import os
import re
import math
import shutil
from typing import Optional

supported_file_extensions = [".png"]
size_multiplier_regex = r'@([\d\.]+)x'
size_multiplier_to_android_size = {
    0.75: 'ldpi',
    1: 'mdpi',
    1.5: 'hdpi',
    2: 'xhdpi',
    3: 'xxhdpi',
    4: 'xxxhdpi'
}
file_abs_path = os.path.abspath(__file__)
directory_abs_path = os.path.dirname(file_abs_path)
android_resource_directory = f"{directory_abs_path}/../app/src/main/res/"


def is_image_file(filepath: str) -> bool:
    return (
            os.path.isfile(filepath) and
            any(filepath.endswith(ext) for ext in supported_file_extensions)
    )


def get_destination_directory(source_filename: str) -> Optional[str]:
    match = re.search(size_multiplier_regex, source_filename)
    size_multiplier = (
        float(match.groups()[0])
        if match and len(match.groups()) == 1
        else 1
    )
    for size_multi, size_android in size_multiplier_to_android_size.items():
        # Account for float point inaccuracy
        if math.isclose(size_multi, size_multiplier):
            return f"drawable-{size_android}"

    return None


def get_destination_filename(source_filename: str) -> str:
    # remove multiplier
    filename = re.sub(size_multiplier_regex, "", source_filename)
    # camelCase -> snake_case
    filename = re.sub(r'(?<!^)(?=[A-Z])', '_', filename).lower()
    return filename


def process_image_files(source_directory: str):
    print(f"Import Images\n")
    count = 0
    for source_filename in os.listdir(source_directory):
        source_filepath = os.path.join(source_directory, source_filename)
        if not is_image_file(source_filepath):
            continue

        destination_directory = get_destination_directory(source_filename)
        if destination_directory is None:
            continue

        if not os.path.exists(destination_directory):
            os.makedirs(destination_directory)

        destination_filename = get_destination_filename(source_filename)
        destination = f"{android_resource_directory}/{destination_directory}/{destination_filename}"
        shutil.copy(source_filepath, destination)
        count += 1
        print(f"\t{source_filename} -> {destination_directory}/{destination_filename}")

    print(f"\nDone! Successfully imported {count} image{'s' if count else ''}!\n")


if __name__ == "__main__":
    if len(sys.argv) != 2:
        sys.exit(f"Usage: import-images [Source Directory]")
    # avoid namespace conflicts
    process_image_files(sys.argv[1])
