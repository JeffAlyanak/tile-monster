Tile Monster
=============

Retro Console Tile Editor
--------------------------

Tile Monster is a fork of Ken Hanson's original TM brought ever-so-slightly into the modern era. Much of its interface will feel feel familiar to users of YY-CHR, MSPaint or tile tools, but Tile Monster is a much more powerful animal!

Tile Monster is a visual explorer of binary videogame data aimed at displaying and editing graphics content for editing and export. It supports many common platform formats out of the box but its design is platform-independant and employs relatively simple, XML-defined definitions for new graphics formats, file formats and palette formats. These "graphics codec" definitions can be linked to file extensions in order to automatically switch to the graphics mode associated with that extension.

Detailed instructions on can be found in the [Manual](manual/Manual.md).

Building & Running
--------

While this has only been tested with `gradle 4.4` it should be simple enough to build on most versions of `gradle`.

Simply running `gradle build` will build the `jar` in the `build/libs/` directory.

It can then be run with `java -jar tilemonster-1.jar`. _Tile Monster requires Java Runtime Environment (JRE) version 1.4.0 or greater._

Features
---------

Platform support:
* Famicom/NES
* SuFami/SNES
* GameBoy
* GameBoy Advance
* Sega Master System
* Sega Genesis/MegaDrive/32X
* Nintendo 64
* WonderSwan
* NeoGeo Pocket
* and more...you can even add your own!

Import/Export data in a variety of formats:
* JPG
* PNG
* GIF
* BMP
* PCX

Tons of useful features:
* Simple, familiar interface
* Automatic color reduction when pasting into low-res formats
* Bookmark ROM locations
* Create palettes or import them directly from ROM

Format definitions have been moved into the `jar` itself for simpler portability but new definitions can still easily be added to the XML `tmspec.xml` file. The same is true for translation files. Please reach out if you would like to contribute new format definitions or translations to any other languages.

Credits & License
---------

This minor modernization of Ken Hanson's original Tile program is released under the same GNU GPLv2 as outlined in the [LICENSE](LICENSE).

Some of the code included also comes from Kerry Shetline <kerry@shetline.com>, Matthias Burg <matthias@burgsoft.de>, Jef Poskanzer <jef@acme.com>, J. David Eisenberg <david@catcode.com>, Richard J.Osbaldeston, Kent Hansen and John Cristy. Where this is the case, the requisite notices and licenses are included.