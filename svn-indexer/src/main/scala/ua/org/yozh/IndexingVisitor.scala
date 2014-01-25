package ua.org.yozh

import org.tmatesoft.svn.core.{SVNNodeKind, SVNDirEntry}
import org.tmatesoft.svn.core.io.SVNRepository
import java.io.{File, ByteArrayOutputStream}
import org.apache.lucene.util.Version
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.index.{IndexWriterConfig, IndexWriter}
import org.apache.lucene.document.{TextField, Field, Document}
import org.apache.lucene.analysis.core.SimpleAnalyzer
import org.slf4j.LoggerFactory

/**
 * @author artem
 */
class IndexingVisitor(repository: SVNRepository, indexer: TextIndexer) extends SvnEntryVisitor {
  val logger = LoggerFactory.getLogger(classOf[IndexingVisitor])

  def visit(path: String, entry: SVNDirEntry, revision: Long) {
    if (entry.getKind == SVNNodeKind.FILE) {
      logger.info("Adding file " + path + "/" + entry.getName)

      val contents = new ByteArrayOutputStream()
      repository.getFile(path + "/" + entry.getName, revision, null, contents)
      indexer.index(entry.getName, path, contents.toString("UTF-8"))
    }
  }
}
