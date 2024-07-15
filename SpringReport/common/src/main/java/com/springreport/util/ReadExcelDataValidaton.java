package com.springreport.util;

import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlCursor;
import javax.xml.namespace.QName;

public class ReadExcelDataValidaton {

	static java.util.List<XmlObject> getX14DataValidations(XSSFSheet sheet) {
		java.util.List<XmlObject> x14DataValidations = new java.util.ArrayList<XmlObject>();
		XmlCursor cursor = sheet.getCTWorksheet().newCursor();
		cursor.selectPath(
				"declare namespace x14='http://schemas.microsoft.com/office/spreadsheetml/2009/9/main' .//x14:dataValidation");
		while (cursor.hasNextSelection()) {
			cursor.toNextSelection();
			XmlObject obj = cursor.getObject();
			x14DataValidations.add(obj);
		}
		return x14DataValidations;
	}

	static void addXSSFX14DataValidations(XSSFSheet sheet, java.util.List<DataValidation> dataValidations) {
		java.util.List<XmlObject> x14DataValidations = getX14DataValidations(sheet);
		for (XmlObject x14DataValidation : x14DataValidations) {
			XSSFX14DataValidation xssfX14DataValidation = new XSSFX14DataValidation(x14DataValidation);
			dataValidations.add(xssfX14DataValidation);
		}
	}

	public static java.util.List<DataValidation> getDataValidations(Sheet sheet) {
		@SuppressWarnings("unchecked")
		java.util.List<DataValidation> dataValidations = (java.util.List<DataValidation>) sheet.getDataValidations();
		if (sheet instanceof XSSFSheet) {
			addXSSFX14DataValidations((XSSFSheet) sheet, dataValidations);
		}
		return dataValidations;
	}

	public static void main(String[] args) throws Exception {
		Workbook workbook = WorkbookFactory.create(new FileInputStream("./Excel.xlsx"));
		Sheet sheet = workbook.getSheetAt(0);
		java.util.List<DataValidation> dataValidations = getDataValidations(sheet);
		for (DataValidation dataValidation : dataValidations) {
			System.out.println(dataValidation);
			System.out.println(dataValidation.getValidationConstraint().getFormula1());
		}
		workbook.close();
	}
}

class XSSFX14DataValidation implements DataValidation {

	private DataValidationConstraint validationConstraint;
	private int errorStyle;
	private boolean emptyCellAllowed;
	private boolean suppressDropDownArrow;
	private boolean showPromptBox;
	private boolean showErrorBox;
	private String promptBoxTitle;
	private String promptBoxText;
	private String errorBoxTitle;
	private String errorBoxText;
	private CellRangeAddressList regions;

	public XSSFX14DataValidation(XmlObject x14DataValidation) {
		String type = "";
		XmlObject typeAttribute = x14DataValidation.selectAttribute(new QName("type"));
		if (typeAttribute != null)
			type = typeAttribute.newCursor().getTextValue();
		Integer validationType = DataValidationConstraint.ValidationType.ANY;
		if ("CUSTOM".equalsIgnoreCase(type)) {
			validationType = DataValidationConstraint.ValidationType.FORMULA;
		} else if ("DATE".equalsIgnoreCase(type)) {
			validationType = DataValidationConstraint.ValidationType.DATE;
		} else if ("DECIMAL".equalsIgnoreCase(type)) {
			validationType = DataValidationConstraint.ValidationType.DECIMAL;
		} else if ("LIST".equalsIgnoreCase(type)) {
			validationType = DataValidationConstraint.ValidationType.LIST;
		} else if ("NONE".equalsIgnoreCase(type)) {
			validationType = DataValidationConstraint.ValidationType.ANY;
		} else if ("TEXT_LENGTH".equalsIgnoreCase(type)) {
			validationType = DataValidationConstraint.ValidationType.TEXT_LENGTH;
		} else if ("TIME".equalsIgnoreCase(type)) {
			validationType = DataValidationConstraint.ValidationType.TIME;
		} else if ("WHOLE".equalsIgnoreCase(type)) {
			validationType = DataValidationConstraint.ValidationType.INTEGER;
		}

		String operator = "";
		XmlObject operatorAttribute = x14DataValidation.selectAttribute(new QName("operator"));
		if (operatorAttribute != null)
			operator = operatorAttribute.newCursor().getTextValue();
		Integer operatorType = DataValidationConstraint.OperatorType.IGNORED;
		if ("BETWEEN".equalsIgnoreCase(operator)) {
			operatorType = DataValidationConstraint.OperatorType.BETWEEN;
		} else if ("NOT_BETWEEN".equalsIgnoreCase(operator)) {
			operatorType = DataValidationConstraint.OperatorType.NOT_BETWEEN;
		} else if ("EQUAL".equalsIgnoreCase(operator)) {
			operatorType = DataValidationConstraint.OperatorType.EQUAL;
		} else if ("NOT_EQUAL".equalsIgnoreCase(operator)) {
			operatorType = DataValidationConstraint.OperatorType.NOT_EQUAL;
		} else if ("GREATER_THAN".equalsIgnoreCase(operator)) {
			operatorType = DataValidationConstraint.OperatorType.GREATER_THAN;
		} else if ("GREATER_OR_EQUAL".equalsIgnoreCase(operator)) {
			operatorType = DataValidationConstraint.OperatorType.GREATER_OR_EQUAL;
		} else if ("LESS_THAN".equalsIgnoreCase(operator)) {
			operatorType = DataValidationConstraint.OperatorType.LESS_THAN;
		} else if ("LESS_OR_EQUAL".equalsIgnoreCase(operator)) {
			operatorType = DataValidationConstraint.OperatorType.LESS_OR_EQUAL;
		}

		String formula1 = null;
		XmlObject[] xmlObjects = x14DataValidation
				.selectChildren(new QName("http://schemas.microsoft.com/office/spreadsheetml/2009/9/main", "formula1"));
		if (xmlObjects.length > 0) {
			XmlObject formula1Element = xmlObjects[0];
			formula1 = formula1Element.newCursor().getTextValue();
		}

		String formula2 = null;
		xmlObjects = x14DataValidation
				.selectChildren(new QName("http://schemas.microsoft.com/office/spreadsheetml/2009/9/main", "formula2"));
		if (xmlObjects.length > 0) {
			XmlObject formula2Element = xmlObjects[0];
			formula2 = formula2Element.newCursor().getTextValue();
		}

		this.validationConstraint = new XSSFDataValidationConstraint(validationType, operatorType, formula1, formula2);

		this.regions = new CellRangeAddressList();
		String sqref = "";
		xmlObjects = x14DataValidation
				.selectChildren(new QName("http://schemas.microsoft.com/office/excel/2006/main", "sqref"));
		if (xmlObjects.length > 0) {
			XmlObject sqrefElement = xmlObjects[0];
			sqref = sqrefElement.newCursor().getTextValue();
		}
		String[] refs = sqref.split(" ");
		for (String ref : refs) {
			CellRangeAddress address = CellRangeAddress.valueOf(ref);
			this.regions.addCellRangeAddress(address);
		}

		String allowBlank = "";
		XmlObject allowBlankAttribute = x14DataValidation.selectAttribute(new QName("allowBlank"));
		if (allowBlankAttribute != null)
			allowBlank = allowBlankAttribute.newCursor().getTextValue();
		this.emptyCellAllowed = ("1".equals(allowBlank) || "TRUE".equalsIgnoreCase(allowBlank));

		String showInputMessage = "";
		XmlObject showInputMessageAttribute = x14DataValidation.selectAttribute(new QName("showInputMessage"));
		if (showInputMessageAttribute != null)
			showInputMessage = showInputMessageAttribute.newCursor().getTextValue();
		this.showPromptBox = ("1".equals(showInputMessage) || "TRUE".equalsIgnoreCase(showInputMessage));

		String showErrorMessage = "";
		XmlObject showErrorMessageAttribute = x14DataValidation.selectAttribute(new QName("showErrorMessage"));
		if (showErrorMessageAttribute != null)
			showErrorMessage = showErrorMessageAttribute.newCursor().getTextValue();
		this.showErrorBox = ("1".equals(showErrorMessage) || "TRUE".equalsIgnoreCase(showErrorMessage));

		// TODO: complete

	}

	public DataValidationConstraint getValidationConstraint() {
		return this.validationConstraint;
	}

	public void setErrorStyle(int errorStyle) {
		this.errorStyle = errorStyle;
	}

	public int getErrorStyle() {
		return this.errorStyle;
	}

	public void setEmptyCellAllowed(boolean allowed) {
		this.emptyCellAllowed = allowed;
	}

	public boolean getEmptyCellAllowed() {
		return this.emptyCellAllowed;
	}

	public void setSuppressDropDownArrow(boolean suppress) {
		this.suppressDropDownArrow = suppress;
	}

	public boolean getSuppressDropDownArrow() {
		return this.suppressDropDownArrow;
	}

	public void setShowPromptBox(boolean show) {
		this.showPromptBox = show;
	}

	public boolean getShowPromptBox() {
		return this.showPromptBox;
	}

	public void setShowErrorBox(boolean show) {
		this.showErrorBox = show;
	}

	public boolean getShowErrorBox() {
		return this.showErrorBox;
	}

	public void createPromptBox(String title, String text) {
		this.promptBoxTitle = title;
		this.promptBoxText = text;
	}

	public String getPromptBoxTitle() {
		return this.promptBoxTitle;
	}

	public String getPromptBoxText() {
		return this.promptBoxText;
	}

	public void createErrorBox(String title, String text) {
		this.errorBoxTitle = title;
		this.errorBoxText = text;
	}

	public String getErrorBoxTitle() {
		return this.errorBoxTitle;
	}

	public String getErrorBoxText() {
		return this.errorBoxText;
	}

	public CellRangeAddressList getRegions() {
		return this.regions;
	}
}
