package ua.org.yozh

import org.slf4j.LoggerFactory
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl
import org.tmatesoft.svn.core.io.SVNRepositoryFactory
import org.tmatesoft.svn.core.{SVNLogEntryPath, SVNLogEntry, SVNNodeKind, SVNURL}
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager
import java.util
import java.util.Map.Entry
import scala.collection.JavaConversions._

/**
 * @author artem
 */
class LogHistoryTraversal(repoUrl: String, username: String, password: String,
                           startRevision: Long, var endRevision: Long, indexer: TextIndexer) {
  val logger = LoggerFactory.getLogger(classOf[RepositoryTraversal])
  val visitors = scala.collection.mutable.ArrayBuffer[SvnLogEntryPathVisitor]()

  DAVRepositoryFactory.setup()
  SVNRepositoryFactoryImpl.setup()

  val repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(repoUrl))
  repository.setAuthenticationManager(new BasicAuthenticationManager(username, password))

  private val nodeKind: SVNNodeKind = repository.checkPath("", -1)
  if (nodeKind == SVNNodeKind.NONE)
    throw new TraversalException(repoUrl, "No entry found at given path")
  else if (nodeKind == SVNNodeKind.FILE)
    throw new TraversalException(repoUrl, "File found when directory expected")

  if (endRevision == -1) {
    endRevision = repository.getLatestRevision
  }

  addVisitor(new LogIndexingVisitor(repository, indexer))

  def addVisitor(visitor: SvnLogEntryPathVisitor) {
    visitors += visitor
  }

  def traverse() {
    logger.info("Indexing changes between " + startRevision + " and " + endRevision + " revision")

    val entries = new util.ArrayList[SVNLogEntry]()
    repository.log(new Array[String](0), entries, startRevision, endRevision, true, true)

    for (entry: SVNLogEntry <- entries) {
      if (entry.getChangedPaths.size() > 0) {
        for (path: Entry[String, SVNLogEntryPath] <- entry.getChangedPaths.entrySet()) {
          for (visitor <- visitors) {
            visitor.visit(path.getKey, path.getValue, entry.getRevision)
          }
        }
      }
    }
  }
}
