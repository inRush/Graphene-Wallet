package com.gxb.gxswallet.utils.jdenticon;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.gxb.sdk.bitlib.util.HashUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class Jdenticon {

    private final String hash;
    private int size;
    private double padding;
    private JdenticonWrapper jdenticonWrapper;

    private Jdenticon(String hash) {
        jdenticonWrapper = JdenticonWrapper.getInstance();
        this.hash = hash;
        this.size = 300;
        this.padding = 0.08;
    }

    public static Jdenticon from(String text) {

        return new Jdenticon(HashUtils.sha256(text.getBytes()).toString());
    }

//    public Jdenticon withSize(int size) {
//
//        if (size <= 30) {
//            throw new IllegalArgumentException("Size must be greater than 30");
//        }
//
//        this.size = size;
//        return this;
//    }
//
//    public Jdenticon withPadding(double padding) {
//
//        if (padding < 0.0) {
//            throw new IllegalArgumentException("Padding must be greater than 0.0");
//        }
//
//        if (padding > 0.5) {
//            throw new IllegalArgumentException("Padding must be smaller than 0.5");
//        }
//
//        this.padding = padding;
//        return this;
//    }
//
//    public String toSvg() {
//
//        return jdenticonWrapper.getSvg(this);
//    }

    public File file() throws IOException {

        File f = createTempFile(FileType.SVG);
        FileUtils.writeStringToFile(f, jdenticonWrapper.getSvg(this), String.valueOf(Charset.forName("UTF-8")));
        return f;
    }

    public File file(String fileName) throws IOException {

        File f = createTempFile(fileName, FileType.SVG);
        File result = new File(f.getParentFile(), fileName + ".svg");
        FileUtils.writeStringToFile(f, jdenticonWrapper.getSvg(this), String.valueOf(Charset.forName("UTF-8")));
        FileUtils.copyFile(f, result);
        return result;
    }

    public Drawable drawable() throws IOException, SVGParseException {
        File file = file();
        SVG svg = SVG.getFromInputStream(new FileInputStream(file));
        return new PictureDrawable(svg.renderToPicture());
    }

//    public File png() throws IOException, TranscoderException {
//
//        return transcodeSvgToPng("jdenticon-" + this.hash);
//    }
//
//    public File png(String filename) throws IOException, TranscoderException {
//
//        return transcodeSvgToPng(filename);
//    }

    String getHash() {
        return hash;
    }

    int getSize() {
        return size;
    }

    double getPadding() {
        return padding;
    }

    private File createTempFile(FileType fileType) throws IOException {
        File file = createTempFile("jdenticon-" + this.hash, fileType);
        file.deleteOnExit();
        return file;
    }

//    private File transcodeSvgToPng(String filename) throws IOException, TranscoderException {
//
//        PNGTranscoder pngTranscoder = new PNGTranscoder();
//
//        TranscoderInput input = new TranscoderInput(this.file().toURI().toString());
//
//        File outputfile = createTempFile(filename, FileType.PNG);
//
//        OutputStream ostream = new FileOutputStream(outputfile);
//        TranscoderOutput output = new TranscoderOutput(ostream);
//
//        pngTranscoder.transcode(input, output);
//
//        ostream.flush();
//        ostream.close();
//
//        return outputfile;
//    }

    private File createTempFile(String name, FileType fileType) throws IOException {

        File file;

        switch (fileType) {
            case SVG:
                file = File.createTempFile(name, ".svg");
                break;
            case PNG:
                file = File.createTempFile(name, ".png");
                break;
            default:
                file = File.createTempFile(name, ".svg");
                break;
        }

        file.deleteOnExit();
        return file;
    }

    private enum FileType {
        SVG, PNG
    }

}
