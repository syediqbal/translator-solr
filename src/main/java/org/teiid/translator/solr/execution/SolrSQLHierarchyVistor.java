package org.teiid.translator.solr.execution;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.teiid.language.AndOr;
import org.teiid.language.Comparison;
import org.teiid.language.DerivedColumn;
import org.teiid.language.Expression;
import org.teiid.language.In;
import org.teiid.language.LanguageObject;
import org.teiid.language.Like;
import org.teiid.language.NamedTable;
import org.teiid.language.Not;
import org.teiid.language.SQLConstants;
import org.teiid.language.SQLConstants.Reserved;
import org.teiid.language.Select;
import org.teiid.language.With;
import org.teiid.language.SQLConstants.Tokens;
import org.teiid.language.visitor.HierarchyVisitor;
import org.teiid.logging.LogManager;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.query.parser.Token;

/**
 * @author student
 * 
 */
public class SolrSQLHierarchyVistor extends HierarchyVisitor {

	private RuntimeMetadata metadata;
	protected static final String UNDEFINED = "<undefined>"; //$NON-NLS-1$
	private SolrQuery params = new SolrQuery();
	private LogManager logger;
	protected StringBuilder buffer = new StringBuilder();
	private SQLConstants token;
	List<DerivedColumn> fieldNameList;

	// private LogManager logger;

	public SolrSQLHierarchyVistor(RuntimeMetadata metadata) {
		this.metadata = metadata;

	}

	@Override
	public void visit(Select obj) {
		// TODO Auto-generated method stub
		super.visit(obj);
		System.out.println("\tstart select visitor: ");
		if (obj.getFrom() != null && !obj.getFrom().isEmpty()) {
			NamedTable table = (NamedTable) obj.getFrom().get(0);

			// testing
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
		// //if there isn't a where clause then get everything
		// if(obj.getWhere() == null){
		// buffer.append("*:*");
		// }
		// visitNode(obj.getWhere());
		fieldNameList = obj.getDerivedColumns();
		// System.out.println(obj.getDerivedColumns()); //testing
		// add query fields
		// for (DerivedColumn field : fieldNameList) {
		// params.setFields(getShortName(field.toString()));
		// // System.out.println(params.getFields());
		// }
		System.out.println("where clause: " + obj.getWhere());
		System.out.println("\tend select visitor: ");
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

	/*
	 * (non-Javadoc) Note: Solr does not support <,> exclusively. It is always
	 * <=, >=
	 * 
	 * @see
	 * org.teiid.language.visitor.HierarchyVisitor#visit(org.teiid.language.
	 * Comparison)
	 */
	@Override
	public void visit(Comparison obj) {
		// TODO Auto-generated method stub
		// super.visit(obj);
		LogManager.logInfo(
				"Parsing compound criteria. Current query string is: ",
				buffer.toString());
//		System.out.println("\t\tstart comparison visit");
		String lhs = getShortName(obj.getLeftExpression().toString());
		Expression rhs = obj.getRightExpression();
//		System.out.print("\t\t\tlhs: " + obj.getLeftExpression().toString());
//		System.out.print("  operator: " + obj.getOperator().toString());
//		System.out.println("  rhs: " + obj.getRightExpression().toString());
		if (lhs != null) {
			switch (obj.getOperator()) {
			case EQ:
				buffer.append(lhs).append(":").append(rhs.toString());
				break;
			case NE:
				buffer.append("NOT").append(Tokens.SPACE).append(lhs)
						.append(":").append(rhs.toString());
				break;
			case LE:
			case LT:
				buffer.append(lhs).append(":[* TO").append(Tokens.SPACE)
						.append(rhs.toString()).append("]");
				break;
			case GE:
			case GT:
				buffer.append(lhs).append(":[").append(rhs.toString())
						.append(" TO *]");
				break;
			}
		}

		// if(obj.getOperator() !=null)
		// {
		// switch (obj.getOperator()) {
		// case AND:
		// buffer.append(Tokens.SPACE).append(Reserved.AND).append(Tokens.SPACE);
		// case OR:
		// buffer.append(Tokens.SPACE).append(Reserved.OR).append(Tokens.SPACE);
		// break;
		// }
		//
		// }
		System.out.println("\t\tend comparison visit");
	}

	@Override
	public void visit(AndOr obj) {
		// TODO Auto-generated method stub
		// super.visit(obj);
//		System.out.println("\t\tstart andor visit");
//		System.out.print("\t\t\tlhs: " + obj.getLeftCondition().toString());
//		System.out.print("  operator: " + obj.getOperator().toString());
//		System.out.println("  rhs: " + obj.getRightCondition().toString());

		// prepare statement
		buffer.append(Tokens.LPAREN);
		buffer.append(Tokens.LPAREN);

		// walk left node
		super.visitNode(obj.getLeftCondition());

		buffer.append(Tokens.RPAREN);

		switch (obj.getOperator()) {
		case AND:
			buffer.append(Tokens.SPACE).append(Reserved.AND)
					.append(Tokens.SPACE);
			break;
		case OR:
			buffer.append(Tokens.SPACE).append(Reserved.OR)
					.append(Tokens.SPACE);
			break;
		}
		
		buffer.append(Tokens.LPAREN);
		
		//walk right node
		super.visitNode(obj.getRightCondition());
		buffer.append(Tokens.RPAREN);
		buffer.append(Tokens.RPAREN);
		
//		System.out.println("\t\tend andor");
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

	public String getTranslatedSQL() {
		if (buffer == null || buffer.length() == 0) {
			return "*:*";
		} else {
			return buffer.toString();
		}

	}
}
