package org.teiid.translator.solr.execution;

import java.util.List;

import org.teiid.language.Comparison;
import org.teiid.language.Expression;
import org.teiid.language.In;
import org.teiid.language.LanguageObject;
import org.teiid.language.Like;
import org.teiid.language.NamedTable;
import org.teiid.language.Not;
import org.teiid.language.Select;
import org.teiid.language.With;
import org.teiid.language.visitor.HierarchyVisitor;
import org.teiid.logging.LogManager;
import org.teiid.metadata.RuntimeMetadata;

public class SolrSQLHierarchyVistor extends HierarchyVisitor {

	private RuntimeMetadata metadata;
	private LogManager logger;
	protected StringBuilder buffer = new StringBuilder();
	protected static final String UNDEFINED = "<undefined>"; //$NON-NLS-1$

	String[] fieldNameList;



	public SolrSQLHierarchyVistor(RuntimeMetadata metadata, LogManager logger) {
		this.metadata = metadata;
		this.logger = logger;

	}

	@Override
	public void visit(Select obj) {
		// TODO Auto-generated method stub
		super.visit(obj);
		if (obj.getFrom() != null && !obj.getFrom().isEmpty()) {
			NamedTable table = (NamedTable) obj.getFrom().get(0);

			// //check if select all case
			// if(table.getMetadataObject().getColumns() != null){
			// if (obj.getDerivedColumns().size() ==
			// table.getMetadataObject().getColumns().size()){
			// buffer.append("*");
			// }else{
			// append(obj.getDerivedColumns());
			// }
			// }
		}
		
		fieldNameList = obj.getColumnNames();

	}
	
	public String[] getFieldNameList() {
		return fieldNameList;
	}
	public String getTranslatedSQL(LanguageObject obj) {
		return buffer.toString();
	}

	@Override
	public void visit(Comparison obj) {
		// TODO Auto-generated method stub
		super.visit(obj);
		Expression lhs = obj.getLeftExpression();
	}

	@Override
	public void visit(In obj) {
		// TODO Auto-generated method stub
		super.visit(obj);
	}

	@Override
	public void visit(Like obj) {
		// TODO Auto-generated method stub
		super.visit(obj);
	}

	@Override
	public void visit(Not obj) {
		// TODO Auto-generated method stub
		super.visit(obj);
	}

	@Override
	public void visit(With obj) {
		// TODO Auto-generated method stub
		super.visit(obj);
	}

	private String formatSolrQuery(String solrQuery) {

		solrQuery = solrQuery.replace("%", "*");
		// solrQuery = solrQuery.replace("_", "?");

		return solrQuery;

	}

	/**
	 * Appends the string form of the LanguageObject to the current buffer.
	 * 
	 * @param obj
	 *            the language object instance
	 */
	public void append(LanguageObject obj) {
		if (obj == null) {
			buffer.append(UNDEFINED);
		} else {
			visitNode(obj);
		}
	}
}
