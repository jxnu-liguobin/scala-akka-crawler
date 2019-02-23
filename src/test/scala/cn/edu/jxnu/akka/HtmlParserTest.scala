package cn.edu.jxnu.akka

import org.htmlparser.tags.LinkTag
import org.htmlparser.visitors.ObjectFindingVisitor
import org.htmlparser.{Node, Parser}
import org.junit.Assert.assertTrue
import org.junit.Test

class HtmlParserTest {

    @Test def testLinkExtraction() = {
        val parser: Parser = new Parser("http://www.baidu.com")
        val visitor: ObjectFindingVisitor = new ObjectFindingVisitor(classOf[LinkTag])
        parser.visitAllNodesWith(visitor)
        val links: Array[Node] = visitor.getTags()
        assertTrue(links.length > 0)

        for (i <- 0 until links.length) {
            val linkTag = links {
                i
            }.asInstanceOf[LinkTag]
            print("\"" + linkTag.getLinkText() + "\" => ")
            println(linkTag.getLink())
        }

    }

}
