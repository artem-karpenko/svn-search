package ua.org.yozh

import org.tmatesoft.svn.core.SVNDirEntry

/**
 * @author artem
 */
trait SvnEntryVisitor {
  def visit(path: String, entry: SVNDirEntry)
}
