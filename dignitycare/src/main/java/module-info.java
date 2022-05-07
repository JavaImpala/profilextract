module dignitycare {
	exports no.ehealthresearch.dignitycare.parserUtil.lineParser;
	exports no.ehealthresearch.dignitycare.parserUtil.matcher;
	exports no.ehealthresearch.dignitycare.parserUtil.pageProcessor;
	exports no.ehealthresearch.dignitycare.profil.reports;
	exports no.ehealthresearch.dignitycare.grunt;
	exports no.ehealthresearch.dignitycare.parserUtil;
	exports no.ehealthresearch.dignitycare.profil.fhir;
	exports no.ehealthresearch.dignitycare.parserUtil.sequence;
	exports no.ehealthresearch.dignitycare.parserUtil.struct;
	exports no.ehealthresearch.dignitycare.opencv;
	exports no.ehealthresearch.dignitycare.parserUtil.endable;
	exports no.ehealthresearch.dignitycare.parserUtil.hocr;
	exports no.ehealthresearch.dignitycare;
	exports no.ehealthresearch.dignitycare.fastlege;
	exports no.ehealthresearch.dignitycare.tesseract;
	exports no.ehealthresearch.dignitycare.pdf;
	exports no.ehealthresearch.dignitycare.profil.reports.readers;

	requires hapi.fhir.base;
	requires itext;
	requires jackson.core;
	requires jackson.databind;
	requires jackson.dataformat.xml;
	requires java.desktop;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.graphics;
	requires jsoup;
	requires opencv;
	requires org.apache.commons.lang3;
	requires org.apache.logging.log4j;
	requires org.bytedeco.javacpp;
	requires org.bytedeco.leptonica;
	requires org.bytedeco.tesseract;
	requires org.hl7.fhir.r4;
	requires pdfbox;
}