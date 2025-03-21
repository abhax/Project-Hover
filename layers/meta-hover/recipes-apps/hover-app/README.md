# HoverApp Recipe

This recipe builds the HoverApp application for the HoverCraft platform.

## Description

HoverApp is a control application for the hovercraft platform with Bluetooth SPP support.

## Source Location

The recipe expects the HoverApp source code to be located at the same directory level as Project-Hover:

```
/path/to/
├── ADSEN86/
    ├── Source/
        ├── Project-Hover/
        ├── hoverApp/
```

If your source code is in a different location, you can customize it by setting the `HOVER_APP_SRC_DIR` variable in your `local.conf`:

```
HOVER_APP_SRC_DIR = "/path/to/your/hoverApp/source"
```

## Building

You can build just the HoverApp:

```bash
bitbake hover-app
```

Or include it in the complete hover-craft-basic image:

```bash
bitbake hover-craft-basic
```

## Dependencies

The recipe automatically includes the necessary dependencies:
- bluez5
- libdrm
- libgbm
- OpenGL ES

## Running

Once deployed to the target, you can run the application:

```bash
hoverApp
``` 