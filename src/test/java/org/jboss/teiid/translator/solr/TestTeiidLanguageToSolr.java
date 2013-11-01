package org.jboss.teiid.translator.solr;

import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.teiid.cdk.CommandBuilder;
import org.teiid.language.Command;
import org.teiid.language.Select;
import org.teiid.metadata.Column;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.Table;
import org.teiid.query.metadata.CompositeMetadataStore;
import org.teiid.query.metadata.QueryMetadataInterface;
import org.teiid.query.metadata.SystemMetadata;
import org.teiid.query.metadata.TransformationMetadata;
import org.teiid.query.unittest.RealMetadataFactory;
import org.teiid.translator.TypeFacility;
import org.teiid.translator.solr.execution.SolrSQLHierarchyVistor;

@SuppressWarnings("nls")
public class TestTeiidLanguageToSolr {

	private QueryMetadataInterface solrMetadata(String ddl, String vdbName, String modelName) throws Exception {

		MetadataFactory mf = new MetadataFactory("", 1, "", SystemMetadata.getInstance().getRuntimeTypeMap(), new Properties(), "");
		createFakeMetadata(mf);
		TransformationMetadata tm = RealMetadataFactory.fromDDL(ddl, vdbName, modelName);
		return tm;
	}

	private void createFakeMetadata(MetadataFactory mf) {
		
		  Table example =mf.addTable("example"); Column id =
		 mf.addColumn("price", TypeFacility.RUNTIME_NAMES.FLOAT, example);
		  Column name =mf.addColumn("weight",
		  TypeFacility.RUNTIME_NAMES.FLOAT, example);
		 Column age =mf.addColumn("popularity",
				TypeFacility.RUNTIME_NAMES.INTEGER, example);
	}

	
	private void testTranslation(String sql, String expectedCQL){
		Select select = (Select)getCommand(sql);
		
		SolrSQLHierarchyVistor visitor = new SolrSQLHierarchyVistor(null, null);
		visitor.visitNode(select);
//		System.out.println(visitor.visit(select));
		//Assert.assertEquals(expectedCQL, visitor.getTranslatedSQL());
	}
	

	public Command getCommand(String sql) {
		
		  CommandBuilder builder = new CommandBuilder(solrMetadata());
		  return builder.getCommand(sql);
		
	} 

	@Test
	public void testSelect() throws Exception {
		
		  testTranslation("select * from example", "SELECT price, weight, popularity FROM example");
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
