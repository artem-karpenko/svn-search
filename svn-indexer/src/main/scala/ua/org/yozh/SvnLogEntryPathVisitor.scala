package ua.org.yozh

import org.tmatesoft.svn.core.SVNLogEntryPath

/**
 * @author artem
 */
trait SvnLogEntryPathVisitor {
  def visit(path: String, entry: SVNLogEntryPath, revision: Long)
}
