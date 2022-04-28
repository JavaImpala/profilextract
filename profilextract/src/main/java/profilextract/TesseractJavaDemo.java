package profilextract;


import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TesseractJavaDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(TesseractJavaDemo.class);

    private static final String TESSDATA_DIR = "/home/tor003/git/profilextract/profilextract/src/main/resources/data";
    private static final String ENGLISH = "nor";

    public static void main(String[] args) throws URISyntaxException {
		Path bengaliImage = Paths.get(TesseractJavaDemo.class.getResource("/images/img1.png").toURI());
		printLinesAndWordsFromOcr(ENGLISH, bengaliImage);
    }

    public static void printLinesAndWordsFromOcr(String language, Path imageFile) {
		TesseractOCRAnalyzer analyser = new TesseractOCRAnalyzer(TESSDATA_DIR, imageFile, language);
		// detect words
		List<OcrWord> words = analyser.extractWordsFromImage();
		words.forEach(word -> LOGGER.info("word: {}", word));
		// detect lines
		List<OcrWord> lines = analyser.extractLinesFromImage();
		lines.forEach(line -> LOGGER.info("line: {}", line));
    }

	public static void extractHOCR(String language, Path imageFile) {
		TesseractOCRAnalyzer analyser = new TesseractOCRAnalyzer(TESSDATA_DIR, imageFile, language);
		// detect words
		
		analyser.extractHOCRFromImage();
		analyser.extractTextFromImage(6);
		
	}

}