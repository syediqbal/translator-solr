package org.teiid.translator.solr.execution;

import java.util.List;

import javax.resource.cci.ResultSet;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.teiid.language.Select;
import org.teiid.logging.LogManager;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.translator.DataNotAvailableException;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.ResultSetExecution;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.solr.SolrConnection;

public class SolrQueryExecution implements ResultSetExecution {

	private RuntimeMetadata metadata;
	private Select query;
	@SuppressWarnings("unused")
	private ExecutionContext executionContext;
	private SolrConnection connection;
	private SolrSQLHierarchyVistor visitor;
	private LogManager logger;
	private SolrQuery params = new SolrQuery();
	private QueryResponse queryResponse = null;
	private String[] fieldList = null;
	

	public SolrQueryExecution(Select command,
			ExecutionContext executionContext, RuntimeMetadata metadata,
			SolrConnection connection) {
		this.metadata = metadata;
		this.query = command;
		this.executionContext = executionContext;
		this.connection = connection;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancel() throws TranslatorException {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute() throws TranslatorException {
		visitor = new SolrSQLHierarchyVistor(metadata,logger);
		//visitor.translateSQL(query);
		//build query in solr instance
		//setQuery
			//set query order
			//sort clause
			//setFields
		
		//traverse commands
		visitor.visitNode(query);
		
		//get query fields
		fieldList  = visitor.getFieldNameList();		
		
		//add query fields
		for(String field : fieldList){
			params.addField(field);			
		}
		
		//TODO set offset
		//TODO set row result limit
		//
		
		
		
		
		//execute query and somewhere in here do translation
		queryResponse = connection.executeQuery(params);
		
	}

	@Override
	public List<?> next() throws TranslatorException, DataNotAvailableException {
		// TODO Auto-generated method stub
		return null;
	}

}
