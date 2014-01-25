package ua.org.yozh

import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory
import org.tmatesoft.svn.core.io.SVNRepositoryFactory
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl
import org.tmatesoft.svn.core.{SVNDirEntry, SVNNodeKind, SVNURL}
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager
import org.slf4j.LoggerFactory
import scala.collection.JavaConversions._

import java.util

/**
 * @author artem
 */
class RepositoryTraversal(repoUrl: String, username: String, password: String, indexer: TextIndexer,
                           var revision: Long) {
  val logger = LoggerFactory.getLogger(classOf[RepositoryTraversal])

  DAVRepositoryFactory.setup()
  SVNRepositoryFactoryImpl.setup()

  val repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(repoUrl))
  repository.setAuthenticationManager(new BasicAuthenticationManager(username, password))

  private val nodeKind: SVNNodeKind = repository.checkPath("", -1)
  if (nodeKind == SVNNodeKind.NONE)
    throw new TraversalException(repoUrl, "No entry found at given path")
  else if (nodeKind == SVNNodeKind.FILE)
    throw new TraversalException(repoUrl, "File found when directory expected")

  if (revision == -1) {
    revision = repository.getLatestRevision
  }

  val visitors = scala.collection.mutable.ArrayBuffer[SvnEntryVisitor]()
  addVisitor(new IndexingVisitor(repository, indexer))

  def addVisitor(visitor: SvnEntryVisitor) {
    visitors += visitor
  }

  def traverse() {
    logger.info("Indexing everything at revision " + revision)
    traverse("")
  }

  private def traverse(path: String) {
    val children = new util.ArrayList[SVNDirEntry]()
    repository.getDir(path, revision, false, children)
    for (entry: SVNDirEntry <- children) {

      for (visitor <- visitors) {
        visitor.visit(path, entry, revision)
      }

      if (entry.getKind == SVNNodeKind.DIR) {
        traverse(if (path.equals("")) entry.getName else path + "/" + entry.getName)
      }
    }
  }
}
