package ua.org.yozh

import org.apache.lucene.analysis.core.SimpleAnalyzer
import org.apache.lucene.util.Version
import org.apache.lucene.store.FSDirectory
import java.io.File
import org.apache.lucene.index.{Term, IndexWriterConfig, IndexWriter}
import org.apache.lucene.document.{TextField, Field, Document}

/**
 * @author artem
 */
class TextIndexer {
  val analyzer = new SimpleAnalyzer(Version.LUCENE_46)
  val directory = FSDirectory.open(new File("d:/tmp/index"))
  val indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_46, analyzer))

  def index(name: String, path: String, contents: String) {
    val document = new Document()
    document.add(new Field("contents", contents, TextField.TYPE_STORED))
    document.add(new Field("path", path + "/" + name, TextField.TYPE_STORED))
    val nameField: Field = new Field("name", name, TextField.TYPE_STORED)
    nameField.setBoost(2)
    document.add(nameField)
    indexWriter.addDocument(document)
  }

  def delete(path: String) {
    indexWriter.deleteDocuments(new Term("path", path))
  }

  def commit() {
    indexWriter.commit()
  }

  def close() {
    if (indexWriter != null) {
      indexWriter.close()
    }
  }
}
