package cn.edu.nju.cs.seg.listener;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.pojo.*;
import cn.edu.nju.cs.seg.service.*;
import cn.edu.nju.cs.seg.util.LuceneIndexUtil;
import cn.edu.nju.cs.seg.util.MD5Util;
import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

/**
 * Created by fwz on 2017/6/26.
 */
@Service
public class StartupListener {

    private final String[] usernames = {
            "Alistar", "Blitzcrank",
            "Curry", "David",
            "Evelynn", "Fiddlesticks",
            "Gragas", "Heimerdinger",
            "Ira", "Jinx",
            "Katarina", "Lux",
            "Marlon", "Nasus",
            "Oscar", "Pantheon",
            "Quinn", "Riven",
            "Shen", "Twitch",
            "Uriah", "Victor",
            "Will", "XinZhao",
            "Yorick", "Zed",
            "小明", "大明", "小绿", "小红",
            "小缘", "小z", "小美", "小真"
    };

    private final String[] studioNames = {
            "spring", "node",
            "apache", "django",
            "angular", "vue",
            "less", "react",
            "ajax", "jquery",
            "bootstrap", "easyui",
            "eclipse", "php",
            "Servlet", "csharp",
            "python", "java"
    };

    private final String[] bios = {
            "愿君此生长如意，往来山川皆故人",
            "植物尚能多肉，何况人乎？",
            "专业造轮子，拉黑抢前排。http://gaclib.net",
            "前端开发、JavaScript、前端工程师 话题的优秀回答者",
            "第一中文COS绘画小说社区",
            "计算机图形学、C++ 话题的优秀回答者",
            "有情怀→渴望做出一款颠覆性App的代码工作者",
            "知乎“温赵轮”三大软狗之一。",
            "知乎吃枣药丸",
            "编程、编译原理、编程语言 等 7 个话题的优秀回答者",
            "不会弹吉他的军迷不是一个好黑客",
            "物理学工作者",
            "传媒创业者",
            "Java吸星大法，独创Hbase大数据武学、算法内功，天地孤影任我行！",
            "辞职不干 回家喂猪 结果老家不让喂了",
            "用户体验/设计研究",
            "GPU Architect",
            "『河马牙医』联合创始人，口腔健康服务顾问。",
            "IDL face group/Deep Learning/Face Recognition/GAN",
            "梦游者。电竞大师。开发者",
            "不写程序的数据工程师不是好产品经理",
            "国内领先的前沿科技媒体和产业服务平台",
            "深度学习/认真严谨地做出有用的回答",
            "耶鲁大学精神医学博士后、香港大学精神医学博士",
            "程序员，B站UP"
    };

    private final String[] departments = {
            "计算机科学与技术系",
            "软件学院",
            "电子科学与技术学院",
            "地球与海洋科学学院",
            "社会学院",
            "商学院",
            "工程管理学院",
            "法学院",
            "大气科学学院",
            "环境学院",
            "匡亚明学院",
            "化学院",
            "天文与空间科学学院",
            "物理学院",
            "数学系",
            "文学院",
            "医学院",
            "新闻传播学院"
    };


    private final String[] questionTitle = {
            "脚手架类的命令行工具用到了哪些技术?",
            "什么时候你不能使用箭头函数？",
            "有哪些看似简单其实非常精妙的代码？",
            "推送的演变",
            "当一个颜值很高的程序员是怎样一番体验？",
            "维护一个大型开源项目是怎样的体验？",
            "本人高一学生，想抽空学习编程(0基础)，想请教各位大神意见或经历分享。感谢！？",
            "IOS平台TensorFlow实践有什么？",
            "你见过最震撼的自然景观是什么？",
            "你们看过的最毁三观的作品是什么?",
            "放弃一个很爱的人是什么感觉？",
            "程序员每天会阅读哪些技术网站或者公众号来提高能力？",
            "科技大佬在严格控制孩子玩手机？",
            "为什么体育竞技中客场对球员影响如此大？",
            "如何设计并实现一个通用的应用运维管控平台",
            "有哪些圈子里才知道的小秘密？",
            "我们为什么选择用 Python 来开发 Quora",
            "为什么我将知乎文章保存在本地后不能打开?",
            "《冰与火之歌》有什么细思恐极的情节？",
            "如何评价人民网狠批《王者荣耀》「陷害人生、负能量、砒霜」？",
            "乐视的财务状况是从什么时候开始失控的？有哪些标志性事件？,",
            "请问python现在学习前景怎么样？就业好吗？那个培训机构靠谱？",
            "从 React 绑定 this，看 JS 语言发展和框架设计",
            "你遇到过哪些高质量的C++面试?",
            "shellcode原理是啥？",
            "有没有必要把机器学习算法自己实现一遍？",
            "要变成R大那样的牛人，我应该怎样度过每一天？",
            "有哪些 Java 源代码看了后让你收获很多，代码思维和能力有较大的提升？",
            "既然在变量前加一个＆就可以得到地址，为什么还需要指针？",
            "有哪些听起来很厉害的专业术语？",
            "人性本恶，机器本善？",
            "如何评价百度 Create 2017 AI 开发者大会？",
            "火是什么物质组成的？",
            "独立开发一个 App 是一种怎样的体验？",
            "c语言printf(\"xyz-123\"+2)为什么结果是z-123？",
            "Spark Streaming有没有计划引入Flink 那种真正的流式处理？",
            "对即将赴美留学的同学，作为学长姐的你有哪些保障自己安全的建议给他们？"
    };


    private final String[] contents = {
            "真的，小时候很喜欢，现在被恶心到了，狼吃羊天经地义，羊防狼也天经地义，但说实话刚开始还行，后来很多时候都是羊村那一堆羊先来找灰太狼的麻烦，特别是小灰灰出来之后，小灰灰才是三观最不正的人，心疼灰太狼担心儿子，儿子却总是和父亲对着干。看的我真难受。",
            "起因是我在一个app里写东西，具体内容就不透露了反正我得到了编辑的所谓赏识，然后加了微信中间也发生了很多糟心事不一一详细描述了，我为了保持自己呆萌宝宝的可爱形象一直装傻。",
            "无论是创建一个新的系统，还是添加功能到现有的系统中，我总是从一个简单到几乎没有任何所需功能的版本启动，然后再一步一步地解决问题，直到满意为止。我从来没有妄想过能够一步登天。相反，我一边开发一边学习，同时新掌握的信息还可以用于解决方案中",
            "自己做的 NodeJS 项目，想实现像 Express 那样 $express myapp 就自动生成了项目结构和相关文件，请问都需要用到哪些技术？\n\n或者像 VUE的命令行工具 vue init webpack my-project  那样的效果也行。\n\n如果能够简单科普一下细节就更感谢了。",
            "共 2670 字，读完需 5 分钟。编译自 Dmitri Pavlutin 的文章，对原文内容做了精简和代码风格优化。ES6 中引入的箭头函数可以让我们写出更简洁的代码，但是部分场景下使用箭头函数会带来严重的问题，有哪些场景？会导致什么问题？该怎么解决，容我慢慢道来。",
            "c c++ java python object-c java script  Visual Basic PHP 汇编\nMATLAB。。。。。",
            "建议用第三方库或工具，而不是自己实现。比如bytemaster/boost_reflect",
            "一个专业的营销团队必然要考虑到这种细节，顾客 来店里看你的产品，他会试着把你的产品跟别人的产品进行比较，这时候销售怎么回答。如果任由销售自由发挥，那保不定销售就会说『其实我觉得锤子手机更好呢』。那公司掏了钱请你们这些销售来干什么，给我拆台？所以必然对他们有要求，话术要标准化。客人最常问的几十个问题，标准的回答是什么。这是合格的营销必须做到的。哪家要是没做到这种程度，粗放式经营， 那这家企业可能已经不存在了，或者三年之内就会被淘汰。\n",
            "在职业系统理论里, 一个职业要想证明其存在意义和合理性,就不仅要有与实际接轨的业务,更关键的是要有抽象知识.   一个在系统内部拥有话语权和管辖权的人,  往往是直接与抽象知识打交道,远离实际业务与委托方的人. 回到题目中, 你之所以认为[R大那样的牛人], 并不是因为R大使用 java 做出怎样了不得的业务---- 那些往往在委托人那里起到决定作用,但对职业系统内部无效; 而是因为R大对 Java 底层虚拟机研究颇深,  就是掌握抽象知识话语权的人.\n",
            "因为服务器就是可以链接公网的机器，无论想做什么只要有想法就可以去尝试实现。亲爱的阿里云同学可能是需要推广材料了，被企业邀请还是头一次，不过我真没用过阿里云。有空可以试试。",
            "唔，还真没特别留意过现成的工具里有哪些能完全headless并且能做allocation profiling。题主的环境是完全没办法通过jstatd之类的方式远程用VisualVM么？",
            "如果生成环境的机器上任何数据都不能向外拷贝的话这事情也挺痛苦的。本来想说可能可以用Oracle JDK的收费功能，Java Flight Recorder / Java Mission Control，来在生产环境中在进程内用JFR收集profile存到本地磁盘，然后把profile文件远程抓到本地离线用JMC打开来看。不能拷贝数据出来的话就用不了这招了（包括其它一些类似做法的profiler也都用不了了）",
            "无所谓了，成熟的机器学习也好，最新的深度学习人工智能也罢，它们的火热都是社会发展自然规律的产物，从事这个行业的人也同样要受着优胜劣汰物竞天择的自然规律束缚，所以再多人涌入这个行业最终生存下来的也只是由供需关系决定",
            "半年在没人教的情况下有这个水平算是不容易。指出后能立即判断出这里有重复执行也是好评，但是正职程序员是不允许出现这种低级错误的。",
            "那时候我还是个小白，用虚拟机装了个CentOS系统来玩，但是总也装不上，在论坛上求助也没人理。半天终于有个人说在某网站看过这个问题，我又找了大半天才找到这个网站，最后也还是没解决问题。就这样，一个系统装了一天半。",
            "人类历史上，只有渡过红海的那一小撮人走出了非洲，走向了全世界。然而到底有多少人，义无反顾地渡海，寻找新家园而困死在了海上？他们是伟大的先驱者。然而，没人纪念他们，没人记住他们。",
            "还用说，当然是DOTA WTF啊举两个例子（因为太多经典教案了，数不清，所以举两个例子就好。",
            "能见证每天在用的编程语言不断演化是一件让人非常兴奋的事情，从错误中学习、探索更好的语言实现、创造新的语言特性是推动编程语言版本迭代的动力。JS 近几年的变化就是最好的例子， 以 ES6 引入的箭头函数（arrow functions）、class 等特性为代表，把 JS 的易用性推到了新的高度。",
            "关于 ES6 中的箭头函数，网上有很多文章解释其作用和语法，如果你刚开始接触 ES6，可以从这里开始。任何事物都具有两面性，语言的新特性常常被误解、滥用，比如箭头函数的使用就存在很多误区。接下来，笔者会通过实例介绍该避免使用箭头函数的场景，以及在这些场景下该如何使用函数表达式（function expressions）、函数声明或者方法简写（shorthand method）来保障代码正确性和可读性。",
            "JS 中对象方法的定义方式是在对象上定义一个指向函数的属性，当方法被调用的时候，方法内的 this 就会指向方法所属的对象\n因为箭头函数的语法很简洁，可能不少同学会忍不住用它来定义字面量方法",
            "calculator.sum 使用箭头函数来定义，但是调用的时候会抛出 TypeError，因为运行时 this.array 是未定义的，调用 calculator.sum 的时候，执行上下文里面的 this 仍然指向的是 window，原因是箭头函数把函数上下文绑定到了 window 上，this.array 等价于 window.array，显然后者是未定义的。",
            "同样的规则适用于原型方法（prototype method）的定义，使用箭头函数会导致运行时的执行上下文错误",
            "this 是 JS 中很强大的特性，可以通过多种方式改变函数执行上下文，JS 内部也有几种不同的默认上下文指向，但普适的规则是在谁上面调用函数 this 就指向谁，这样代码理解起来也很自然，读起来就像在说，某个对象上正在发生某件事情。",
            "但是，箭头函数在声明的时候就绑定了执行上下文，要动态改变上下文是不可能的，在需要动态上下文的时候它的弊端就凸显出来。比如在客户端编程中常见的 DOM 事件回调函数（event listenner）绑定，触发回调函数时 this 指向当前发生事件的 DOM 节点，而动态上下文这个时候就非常有用",
            "在全局上下文下定义的箭头函数执行时 this 会指向 window，当单击事件发生时，浏览器会尝试用 button 作为上下文来执行事件回调函数，但是箭头函数预定义的上下文是不能被修改的，这样 this.innerHTML 就等价于 window.innerHTML，而后者是没有任何意义的。",
            "构造函数中的 this 指向新创建的对象，当执行 new Car() 的时候，构造函数 Car 的上下文就是新创建的对象，也就是说 this instanceof Car === true。显然，箭头函数是不能用来做构造函数， 实际上 JS 会禁止你这么做，如果你这么做了，它就会抛出异常。",
            "构造新的 Message 实例时，JS 引擎抛了错误，因为 Message 不是构造函数。在笔者看来，相比旧的 JS 引擎在出错时悄悄失败的设计，ES6 在出错时给出具体错误消息是非常不错的实践。",
            "箭头函数允许你省略参数两边的括号、函数体的花括号、甚至 return 关键词，这对编写更简短的代码非常有帮助。这让我想起大学计算机老师给学生留过的有趣作业：看谁能使用 C 语言编写出最短的函数来计算字符串的长度，这对学习和探索新语言特性是个不错的法子。但是，在实际的软件工程中，代码写完之后会被很多工程师阅读，真正的 write once, read many times，在代码可读性方面，最短的代码可能并不总是最好的。一定程度上，压缩了太多逻辑的简短代码，阅读起来就没有那么直观",
            "multiply 函数会返回两个数字的乘积或者返回一个可以继续调用的固定了一个参数的函数。代码看起来很简短，但大多数人第一眼看上去可能无法立即搞清楚它干了什么，怎么让这段代码可读性更高呢？有很多办法，可以在箭头函数中加上括号、条件判断、返回语句，或者使用普通的函数",
            "箭头函数无疑是 ES6 带来的重大改进，在正确的场合使用箭头函数能让代码变的简洁、短小，但某些方面的优势在另外一些方面可能就变成了劣势，在需要动态上下文的场景中使用箭头函数你要格外的小心，这些场景包括：定义对象方法、定义原型方法、定义构造函数、定义事件回调函数。",
    };

    //从bilibili和网易云音乐搬来的评论
    private final String[] comments = {
            "没听懂",
            "关注一年多一点点了，没错没错，泠鸢果然还是最适合这种稍微带点忧伤的慢节奏的调调，她的声线空灵并且带着一种别样的温柔，就跟你的名字一样，不经意之间就能将你捕捉。额，她应该是相当有特色的唱见了吧（￣▽￣），希望她能多出些类似这种既温柔又空灵的曲子，不经意之间就敲动你心扉的感觉真是很赞！",
            "看到这个就想到网易云热评，生病了一个女同学为我唱了这首歌，现在是我未婚妻。（￣▽￣）然后我们都提醒他醒醒，睡过头了hhh",
            "明明跟原版差别很大啊……´･ᴗ･`。一听就能听出差别，很大的差别",
            "我承担了这季的插入曲日语字幕君，多多指教QAQ",
            "相隔一年多再看到你们第一反应是骄傲，做到了呢，这么久对你们的喜欢都没有减少，反而，有点想你们了...(笑) μ’sic forever",
            "此up主必火，提前抢楼。随手拉出的音乐就如此魔性，不亏是业界毒瘤，这部视频就是专门展现这魔性的天赋",
            "考古，膜拜",
            "\\（´◔౪◔）/每日打卡。欢迎新教徒。",
            "卧槽 预警 ！注意 这不是演习，这是 大写的 卧槽",
            "从3GS到6，已经再也不想碰苹果的东西了。还是1+看着靠谱",
            "抢了两个月小米6，没抢到过，不想到黄牛那里加价买，也没有什么保障，一加首发，官网一次抢到。天意？=_=",
            "。。。。。。可垃圾辣条了",
            "1分多钟的视频明明可以用30秒解释。。。",
            "请勿在公共场合谈论bug",
            "求dalao告诉我一下这个地图叫啥",
            "如果不放到背包就没啥区别了吧？",
            "不过是自命不凡却如纸薄的盾，在心跳一遍遍响彻的夜里 每次却只能带来不变的答案，无论身在何处 却只能不断重复循环。",
            "苦逼歌C位看准园田海未。",
            "对自己越来越迷茫了，不想学习不想玩了不知道自己到底要干啥",
            "o shi ma i，结束",
            "结束了",
            "最心疼英梨梨了，英梨梨是不想离开的，英梨梨给学姐看画的那段，英梨梨笑着哭着说，在伦也身边是画不出来的，待在社团的话，我是不会更进一步的，我就没法成为…伦也需要的最厉害的插画师了啊   哭成狗了这段看的",
            "还记得第一次听这首歌是初二的时候，时间一晃而过，转眼我已到了大学，当我再次听到这首歌的时候，当年的梦想实现了吗，现在的我过得真的快乐吗，我不知道。谢谢你miku，这么多年的陪伴。",
            "听着歌的时候突然想起她，突然泪流满面。再也走不回去的昔日，夕日坂。",
            "再说miku唱歌没感情的我一定要打死他",
            "全世界都坠入爱河  只有我还在岸上[大笑]",
            "想起b站里头这首歌一唱到shout out的时候弹幕就各种刷夏娜[大哭][大哭]",
            "我觉得用网易云的真的是喜欢音乐的！[可爱] 绝对不把网易云音乐推荐给随便的人。",
            "上上次流泪早已不记得是在多少年前，也许是童年也说不定。但上次流泪是在两年前某个清晨刚起床听到这首歌。七年的恋情终结一年后。。。床上的室友刚睡醒，惊讶的问“想家了？！”把我给逗乐了，然后边哭边笑了好久，感觉把一年份的都补回来了。结果现在每次听这歌都既想哭又想笑，快成病了。。。",
            "我就奇怪了。。。崩三的基佬去哪了",
            "德莉莎世界第一可爱！！！！！",
            "如今我才明白，原来这个是讲的现实，越长大越孤单，一个个朋友在人生路上离去渐渐陌生，每个人都有自己的人生都要去完成，大概每个人都会有一个人孤单的走的时候。但是，有你们的记忆就不会孤单，可以勇敢走下去",
            "楼下别闹，我见过最棒的党争应该是：春希小三，春希碧池。",
            "樱花满地集于我心，蝶舞纷飞祈愿相随",
            "在中国，有那时间搞三角恋还不如用青春去刷题。。。",
            "三月雨有另一个天依版本（B站，av452235），个人认为是调教最好的，吐字更清晰语调也更美，尤其是高潮部分的感情非常足，戏腔凄美动人；但是因为没有精良的pv或者名人P主，人气并不是很高。如果大家有兴趣真的可以看看，在云音乐没找到这个版本感到有些可惜呢。",
            "和她相遇在她18岁的年纪，她表面高冷，其实内心极其脆弱，受过伤，我无微不至的关心她呵护她，她也逐渐好转。其实，在最好的年龄遇见自己觉得是对的的那一半，殊为不易，能走下去，更需要勇气，所以，我觉得我应该好好珍惜她。邓婧同学，这是你最喜欢的歌，我在下面评论，你看的见吗？感谢遇见你。",


    };

    private final String[] essayTitles = {
            "C#编程日常1（系列简介）",
            "也许是2017最惊艳的演示工具 - Ludus",
            "扫码支付吃个煎饼，街边摊支付的背后也要有大数据运营",
            "如何用 TensorFlow 打造 Not Hotdog 的移动应用",
            "记者——从最完美的职业 沦为人人喊打",
            "编写更好的Java单元测试的7个技巧",
            "框架基础：ajax设计方案（五）--- 集成promise规范，更优雅的书写代码",
            "动效的基本常识",
            "独家 | 阿里「智能音箱」发布前夕，首次公布自然语言处理成果",
            "PostgreSQL入门到精通——世界上功能最强大的开源数据库",
            "问什么伊拉克叙利亚难民不去迪拜沙特卡塔尔这些国家？",
            "他们开启了医学史上最伟大的发明，但却只留下了一座无字碑",
            "一起学习 Material Design 02",
            "移动端图片上传旋转、压缩的解决方案",
            "穿行于街头，如何选择一款逼格满满的摄影包",
            "ParserGen生成预定义好的各种visitor",
            "重构是永远都无法避免的",
            "GacUI中文教程就写SegmentFault专栏了",
            "一天就写了一个 Razor.js 模板引擎",
            "C++奇技淫巧：通过无脑字符串替换的方法，来把一个递归函数改写成非递归函数",
            "40个Java多线程问题总结",
            "开源推荐：Android图片压缩开源库",
            "Android内存泄漏检测利器：LeakCanary",
            "Bilibili视频播放页面接口整理（不定期更新）",
            "重新认识AndroidStudio和Gradle，这些都是我们应该知道的",
            "iOS 性能监控方案 Wedjat（上篇）"
    };

//    private boolean isTableOk = false;


    @PostConstruct
    public void beforeApplicationStart()
            throws SQLException, IOException, ClassNotFoundException {
        preparing();
        createDataBase();
        initData();
        System.out.println("----------------------------------");
        System.out.println("       Server Started!");
        System.out.println("----------------------------------");

    }

    public void preparing() {
        System.out.println("----------------------------------");
        System.out.println("       Preparing...");
        System.out.println("----------------------------------");
    }

    public void createDataBase()
            throws IOException, ClassNotFoundException, SQLException {
        URL url = Thread.currentThread().getContextClassLoader()
                .getResource("hibernate.cfg.xml");
//        System.out.println(url.getPath());
        File hibernateConfig = new File(url.getPath());
        if (hibernateConfig.exists()) {
            Document document = Jsoup.parse(hibernateConfig, "utf8");
            Elements properties = document.getElementsByTag("property");
            String username = "";
            String password = "";
            String dbUrl = "jdbc:mysql://localhost:3306" +
                    "?useSSL=true";
            String driver = "com.mysql.jdbc.Driver";
            for (Element property : properties) {
                if ("connection.username".equals(property.attr("name"))) {
                    username = property.html();
                }
                if ("connection.password".equals(property.attr("name"))) {
                    password = property.html();
                }
                if ("connection.driver_class".equals(property.attr("name"))) {
                    driver = property.html();
                }
            }
            System.out.println("DataBase UserName: " + username);
            System.out.println("DataBase Password: " + password);
            System.out.println("Driver: " + driver);
            Connection connection = null;
            Class.forName(driver);
            connection = DriverManager.getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            statement.execute("CREATE DATABASE IF NOT EXISTS seg DEFAULT CHARSET=UTF8;");
            connection.close();
        }

    }


    public void initData() throws IOException {

        System.out.println("----------------------------------");
        System.out.println("       Init Data.....");
        System.out.println("----------------------------------");
        if (ServerConfig.INIT_DATA && UserService.findAllUsers().size() == 0) {
//            ServerConfig.NOTIFICATION = false;
            //add users
            Random random = new Random(System.currentTimeMillis());
            for (int i = 0; i < usernames.length; i++) {
                User user = new User("stu" + (i + 1) + "@nju.edu.cn", "1234");
                user.setUsername(usernames[i]);
                user.setBio(bios[random.nextInt(bios.length)]);
                user.setSex(random.nextInt(100) % 2 == 1 ? "female" : "male");
                user.setDepartment(departments[random.nextInt(departments.length)]);
                UserService.add(user);
            }
            System.out.println("User added");
            List<User> users = UserService.findAllUsers();
            //add studios
            for (int i = 0; i < studioNames.length; i++) {
                Studio studio = new Studio(studioNames[i], users.get(i % users.size()));
                studio.setBio(bios[random.nextInt(bios.length)]);
                int studioId = StudioService.add(studio);
                StudioService.addMember(studioId, users.get(i % users.size()).getId());
                StudioService.addMember(studioId, users.get(i % users.size() + 1).getId());
            }
            List<Studio> studios = StudioService.findAllStudios();
            System.out.println("Studios added");
            //add questions
            for (int i = 0; i < questionTitle.length; i++) {
                Question question = new Question(
                        questionTitle[i], contents[random.nextInt(contents.length)],
                        users.get(random.nextInt(users.size())),
                        studios.get(random.nextInt(studios.size())), Question.TYPE_TEXT);
                question.setHeat(random.nextInt(30));
                QuestionService.add(question);
            }
            System.out.println("Questions added");
            //add essays
            for (int i = 0; i < essayTitles.length; i++) {
                Essay essay = new Essay(essayTitles[i],
                        contents[random.nextInt(contents.length)],
                        studios.get(random.nextInt(studios.size())), Essay.TYPE_TEXT);
                essay.setHeat(random.nextInt(20));
                EssayService.add(essay);
            }
            List<Question> questions = QuestionService.findAllQuestions();
            List<Essay> essays = EssayService.findAllEssays();
            //add answers
            for (int i = 0; i < questions.size(); i++) {
                Studio studio = questions.get(i).getStudio();
                List<User> members = StudioService.findUsersByStudioId(studio.getId());
                for (User member : members) {
                    Answer answer = new Answer(
                            contents[random.nextInt(contents.length)],
                            member, questions.get(i), Answer.TYPE_TEXT);
                    AnswerService.add(answer);
                }
            }
            List<Answer> answers = AnswerService.findAllAnswers();
            System.out.println("Answers added");
            //add comments
            for (int i = 0; i < answers.size(); i++) {
                int commentSize = random.nextInt(5);
                for (int j = 0; j < commentSize; j++) {
                    Comment comment = new Comment(users.get(random.nextInt(users.size())),
                            comments[random.nextInt(comments.length)],
                            answers.get(i));
                    CommentService.add(comment);
                }
            }
            for (int i = 0; i < essays.size(); i++) {
                int commentSize = random.nextInt(6);
                for (int j = 0; j < commentSize; j++) {
                    Comment comment = new Comment(users.get(random.nextInt(users.size())),
                            comments[random.nextInt(comments.length)],
                            essays.get(i));
                    CommentService.add(comment);
                }
            }
            System.out.println("Comments added");


            IndexWriter w = LuceneIndexUtil.getIndexWriter();
            w.deleteAll();
            for (User user : users) {
                LuceneIndexUtil.addUser(w, user);
            }
            for (Studio studio : studios) {
                LuceneIndexUtil.addStudio(w, studio);
            }
            for (Question question : questions) {
                LuceneIndexUtil.addQuestion(w, question);
            }
            for (Essay essay : essays) {
                LuceneIndexUtil.addEssay(w, essay);
            }
            w.close();
            System.out.println("Index init");
            String md5 = MD5Util.getFileMD5(new File("avatars/1.png"));

            AvatarService.add(new Avatar(md5, "png"));
            ServerConfig.NOTIFICATION = true;

        }


    }

}
