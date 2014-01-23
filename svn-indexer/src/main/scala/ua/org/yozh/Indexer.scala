package ua.org.yozh

/**
 * @author artem
 */
class Indexer(val repoUrl: String, username: String, password: String) {
  def index() {
    val repositoryTraversal = new RepositoryTraversal(repoUrl, username, password)
    repositoryTraversal.addVisitor(new PrintingVisitor())
    repositoryTraversal.traverse("")
    repositoryTraversal.close()
  }
}
