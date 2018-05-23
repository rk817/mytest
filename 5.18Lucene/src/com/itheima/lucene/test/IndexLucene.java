package com.itheima.lucene.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.itheima.lucene.dao.BookDao;
import com.itheima.lucene.dao.BookDaoImpl;
import com.itheima.lucene.pojo.Book;

/**
 * 作业： 写两遍！！！
 * @author lj520
 *
 */
public class IndexLucene {

	@Test
	public void test() throws Exception {
		//1、采集数据；读取了POJO集合
		BookDao daoImpl = new BookDaoImpl();
		List<Book> list = daoImpl.queryList();
		//2、创建文档对象集合
		List<Document> docList = new ArrayList<>();
		//3、遍历此POJO对象集合
		for (Book book : list) {
			//4、把每一个POJO对象放在一个文档中
			Document doc = new Document();
			//5、创建域对象TextField：文本域
			//参数说明：1、指定域名；2、域值；3、是否把数据存储在索引库里面；
			Field idField = new TextField("id", book.getId()+"", Store.YES);
			Field nameField = new TextField("name", book.getName()+"", Store.YES);
			Field priceField = new TextField("price", book.getPrice()+"", Store.YES);
			Field picField = new TextField("pic", book.getPic()+"", Store.YES);
			Field descField = new TextField("desc", book.getDesc()+"", Store.YES);
			//6、再把域放在文档中；
			doc.add(idField);
			doc.add(nameField);
			doc.add(priceField);
			doc.add(picField);
			doc.add(descField);
			//7、把每一个文档放在文档集合中
			docList.add(doc);
		}
		//8、创建分词器（在写入的时候把文档进行分词）  : Lucene中默认自带的英文分词器
//		Analyzer analyzer = new StandardAnalyzer();
		IKAnalyzer analyzer = new IKAnalyzer();
		
		//9、创建指定索引库地址的流对象Directory
		Directory directory = FSDirectory.open(new File("F:\\41\\lucene"));
		//10、创建写入索引库的配置对象IndexWriterConfig
		//参数说明：1、指定Lucene版本号；2、指定分词器
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
		//11、创建写入索引库对象的时候IndexWriter，需要两个参数（1、指定写入索引库的地址刘备；2、写入索引库的配置对象）
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
		//12、把文档集合遍历，一个文档一个文档的写入索引库
		for (Document document : docList) {
			indexWriter.addDocument(document);
		}
		//13、释放资源
		indexWriter.close();
	}

}
