package org.teiid.translator.solr.execution;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
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
import org.teiid.language.SQLConstants.Tokens;
import org.teiid.language.visitor.HierarchyVisitor;
import org.teiid.logging.LogManager;
import org.teiid.metadata.RuntimeMetadata;

/**
 * @author student
 * 
 */
public class SolrSQLHierarchyVistor extends HierarchyVisitor {

	private RuntimeMetadata metadata;
	protected static final String UNDEFINED = "<undefined>"; //$NON-NLS-1$
	private SolrQuery params = new SolrQuery();
	protected StringBuilder buffer = new StringBuilder();

	List<DerivedColumn> fieldNameList;

	// private LogManager logger;

	public SolrSQLHierarchyVistor(RuntimeMetadata metadata) {
		this.metadata = metadata;

	}

	@Override
	public void visit(Select obj) {
		// TODO Auto-generated method stub
		super.visit(obj);
		if (obj.getFrom() != null && !obj.getFrom().isEmpty()) {
			NamedTable table = (NamedTable) obj.getFrom().get(0);
		
			// testing
			// //check if select all case
			// if(table.getMetadataObject().getColumns() != null){
//			 if (obj.getDerivedColumns().size() ==
//			 table.getMetadataObject().getColumns().size()){
//			 buffer.append("*");
			// }else{
			// append(obj.getDerivedColumns());
			// }
			// }
		}

		//if there isn't a where clause then get everything
		if(obj.getWhere() == null){
			buffer.append("*:*");
		}
		
		fieldNameList = obj.getDerivedColumns();
		// System.out.println(obj.getDerivedColumns()); //testing
		// add query fields
		for (DerivedColumn field : fieldNameList) {
			params.setFields(getShortName(field.toString()));
			// System.out.println(params.getFields());
		}
		System.out.println(params.getQuery());
	}

	/**
	 * @param elementName
	 * @return
	 * @since 4.3
	 */
	public static String getShortName(String elementName) {
		int lastDot = elementName.lastIndexOf("."); //$NON-NLS-1$
		if (lastDot >= 0) {
			elementName = elementName.substring(lastDot + 1);
		}
		return elementName;
	}

	/**
	 * @return the full column names tableName.columnNames
	 */
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

	public SolrQuery getParams() {
		// System.out.println(this.params.getFields());
		return this.params;
	}
	  
    /**
     * Appends the string form of the LanguageObject to the current buffer.
     * @param obj the language object instance
     */
    public void append(LanguageObject obj) {
        if (obj == null) {
            buffer.append(UNDEFINED);
        } else {
            visitNode(obj);
        }
    }
    
//    /**
//     * Simple utility to append a list of language objects to the current buffer
//     * by creating a comma-separated list.
//     * @param items a list of LanguageObjects
//     */
//    protected void append(List<? extends LanguageObject> items) {
//        if (items != null && items.size() != 0) {
//            append(items.get(0));
//            for (int i = 1; i < items.size(); i++) {
//                buffer.append(Tokens.COMMA)
//                      .append(Tokens.SPACE);
//                append(items.get(i));
//            }
//        }
//    }
//    
//    /**
//     * Simple utility to append an array of language objects to the current buffer
//     * by creating a comma-separated list.
//     * @param items an array of LanguageObjects
//     */
//    protected void append(LanguageObject[] items) {
//        if (items != null && items.length != 0) {
//            append(items[0]);
//            for (int i = 1; i < items.length; i++) {
//                buffer.append(Tokens.COMMA)
//                      .append(Tokens.SPACE);
//                append(items[i]);
//            }
//        }
//    }
}
