package ua.org.yozh

/**
 * @author artem
 */
class Indexer(val repoUrl: String, username: String, password: String) {
  def index() {
    println("Indexing repo " + repoUrl)
  }
}
