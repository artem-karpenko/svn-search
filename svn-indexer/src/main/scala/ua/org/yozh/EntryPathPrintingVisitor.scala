package ua.org.yozh

import org.tmatesoft.svn.core.SVNLogEntryPath
import org.slf4j.LoggerFactory

/**
 * @author artem
 */
class EntryPathPrintingVisitor extends SvnLogEntryPathVisitor {
  val logger = LoggerFactory.getLogger(classOf[EntryPathPrintingVisitor])

  def visit(path: String, entry: SVNLogEntryPath, revision: Long) {
    logger.info(String.valueOf(entry.getType))
    logger.info(entry.getKind.toString)
    logger.info(entry.getPath)
  }

  def close() { }
}
