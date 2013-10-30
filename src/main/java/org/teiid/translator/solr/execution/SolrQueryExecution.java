package org.teiid.translator.solr.execution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.resource.cci.ResultSet;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.teiid.language.QueryExpression;
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
	private Iterator<SolrDocument> docItr;
	private int docNum = 0;
	private int docIndex = 0;
	private Class<?>[] expectedTypes;

	public SolrQueryExecution( QueryExpression command,
			ExecutionContext executionContext, RuntimeMetadata metadata,
			SolrConnection connection) {
		this.metadata = metadata;
		this.query = (Select) command;
		this.executionContext = executionContext;
		this.connection = connection;
		this.expectedTypes = command.getColumnTypes();
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

		docItr = queryResponse.getResults().iterator(); // change to iterator?
														// how does iterator
														// work?

		// docNum = (int) docs.getNumFound();

		/*
		 * TODO write logic to handle limiting the number of docs found
		 * logger.logDetail("Total docs returned: " + numFound); if(initialLimit
		 * != -1 && initialLimit < numFound) { numToRetrieve = initialLimit; }
		 * else { numToRetrieve = numFound; }
		 */
	}

	@Override
	public List<?> next() throws TranslatorException, DataNotAvailableException {

		final List<Object> row = new ArrayList<Object>();

		if (this.docItr != null && this.docItr.hasNext()) {
			// iterate through columns
			SolrDocument doc = this.docItr.next();
			for (int i=0; i < this.visitor.getFieldNameList().length; i++) {
				// TODO map doc field values to metadata space
			}

			return row;
		}
		return null;
	}

}
