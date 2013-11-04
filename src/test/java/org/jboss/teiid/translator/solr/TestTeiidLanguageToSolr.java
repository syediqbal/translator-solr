package org.jboss.teiid.translator.solr;

import java.io.IOException;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.teiid.cdk.CommandBuilder;
import org.teiid.core.util.ObjectConverterUtil;
import org.teiid.core.util.UnitTestUtil;
import org.teiid.language.Command;
import org.teiid.language.Select;
import org.teiid.metadata.Column;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.metadata.Table;
import org.teiid.query.metadata.CompositeMetadataStore;
import org.teiid.query.metadata.QueryMetadataInterface;
import org.teiid.query.metadata.SystemMetadata;
import org.teiid.query.metadata.TransformationMetadata;
import org.teiid.query.unittest.RealMetadataFactory;
import org.teiid.translator.TypeFacility;
import org.teiid.translator.solr.SolrExecutionFactory;
import org.teiid.translator.solr.execution.SolrQueryExecution;
import org.teiid.translator.solr.execution.SolrSQLHierarchyVistor;
import org.teiid.cdk.api.TranslationUtility;

@SuppressWarnings("nls")
public class TestTeiidLanguageToSolr {

	private TransformationMetadata metadata;
	private SolrExecutionFactory translator;
	private TranslationUtility utility;

	private QueryMetadataInterface setUp(String ddl, String vdbName,
			String modelName) throws Exception {

		this.translator = new SolrExecutionFactory();
		this.translator.start();

		// MetadataFactory mf = new MetadataFactory("", 1, "",
		// SystemMetadata.getInstance().getRuntimeTypeMap(), new Properties(),
		// "");

		metadata = RealMetadataFactory.fromDDL(ddl, vdbName, modelName);
		this.utility = new TranslationUtility(metadata);

		return metadata;
	}

	// private void createFakeMetadata(MetadataFactory mf) {
	//
	// Table example = mf.addTable("example");
	// Column id = mf.addColumn("price", TypeFacility.RUNTIME_NAMES.FLOAT,
	// example);
	// Column name = mf.addColumn("weight", TypeFacility.RUNTIME_NAMES.FLOAT,
	// example);
	// Column age = mf.addColumn("popularity",
	// TypeFacility.RUNTIME_NAMES.INTEGER, example);
	// }

	private String getSolrTranslation(String sql) throws IOException, Exception {
		Select select = (Select)getCommand(sql);
		SolrSQLHierarchyVistor visitor = new SolrSQLHierarchyVistor(
				this.utility.createRuntimeMetadata());
System.out.println("\n\nstart visitor"); 
		visitor.visit(select);
		System.out.println("end visitor"); 
		return visitor.getTranslatedSQL();

	}

	public Command getCommand(String sql) throws IOException, Exception {

		CommandBuilder builder = new CommandBuilder(setUp(
				ObjectConverterUtil.convertFileToString(UnitTestUtil
						.getTestDataFile("exampleTBL.dll")), "exampleVDB",
				"exampleModel"));
		return builder.getCommand(sql);

	}

//	@Test
//	public void testSelectStar() throws Exception {
//
//		// column test, all columns translates to price, weight and popularity
//		Assert.assertEquals(getSolrTranslation("select * from example"), "*:*");
//
//	}
//
//	@Test
//	public void testSelectColumn() throws Exception {
//		Assert.assertEquals(
//				getSolrTranslation("select price,weight,popularity from example"), "*:*");
//	}
//
//	@Test
//	public void testSelectFrom() throws Exception {
//	}
//
//	@Test
//	public void testSelectFromJoin() throws Exception {
//	}
//
//	@Test
//	public void testSelectWhereEQ() throws Exception {
//		Assert.assertEquals(
//				getSolrTranslation("select price,weight,popularity from example where price=1"),
//				"price:1.0");
//	}
//	//only need to preform LT bc SOLR does not handle strict <,> only <=,>=
//	@Test
//	public void testSelectWhereGT() throws Exception {
//		Assert.assertEquals(
//				getSolrTranslation("select price,weight,popularity from example where price>1"),
//				"price:[1.0 TO *]");
//	}
//	//only need to preform LT bc SOLR does not handle strict <,> only <=,>=
//	@Test
//	public void testSelectWhereGE() throws Exception {
//		Assert.assertEquals(
//				getSolrTranslation("select price,weight,popularity from example where price>=1"),
//				"price:[1.0 TO *]");
//	}
//	//only need to preform LT bc SOLR does not handle strict <,> only <=,>=
//	@Test
//	public void testSelectWhereLT() throws Exception {
//		Assert.assertEquals(
//				getSolrTranslation("select price,weight,popularity from example where price<1"),
//				"price:[* TO 1.0]");
//	}
//	//only need to preform LT bc SOLR does not handle strict <,> only <=,>=
//	@Test
//	public void testSelectWhereLE() throws Exception {
//		Assert.assertEquals(
//				getSolrTranslation("select price,weight,popularity from example where price<=1"),
//				"price:[* TO 1.0]");
//	}
//	@Test
//	public void testSelectWhereNEQ() throws Exception {
//		Assert.assertEquals(
//				getSolrTranslation("select price,weight,popularity from example where price!=1"),
//				"NOT price:1.0");
//	}
	@Test
	public void testSelectWhenOr() throws Exception {
		Assert.assertEquals(
				getSolrTranslation("select price,weight,popularity from example where price=1 or weight >5"),
				"((price:1.0) OR (weight:[5.0 TO *]))");
	}

//	@Test
//	public void testSelectWhenOr() throws Exception {
//		Assert.assertEquals(
//				getSolrTranslation("select price,weight,popularity from example where weight < 5 AND price=1 or weight > 5 and popularity>2 or popularity < 1"),
//				"price:1.0 OR weight:[5 TO *]");
//	}
//	@Test
//	public void testSelectWhenOr() throws Exception {
//		Assert.assertEquals(
//				getSolrTranslation("select price,weight,popularity from example where (weight < 5 OR price=1) AND weight > 5"),
//				"price:1.0 OR weight:[5 TO *]");
//	}

//	@Test
//	public void testSelectWhenLike() throws Exception {
//	}
//
//	@Test
//	public void testSelectWhenNot() throws Exception {
//	}
//
//	@Test
//	public void testSelectWhenIn() throws Exception {
//	}
//
//	@Test
//	public void testSelectWhenAndOr() throws Exception {
//	}
//
//	@Test
//	public void testSelectWhenAnd() throws Exception {
//	}
//
//	@Test
//	public void testSelectGroupBy() throws Exception {
//	}
//
//	@Test
//	public void testSelectWhenOrderBy() throws Exception {
//	}
//
//	@Test
//	public void testSelectWhenComparison() throws Exception {
//		// =
//		// <
//		// >
//		// <=
//		// >=
//		// !=
//	}
}
