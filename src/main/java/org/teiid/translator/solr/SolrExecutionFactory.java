package org.teiid.translator.solr;

import javax.resource.cci.ConnectionFactory;

import org.teiid.translator.solr.execution.SolrQueryExecution;
import org.teiid.translator.solr.metadata.SolrMetadataProcessor;
import org.teiid.language.QueryExpression;
import org.teiid.language.Select;
import org.teiid.logging.LogConstants;
import org.teiid.logging.LogManager;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.resource.spi.BasicConnectionFactory;
import org.teiid.resource.spi.BasicManagedConnectionFactory;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.ResultSetExecution;
import org.teiid.translator.Translator;
import org.teiid.translator.TranslatorException;

@Translator(name = "solr", description = "A translator for Solr search platform")
public class SolrExecutionFactory extends
		ExecutionFactory<ConnectionFactory, SolrConnection> {

	public SolrExecutionFactory() {
		// connect to eis

		// query eis
	}

	@Override
	public void start() throws TranslatorException {
		super.start();
		LogManager.logTrace(LogConstants.CTX_CONNECTOR,
				"Solr Executionfactory Started");
	}

	@Override
	public ResultSetExecution createResultSetExecution(QueryExpression command,
			ExecutionContext executionContext, RuntimeMetadata metadata,
			SolrConnection connection) throws TranslatorException {
		return new SolrQueryExecution((Select) command, executionContext, metadata, connection);
	}

/*	@Override
	public void getMetadata(MetadataFactory metadataFactory,
			SolrQueryExecution conn) throws TranslatorException {
			SolrMetadataProcessor processor = new SolrMetadataProcessor(metadataFactory, conn.keyspaceInfo());
			processor.processMetadata();
	}

	@Override
	*/
/*	TODO
 * @Override
	public boolean supportsOrderBy() {
		return true;
	}*/

/*	TODO
	@Override
	public boolean supportsCompareCriteriaEquals() {
		return true;
	}*/

/*	@Override
	public boolean supportsInCriteria() {
		return true;
	}*/

/*	@Override
	public boolean supportsRowLimit() {
		return true;
	}*/

/*	@Override
	public boolean supportsNotCriteria() {
		return true;
	}*/

/*	@Override
	public boolean supportsCompareCriteriaOrdered() {
		return true;
	}*/

/*	@Override
	public boolean supportsLikeCriteria() {
		return true;
	}*/

}
