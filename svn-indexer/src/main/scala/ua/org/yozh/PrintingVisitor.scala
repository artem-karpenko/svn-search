package ua.org.yozh

import org.tmatesoft.svn.core.SVNDirEntry
import org.slf4j.LoggerFactory

/**
 * @author artem
 */
class PrintingVisitor extends SvnEntryVisitor {
  val logger = LoggerFactory.getLogger(classOf[PrintingVisitor])

  @Override
  def visit(path: String, entry: SVNDirEntry) {
//    logger.info(entry.getRelativePath())
//    logger.info(entry.getName)
    logger.info((if (path.equals("")) "" else path + "/") +  entry.getName)
  }
}
