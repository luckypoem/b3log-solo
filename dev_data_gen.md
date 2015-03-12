开发需要生成一些假数据，以方便测试、调试。下面列出目前已有的一些生成工具及使用方法：

<ol>
<li>文章生成，/dev/articles/gen/${num}</li>
${num} 是需要生成文章的数量，比如浏览器直接访问  /dev/articles/gen/100，就会生成 100 篇文章（作者全为管理员）。<br>
</ol>


---

_注意_：所有开发相关工具
<ul>
<li>URL 路径都是在 /dev/ 下面</li>
<li>只有在开发模式下能够使用，生产环境访问无效</li>
</ul>