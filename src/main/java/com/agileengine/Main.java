package com.agileengine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Optional;

public class Main {

    private static String CHARSET_NAME = "utf8";

    static String resourcePathOriginal = "./samples/sample-0-origin.html";
//    static String resourcePathTarget = "./samples/sample-1-evil-gemini.html";
//    static String resourcePathTarget = "./samples/sample-2-container-and-clone.html";
//    static String resourcePathTarget = "./samples/sample-3-the-escape.html";
    static String resourcePathTarget = "./samples/sample-4-the-mash.html";
    static String originalElementId = "make-everything-ok-button";

    public static void main(String[] args) {
        if (args.length >= 2 ) {
            resourcePathOriginal = args[0];
            resourcePathTarget = args[1];
            if (args.length == 3) {
                originalElementId = args[2];
            }
        }

        Optional<Element> buttonOpt = findElementById(new File(resourcePathOriginal), originalElementId);

        if (!buttonOpt.isPresent()) {
            System.out.println("Original element not found by id");
            return;
        }

        Element originalElement = buttonOpt.get();

        System.out.println("Original element info:");
        System.out.println("attributes: " + originalElement.attributes());
        System.out.println("name: " + originalElement.tagName());
        System.out.println("text: " + originalElement.ownText());
        System.out.println();

        Optional<Elements> selectedElementsByTagNameOpt = findElementsByTagName(new File(resourcePathTarget), originalElement.tagName());

        if (!selectedElementsByTagNameOpt.isPresent()) {
            System.out.println("No elements matching tag name were found");
            return;
        }

        Elements selectedElementsByTagName = selectedElementsByTagNameOpt.get();

        Element resultingElement = selectedElementsByTagName.first();
        int attributesMatchingResult = -1;

        for (Element currentElement : selectedElementsByTagName) {
            int attributesMatchingCurrent = 0;

            for (Attribute originalElementAttribute : originalElement.attributes()) {
                if (originalElementAttribute.getValue().equals(currentElement.attr(originalElementAttribute.getKey()))) {
                    attributesMatchingCurrent++;
                }
            }
            if (!originalElement.ownText().equals("") && originalElement.ownText().equals(currentElement.ownText())) {
                attributesMatchingCurrent++;
            }

            if (attributesMatchingCurrent > attributesMatchingResult) {
                resultingElement = currentElement;
                attributesMatchingResult = attributesMatchingCurrent;
            }
        }

        ArrayDeque<Element> treeToResult = new ArrayDeque<>();
        while (resultingElement.hasParent()) {
            treeToResult.push(resultingElement);
            resultingElement = resultingElement.parent();
        }

        System.out.println("Result path:");
        while (!treeToResult.isEmpty()) {
            Element item = treeToResult.pop();
            System.out.println(item.tagName() + " class=\"" + item.className() + "\"" + " text=\"" + item.ownText() + "\"");
        }
    }


    private static Optional<Element> findElementById(File htmlFile, String targetElementId) {
        try {
            Document doc = Jsoup.parse(htmlFile, CHARSET_NAME, htmlFile.getAbsolutePath());
            return Optional.of(doc.getElementById(targetElementId));

        } catch (IOException e) {
            System.out.println("Error while reading original file");
            return Optional.empty();
        }
    }

    private static Optional<Elements> findElementsByTagName(File htmlFile, String tagName) {
        try {
            Document doc = Jsoup.parse(htmlFile, CHARSET_NAME, htmlFile.getAbsolutePath());
            return Optional.of(doc.select(tagName));

        } catch (IOException e) {
            System.out.println("Error while reading target file");
            return Optional.empty();
        }
    }
}
