package ua.org.yozh

/**
 * @author artem
 */
class Indexer(val repoUrl: String, username: String, password: String) {
  def index() {
    val indexer = new TextIndexer

//    val repositoryTraversal = new RepositoryTraversal(repoUrl, username, password, indexer, 2)
//    repositoryTraversal.addVisitor(new PrintingVisitor())
//    repositoryTraversal.traverse()

    val logTraversal = new LogHistoryTraversal(repoUrl, username, password, 3, 4, indexer)
//    logTraversal.addVisitor(new EntryPathPrintingVisitor)
    logTraversal.traverse()

    indexer.close()
  }
}
