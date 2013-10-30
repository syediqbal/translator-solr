package org.teiid.translator.solr.execution;

import java.util.ArrayList;
import java.util.List;

import javax.resource.cci.ResultSet;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
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
	private SolrDocumentList docs;
	private int docNum = 0;
	private int docIndex = 0;

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
		visitor = new SolrSQLHierarchyVistor(metadata, logger);
		// visitor.translateSQL(query);
		// build query in solr instance
		// setQuery
		// set query order
		// sort clause
		// setFields

		// traverse commands
		visitor.visitNode(query);

		// get query fields
		fieldList = visitor.getFieldNameList();

		// add query fields
		for (String field : fieldList) {
			params.addField(field);
		}

		// TODO set offset
		// TODO set row result limit
		//

		// execute query and somewhere in here do translation
		queryResponse = connection.executeQuery(params);

//		docs = queryResponse.getResults(); //change to iterator? how does iterator work?

		docNum = (int) docs.getNumFound();

		/*
		 * TODO write logic to handle limiting the number of docs found
		 * logger.logDetail("Total docs returned: " + numFound); if(initialLimit
		 * != -1 && initialLimit < numFound) { numToRetrieve = initialLimit; }
		 * else { numToRetrieve = numFound; }
		 */
	}

	@Override
	public List<?> next() throws TranslatorException, DataNotAvailableException {

		final List<Object> values = new ArrayList<Object>();

		if (docIndex < docNum) {
			// iterate through columns
			for (String field : fieldList) {
				 //TODO map doc field values to metadata space 
			}
			
			docIndex = docIndex + 1;
			return values;
		} else {
			return null;
		}
	}

}
