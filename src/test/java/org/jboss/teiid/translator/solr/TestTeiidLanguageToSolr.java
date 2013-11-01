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
import org.teiid.translator.solr.execution.SolrSQLHierarchyVistor;
import org.teiid.cdk.api.TranslationUtility;


@SuppressWarnings("nls")
public class TestTeiidLanguageToSolr {
	
	private TransformationMetadata metadata;
	private SolrExecutionFactory translator;
	private TranslationUtility utility;
	
	private QueryMetadataInterface setUp(String ddl, String vdbName, String modelName) throws Exception {
		
		this.translator = new SolrExecutionFactory();
		this.translator.start();
		
		//MetadataFactory mf = new MetadataFactory("", 1, "", SystemMetadata.getInstance().getRuntimeTypeMap(), new Properties(), "");
		
		metadata = RealMetadataFactory.fromDDL(ddl, vdbName, modelName);
		this.utility = new TranslationUtility(metadata);
		
		return metadata;
	}

//	private void createFakeMetadata(MetadataFactory mf) {
//
//		Table example = mf.addTable("example");
//		Column id = mf.addColumn("price", TypeFacility.RUNTIME_NAMES.FLOAT,
//				example);
//		Column name = mf.addColumn("weight", TypeFacility.RUNTIME_NAMES.FLOAT,
//				example);
//		Column age = mf.addColumn("popularity",
//				TypeFacility.RUNTIME_NAMES.INTEGER, example);
//	}

	
	private void testTranslation(String sql, String expectedSolrOutput) throws IOException, Exception{
		Select select = (Select)getCommand(sql);
		
		SolrSQLHierarchyVistor visitor = new SolrSQLHierarchyVistor(this.utility.createRuntimeMetadata());
//		System.out.println(visitor.getParams().getFields());
		visitor.visitNode(select);		
		Assert.assertEquals(expectedSolrOutput, visitor.getParams().getFields());

	}
	

	public Command getCommand(String sql) throws IOException, Exception {
		
		  CommandBuilder builder = new CommandBuilder(setUp(ObjectConverterUtil.convertFileToString(UnitTestUtil.getTestDataFile("exampleTBL.dll")), "exampleVDB", "exampleModel"));
		  return builder.getCommand(sql);
		
	} 

	@Test
	public void testSelectColumns() throws Exception {
		
		  
		//column test, all columns translates to price, weight and popularity
		  testTranslation("select * from example", "price,weight,popularity");
		  testTranslation("select price from example", "price");
		  
		  //test multi-tables columns
		  //testTranslation(sql, expectedSolrOutput);
		 
	}
	@Test
	public void testSelectFrom() throws Exception {}
	@Test
	public void testSelectFromJoin() throws Exception {}
	@Test
	public void testSelectWhen() throws Exception {}
	@Test
	public void testSelectWhenOr() throws Exception {}
	@Test
	public void testSelectWhenLike() throws Exception {}
	@Test
	public void testSelectWhenNot() throws Exception {}
	@Test
	public void testSelectWhenIn() throws Exception {}
	@Test
	public void testSelectWhenAndOr() throws Exception {}
	@Test
	public void testSelectWhenAnd() throws Exception {}
	@Test
	public void testSelectGroupBy() throws Exception {}
	@Test
	public void testSelectWhenOrderBy() throws Exception {}
	@Test
	public void testSelectWhenComparison() throws Exception {
//		=
//		<
//		>
//		<=
//		>=
//		!=
	}
}
