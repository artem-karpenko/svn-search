package ua.org.yozh

import org.tmatesoft.svn.core.{SVNNodeKind, SVNLogEntryPath}
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import org.tmatesoft.svn.core.io.SVNRepository

/**
 * @author artem
 */
class LogIndexingVisitor(repository: SVNRepository, indexer: TextIndexer) extends SvnLogEntryPathVisitor {
  val logger = LoggerFactory.getLogger(classOf[LogIndexingVisitor])

  def visit(path: String, entry: SVNLogEntryPath, revision: Long) {
    if (entry.getKind == SVNNodeKind.FILE) {
      if (entry.getType == 'A') {
        logger.info("Adding new file " + entry.getPath)

        val contents = new ByteArrayOutputStream()
        repository.getFile(entry.getPath, revision, null, contents)

        indexer.index(entry.getPath.substring(entry.getPath.lastIndexOf('/') + 1),
          entry.getPath.substring(0, entry.getPath.lastIndexOf('/')), contents.toString("UTF-8"))
      } else if (entry.getType == 'D') {
        logger.info("Deleting file " + entry.getPath)

        indexer.delete(entry.getPath)
      }
    }
  }
}
