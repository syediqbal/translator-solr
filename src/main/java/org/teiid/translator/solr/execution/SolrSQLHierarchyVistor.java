package org.teiid.translator.solr.execution;

import java.util.List;


import org.teiid.language.Comparison;
import org.teiid.language.DerivedColumn;
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
	protected static final String UNDEFINED = "<undefined>"; //$NON-NLS-1$

	List<DerivedColumn> fieldNameList;
//	private LogManager logger;

	public SolrSQLHierarchyVistor(RuntimeMetadata metadata) {
		this.metadata = metadata;

	}

	@Override
	public void visit(Select obj) {
		// TODO Auto-generated method stub
		super.visit(obj);
		if (obj.getFrom() != null && !obj.getFrom().isEmpty()) {
			NamedTable table = (NamedTable) obj.getFrom().get(0);
			System.out.println(table); //testing
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

		fieldNameList = obj.getDerivedColumns();
//		System.out.println(obj.getDerivedColumns()); //testing
	}

	  /** 
     * @param elementName
     * @return
     * @since 4.3
     */
    public static String getShortName(String elementName) {
        int lastDot = elementName.lastIndexOf("."); //$NON-NLS-1$
        if(lastDot >= 0) {
            elementName = elementName.substring(lastDot+1);                
        } 
        return elementName;
    }
    
	public List<DerivedColumn> getFieldNameList() {
		return fieldNameList;
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

	public String getShortFieldName(int i) {
		return getShortName(fieldNameList.get(i).toString());

	}


	public String getFullFieldName(int i) {
		return fieldNameList.get(i).toString();

	}
}
