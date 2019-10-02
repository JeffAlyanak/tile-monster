# Tile Monster v1
**_Based on v0.15a of Kent Hansen's original TM program_**

* [Introduction](#introduction)
* [Architecture](#architecture)
* [How To Use](#how-to-use)
* [Configuring the Specs](#configuring-the-specs)
* [Writing File Listeners](#writing-file-listeners)
* [Language Support](#language-support)
* [Known Issues](#known-issues)

## Introduction

Tile Monster, or TM for short, is a program that facilitates the viewing and editing of raw graphics data; that is, graphics contained in arbitrary binary files, where one usually doesn't have initial knowledge about the location and/or format of the graphics. The main area of application thus far has been videogame console binaries. In most cases, these files contain a large variety of data, not just graphics -- sound data and actual game code are two examples. Collections of data in one and the same file may be interspersed in any conceivable fashion, subject to hardware/software-constraints of the intended runtime-environment as well as the decisions made by the producers of the binary (the developers).

TM is a kind of visual explorer of such files, in that it allows the file contents to be interpreted and displayed in a multitude of ways. By perusing the file and tweaking the settings of the renderer, the binary data that corresponds to graphics may be made accessible in the way they are intended, and edited to the satisfaction of the user.

## Architecture

Tile Monster is designed to be as format-independent as possible. This concerns both graphics formats, file formats and palette formats. The basic, fundamental entity that fuels TM's functionality is what I've dubbed the "graphics codec". Simply put, a Tile Monster graphics codec is a definition of how a chunk of data (more specifically, an array of bytes) is to be transformed (decoded) into an 8x8 grid of pixels (or tile), and from pixels back to data (encoded). In other words, it is a specification of a graphics format, and it is precisely the information Tile Monster needs in order to support the viewing and editing of a particular format. There are absolutely no such hard-coded formats supported by Tile Monster; every format is defined in an external resource file that is read when the program is started. The resource file that comes with this program distribution contains definitions of several "standard" graphics formats that are well-known and documented. However, the user also has the ability to edit the resource file and configure/add additional formats.

In addition to merely defining and describing graphics codecs, they can also be hooked to file extensions (filters) used by the Open File dialog in Tile Monster. Upon loading a file with a defined extension, Tile Monster will then automatically switch to the graphics mode associated with that extension.

Furthermore, "file listeners" can be registered to deal with constraints of various file formats. A file listener provides the detection of a particular file format, and is allowed to process file data when it is loaded and/or saved in Tile Monster. For example, many formats have headers with fields that should be updated after data has been modified (such as checksums). Other formats contain interleaved data; the file listener may then de-interleave the data when it is loaded, so that it is readily viewable, and re-interleave it when the file is saved.

Palette formats and the associated file extensions for those are handled in a way similar to file filters. The location, size and format of palette data within files of particular extensions can be defined; the data is then managed automatically when a proper palette file is loaded in Tile Monster.

The separation of graphics and file format definitions from the actual program gives Tile Monster substantial flexibility in dealing with a variety of files and formats. Graphics-, file- and palette formats can be defined, customized, removed or added without requiring changes to Tile Monster itself.

![Figure 1 illustrates the general idea behind graphics codecs.](https://gitlab.com/jeffalyanak/tile-monster/raw/master/manual/images/fig1.png)
*Figure 1 illustrates the general idea behind graphics codecs.*

A family of formats is a group of graphics formats that have a set of attributes in common. A generic codec for such a family is an (abstract) graphics encoder/decoder that can be configured to support any member of it. A specific format is a member of the family. We can arrive at such a member by instantiating the set of attributes with specific values. Finally, a specific codec can be constructed by fusing a description of the specific format in some language (explained later) with the generic codec.

An example follows in Figure 2.

![Figure 2: How a codec for Game Boy tiles is constructed.](https://gitlab.com/jeffalyanak/tile-monster/raw/master/manual/images/fig2.png)
*Figure 2: How a codec for Game Boy tiles is constructed.*


## How To Use

In terms of general file management and actual graphics editing, the program is purposefully very similar to programs such as Microsoft Paint and JASC Paintshop Pro. I will assume that the user has already had some experience with these or similar programs (and moreover, with GUI-driven applications in general), and therefore won't cover "For Dummies" aspects of program use. (If any Dummies should happen to read this: There might be a while before I get the backing and funds necessary to put out "Tile Monster For Dummies" -- patience please.)

I realize that this section is currently too brief, and will expand it once I get feedback concerning difficulties in using/accessing Tile Monster's functionality. The best way to learn about features right now is to goof around (or ask me questions).

### Browsing the file

Use the slider on the left side of the file window, the toolbar buttons or the keyboard. Like I mentioned, most binaries contain more than just graphics data so often you have to search through large portions of a file before you strike gold. (Uncompressed) graphics data is usually fairly easy to spot since it is a lot less "noisy" than, say, a sequence of program instructions. The strategy I use is to quickly scroll through the file using the slider, take note of "interesting" spots, and then start fine-tuning the other display settings until (hopefully) something cool reveals itself.

### Resizing the tile canvas

Changing the number of columns and rows that are displayed is often necessary in order to align graphics properly; for example, if a piece of graphics is eight tiles wide, you need to set the dimensions of the tile canvas accordingly (otherwise the graphics will look garbled). This can either be done from the Image->Canvas Size dialog, or incrementally through toolbar buttons. The keyboard shortcut keys for these are Shift+Up|Down|Left|Right.

### 1-D and 2-D modes explained

From the View->Mode menu you can select either 1-Dimensional (1-D) or 2-Dimensional (2-D) mode. This is to facilitate both individual-tile and "bitmapped" graphics modes, respecticely. In 1-D mode, data is interpreted as a sequence of individual bitmaps each 8x8 pixels in size; each such 8x8 bitmap (tile) is stored one after the other in memory. In 2-D mode, the data is interpreted as full bitmaps, where each scanline is stored one after the other in memory.

1-D modes are common in consoles with small graphics memories, particularly older ones. 2-D modes are found in consoles with high resolutions and large graphics memories, which is the norm nowadays. Future versions of TM is likely to be geared more towards these systems; it is a problem right now that such modes can only be viewed with a horizontal resolution which is a multiple of eight. (But if I removed this "feature", the program wouldn't really be a Tile Monster, now would it. :-] )

### Partioning the tile canvas into blocks

Sometimes it is very useful to be able to divide the grid of tiles into smaller, adjacent regions of tiles, or blocks. These "sub-grids" then become the units of display. You can set the block dimensions from the View->Block Size->Custom dialog. For example, if you have determined that the file you are editing contains a series of bitmaps each 4x4 tiles in size, you can set the block size accordingly and stack an arbitrary amount of these alongside eachother by resizing the tile canvas. Figure 3 illustrates the idea. (Gridlines are enabled for both blocks and tiles here to show the boundaries between them.)

![Figure 3: Block size of 4x4 allowing display of multiple images.](https://gitlab.com/jeffalyanak/tile-monster/raw/master/manual/images/fig3.png)
*Figure 3: Block size of 4x4 allowing display of multiple images.*

When you set the block size manually, it will stay fixed when you resize the tile canvas. If you want the block to be the same size as the tile canvas (i.e. treat the entire canvas as one image, which is the default behaviour), select Full Canvas from the View->Block Size menu.

### Row-interleaving blocks

This is only useful when working with certain 1-D graphics formats, to my knowledge; but it is indeed very useful. Basically, the tiles within each block are reordered as illustrated in this figure:

![Figure 4: A 4x2 block non-interleaved (left) versus interleaved (right).](https://gitlab.com/jeffalyanak/tile-monster/raw/master/manual/images/fig4.png)
*Figure 4: A 4x2 block non-interleaved (left) versus interleaved (right).*


So in interleaved mode, instead of tiles simply being displayed left-to-right, they are displayed in a sort of zig-zag pattern. Or, more formally, for every pair of consecutive rows in a block, the first row contains the even tiles (0, 2, 4, ...) while the second row contains the odd tiles (1, 2, 3, ...). This achieves the nifty effect of properly displaying tiles that are 8x16 pixels in size, which was a popular format for sprites in a few consoles (NES, Game Boy, ...). Figure 5 illustrates the dramatic impact that enabling row-interleaving has on the appearance of graphics in this format.

![Figure 5: Without row-interleaving (top) versus with row-interleaving (bottom).](https://gitlab.com/jeffalyanak/tile-monster/raw/master/manual/images/fig5.png)
*Figure 5: Without row-interleaving (top) versus with row-interleaving (bottom).*

### Setting the tile codec

As was mentioned, when you open a file for viewing/editing Tile Monster attempts to guess the correct graphics mode based on the filename extension. However, it may be necessary to change the codec used for displaying graphics manually. This can be done either from the View->Tile Codec menu or by pressing TAB or Shift+TAB repeatedly to cycle forwards and backwards through the installed codecs, respectively. In older consoles, there was usually only a single graphics format per console, so changing the tile codec isn't necessary once that format has been established. But in newer consoles, there are often a variety of formats used; for example, graphics in the Nintendo 64 game "Zelda: Ocarina of Time" range from 4-bit to 32-bit. So even though Tile Monster defaults to displaying N64 files in, say, 16-bit ARGB mode, don't think that that's the only mode you'll find N64 graphics in.

### Bookmarks

A bookmark records most of the visual state information at the time it was created, including the file offset, graphics mode, block settings and display width & height. You can then restore those settings at a later time simply by selecting the appropriate bookmark from the Navigate menu. The bookmarks can be organized in folders hierarchically.

### Importing Internal Palettes

Palettes can be referenced or extracted directly from the file you are editing through the Palette->Import From->This File dialog. So if you know the location, size and format of a palette within a file, you can specify it here. (Figuring out the actual location of such data is beyond the scope of this document, and may vary greatly among file types.) The "Copy" checkbox in the import dialog specifies whether the data should be extracted (copied) from the file or if it should merely be referenced. If it is referenced, then any changes to the palette data also affect the data in the file.

Palette management is done in a way similar to bookmarks. If you create or import a palette and want to keep the information/data for it on a more permanent basis, select Palette->Add To Palettes.

### Resource Files

Each file has a corresponding resource file that contains the bookmarks and palettes recorded for that file. Such resource files are stored in XML format in the resources folder. When you open a file, Tile Monster automatically loads the associated resource file.

### Exporting and importing bitmaps

... can be done from the edit menu: Copy To and Paste From, respectively. You can save and load bitmaps in a variety of formats. After exporting a chunk of graphics to a bitmap, you can edit it in a more powerful graphics application and import the bitmap back into Tile Monster again once you're satisfied.

## Configuring the Specs

The following sections are mainly for advanced users who want to extend the format support in Tile Monster.

The Tile Monster specifications are stored in the file `tmspec.xml`, which is located in the `src/resources` directory. The specs are written in XML (Extensible Markup Language). It is quite possible to write front-ends that can read, modify and write the contents of this file in a more user-friendly way, but this has not been a priority during the infancy stages of the application development. Thus, you need to be familiar with XML in order to configure the graphics formats, file extensions and such.

The layout of the specification file is like so:

```
- <tmspec>
  + <colorformats>
  + <tileformats>
  + <filefilters>
  + <palettefilters>
  + <filelisteners>
```
Each "major" tag and the allowed subtags are explained in detail in the following.

### directcolor

**Description:**
Defines a color in terms of Red, Green, Blue and Alpha components.
The components are specified as bitmasks in hexadecimal format. For example, if the bitmask for Red is FF0000, this states that the Red component is located in bits 16-23 of a word that holds a pixel (an instance of the color).
Each mask must have consecutive 1-bits and the number of 1-bits should not exceed 8.

**Attributes:**
`id` - Unique identification string. (Req)
`bpp` - Bits per pixel. The number of bits used to store a pixel using this color format, for example the sum of the number of bits in the bitmasks. (Req)
`rmask` - Bitmask for Red component. (Req)
`gmask` - Bitmask for Green component. (Req)
`bmask` - Bitmask for Blue component. (Req)
`amask` - Bitmask for Alpha component. (Opt)

**Subtags:**
`description` - Plaintext description of the color format. (Req)

**Example:**
```
<directcolor id="CF00" bpp="15" rmask="001F" gmask="03E0" bmask="7C00">
  <description>15bpp BGR (555)</description>
<directcolor>
```
### indexedcolor

**Description:**
Defines a set of colors through a table of predefined RGB values (palette). When a color in this format needs to be rendered, it can be looked up in the table to find its corresponding RGB representation.

**Attributes:**
`id` - Unique identification string. (Req)
`bpp` - Bits per pixel. This value implicitly defines the size of the RGB lookup table; for example, if 6 bits per pixel are used, the table must hold 2^6=64 entries. (Req)
`format` - Refers to the id attribute of a defined directcolor format. The entries in the RGB lookup table are stored in this format.
`endianness` - The byte order of the RGB lookup table data (read on). Can be little or big; the former is default. (Opt)

**Subtags:**
`data` - The RGB lookup table data, stored as a string of hexadecimal digits. The string is interpreted as a sequence of bytes stored in the appropriate directcolor format, using the specified byte order.(Req)
`description` - Plaintext description of the color format. (Req)

**Example:**
```
<indexedcolor id="CF02" bpp="6" endianness="big">
  <data>757575271B8F (and so on...) 000000</data>
  <description>Nintendo (NES)</description>
</indexedcolor>
```
### planartile

**Description:**
Defines a palettized planar tile format.

**Attributes:**
`id` - Unique identification string. (Req)
`bpp` - Bits per pixel. This also implicitly defines how many planes the tile consists of. (Req)
`planeorder` - The order of the bytes that make up a row (eight pixels) of the tile. It is a comma-separated list of offsets relative to zero. The largest offset should not exceed bpp-1. (Req)

**Subtags:**
`description` - Plaintext description of the tile format. (Req)

**Example:**
```
<planartile id="PL03" bpp="4" planeorder="0,1,2,3">
  <description>4bpp planar</description>
</planartile>
```

### lineartile

**Description:**
Defines a palettized linear tile format.

**Attributes:**
`id` - Unique identification string. (Req)
`bpp` - Bits per pixel. Must be one of 1, 2, 4 or 8.(Req)
`ordering` - The order of individual pixel data within a byte. This is similar to endianness, but on the sub-byte level. Possible values are in and reverse; the former is default. (Opt)

**Subtags:**
`description` - Plaintext description of the tile format. (Req)

**Example:**
```
<lineartile id="LN03" bpp="2" ordering="reverse">
  <description>2bpp linear, reverse-order</description>
</lineartile>
```

### directcolortile

**Description:**
Defines a direct-color linear tile format.

**Attributes:**
`id` - Unique identification string. (Req)
`bpp` - Bits per pixel. The number of bits used to store a pixel using this color format, for example the sum of the number of bits in the bitmasks. (Req)
`rmask` - Bitmask for Red component. (Req)
`gmask` - Bitmask for Green component. (Req)
`bmask` - Bitmask for Blue component. (Req)
`amask` - Bitmask for Alpha component. (Opt)

**Subtags:**
`description` - Plaintext description of the tile format. (Req)

**Example:**
```
<directcolortile id="DC00" bpp="15" rmask="7C00" gmask="03E0" bmask="001F">
  <description>15bpp RGB (555)</description>
</directcolortile>
```

### compositetile

**Description:**
Defines a composite tile format. A composite tile is a tile made up of two or more separate tiles, where each tile is of a format defined elsewhere. Each tile may itself be composite, so it is a sort of recursive format. As an example, Nintendo Entertainment System (NES) tiles are 2bpp planar, like Game Boy tiles; but unlike Game Boy tiles, NES tiles are stored as two consecutive (separate) 1bpp tiles. This behaviour cannot be specified through a regular planartile format; however, it is easily defined as a composite tile format.

**Attributes:**
`id` - Unique identification string. (Req)
`formats` - A comma-separated list of tile format identification strings; these refer to tile formats defined elsewhere in the specification document. This list defines both the order (starting with the lowest bits) and the actual format of the individual tiles that make up the composite tile. The tiles are assumed to be stored consecutively in memory. (Req)

**Subtags:**
`description` - Plaintext description of the tile format. (Req)

**Example:**
```
<compositetile id="CP01" formats="PL01,PL00">
  <description>3bpp planar, composite (2bpp+1bpp)</description>
</compositetile>
```

### filefilter

**Description:**
Defines a file filter. A file filter allows you to associate files of certain extensions with a default graphics format and display mode. The filter is used by the "Open File..." dialog in Tile Monster.

**Attributes:**
`extensions` - A comma-separated list of file extensions (without the '.' character) to be included in the file filter. The wildcard character '?' is supported, which matches any character. (Req)
`tileformat` - Refers to a defined tile format that should be used to display files supported by this file filter.(Req)
`mode` - The default mode to display graphics in when a file supported by this file filter is opened. Can be 1D or 2D. (Opt)

**Subtags:**
`description` - Plaintext description of the file filter. (Req)

**Example:**
```
<filefilter extensions="n64,v64,z64" tileformat="DC06" mode="2D">
  <description>Nintendo 64 (*.n64, *.v64, *.z64)</description>
</filefilter>
```

### palettefilter

**Description:**
Defines a palette filter. A palette filter allows you to associate palette files (files containing palette data) of certain extensions with information on how palette data can be extracted from these files. The filter is used by the "Import Palette..." dialog in Tile Monster.

**Attributes:**
`extensions` - A comma-separated list of file extensions (without the '.' character) to be included in the palette filter. The wildcard character '?' is supported, which matches any character. (Req)
`colorformat` - Identification string of a defined color format that will be used to read palette data from the file. (Req)
`size` - The size of the palette (number of entries). (Req)
`offset` - The starting offset of the palette data in the file.(Req) endianness - The byte order of the palette data. Can be little or big; the former is default. (Opt)

**Subtags:**
`description` - Plaintext description of the palette filter. (Req)

**Example:**
```
<palettefilter extensions="zs?" colorformat="CF00" size="256" offset="52243" endianness="little">
  <description>ZSNES Save States (*.zs?)</description>
</palettefilter>
```

### filelistener

**Description:**
Registers a file listener. A file listener receives notifications when a file has been loaded and when it is about to be saved. At these times it may perform various operations on the data. For example, when the file is saved it may recalculate checksums. The first criteria for a file listener to receive file-loaded/file-saving notifications is that the extension of the file being saved is among the extensions hooked to that file listener. The second criteria is that the file listener must determine that the file data is indeed of the correct format (and not, say, a file of Format X saved with an extension typically related to Format Y). This is typically done by checking header fields (ID strings and such). The listener itself is implemented as a Java class (more about this in the next section).

**Attributes:**
`classname` - The name of the Java class that implements the file listener. (Req)

**Subtags:**
None.

**Example:**
```
<filelistener classname="NESFileListener"/>
```

## Writing File Listeners

Writing a file listener can be done by subclassing the abstract class TMFileListener, which is found in the root directory of the source code distribution of the program. The following methods must be implemented:

`public boolean doFormatDetect(final byte[] data, String extension):`
Takes an array of bytes which is the contents of the file, and the lower-case file extension; returns `true` if the data and extension complies with the format(s) that the file listener supports, `false` otherwise. (One may choose to ignore the extension if the data itself can provide unambiguous evidence that the data is of the correct format; however, this isn't recommended in the general case.)

Typical code looks like this:

```
// verify extension
if (!extension.equals("gba")) return false;
// verify some header fields
// ...
if (!header_valid) return false;
// format verified
return true;
```

`public void fileLoaded(byte[] data, String extension):`
Invoked after the file has been loaded. data is the contents of the file; `extension` is the lower-case extension of the filename. This method is _only_ invoked when `doFormatDetect(data, extension)` has already returned `true`.

`public void fileSaving(byte[] data, String extension):`
Invoked before the file is saved. The parameters are the same as for `fileLoaded`.

Once the file listener has been compiled, it should be put in the Tile Monster root directory and registered in the `<filelisteners>` section of the specification file (tmspec.xml). The listener will then start receiving events the next time Tile Monster is started.

Only one file listener is allowed to process a particular file. The order of listeners as defined in the specification file defines the order in which Tile Monster will search for a listener when a file has been loaded. As soon as a file listener is found that returns `true` in `doFormatDetect`, that listener will receive both the `fileLoaded` and `fileSaved` events for the file that owns the data.

For real examples of file listeners, check out one or more of those that are included in the source distribution: These include `GameBoyAdvanceFileListener` and `SegaGenesisFileListener`.

## Language Support

Tile Monster can easily be translated to a different language. Language files can be added to the `src/resources/languages` directory.

Ensure that new language filenames follow the format `language_lc_CC.properties`, where **lc** is a lower-case, two-letter ISO language code (see, for example, http://www.ics.uci.edu/pub/ietf/http/related/iso639.txt) and **CC** is an upper-case, two-letter ISO country code (see, for example, http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html).

The lines starting with # are comments and aren't strictly necessary to translate. Other lines are of the form

```
TranslationKey = Translation Text
```
where `TranslationKey` is a descriptive name of a piece of text and must remain unchanged. On the right side of the equality sign, the actual text must be supplied in the proper language. You can use the standard English translation (language_en_US.properties) as a guide for making new translations.

The help file can also be translated. To do so, copy one of the already translated help files (say, help_en_US.htm) from the docs directory to help_lc_CC.htm (where lc and CC are the same as above) and translate the text within to the proper language.

## Known Issues

These are some of the current problems and shortcomings that will (hopefully) be addressed in the future. Complaints about these issues will generally be ignored (although the people who will be complaining probably haven't bothered reading this far...).

* Out of memory error when loading really large files. Nothing much I can do with it as it is due to the Java Virtual Machine (JVM). It can be rectified by forcing the JVM to grab more system memory on startup. Run Tile Monster from the command line with the option -mxXm, where X is the number of megabytes you require; i.e. java -mx500M -jar tm.jar to allocate 500MB.
* Undoing and redoing Copy/Paste operations isn't totally kosher.
* Edit Colors in Palette menu doesn't do anything. It's supposed to bring up a dialog where you view all the colors in the current palette and click on them to edit. As of yet the only way to edit colors is by double-clicking on a color directly in the palette panel.
* Bizarre problem related to full-screen modes in some emulators. If you're running Tile Monster, then run, say, FCEUltra in full-screen mode, then go back to TM, sometimes the GUI isn't repainted -- it appears totally dead. The way to fix it is by changing the main window state, for example by minimizing and subsequently maximizing it. This is either due to a bug in the screen mode restoration of FCEUltra, a bug in the Java GUI runtimes, or both; there's not much I can do about it, unless I can come up with some cheap hack to work around it.
* Move button in Organize Bookmarks/Palettes dialog doesn't do anything. It's supposed to bring up a new dialog where you select a folder to move the item(s) to. But this is kinda redundant anyway, since you can move items by dragging and dropping. Easy to implement, low priority.
* Translation isn't applied 100% everywhere in the program.
* The Mover tool (used for "dragging" the focus in a zoomed canvas) is a bit jerky, to put it mildly.
* In order to change languages after the first time you've run the program, you have to delete the file settings.xml and restart Tile Monster.
* Lots of other tiny things that serve to darken my day. Oh, you know what they are. 

