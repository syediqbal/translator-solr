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

	
	private void testTranslation(String sql, String expectedSolr) throws IOException, Exception{
		Select select = (Select)getCommand(sql);
		
		SolrSQLHierarchyVistor visitor = new SolrSQLHierarchyVistor(this.utility.createRuntimeMetadata());
		visitor.visitNode(select);
		System.out.println(visitor.getShortFieldName(0));
		//Assert.assertEquals(expectedCQL, visitor.getTranslatedSQL());
	}
	

	public Command getCommand(String sql) throws IOException, Exception {
		
		  CommandBuilder builder = new CommandBuilder(setUp(ObjectConverterUtil.convertFileToString(UnitTestUtil.getTestDataFile("exampleTBL.dll")), "exampleVDB", "exampleModel"));
		  return builder.getCommand(sql);
		
	} 

	@Test
	public void testSelect() throws Exception {
		
		  testTranslation("select * from example", "SELECT price, weight, popularity from example");
//		  testTranslation("select name,age from Person",
//		  "SELECT name, age FROM Person");
//		  testTranslation("select * from Person", "SELECT * FROM Person");
//		  testTranslation("select count(*) from Person limit 10",
//		  "SELECT COUNT(*) FROM Person LIMIT 10"); testTranslation(
//		  "select * from Person where id=1 and age>=18 and age<=100",
//		 "SELECT * FROM Person WHERE id = 1 AND age >= 18 AND age <= 100");
//		  testTranslation("select * from Person where id in(1,2,3)",
//		  "SELECT * FROM Person WHERE id IN (1, 2, 3)");
		 
	}
}
